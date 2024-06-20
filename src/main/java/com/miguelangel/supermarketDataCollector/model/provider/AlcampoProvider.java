package com.miguelangel.supermarketDataCollector.model.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miguelangel.supermarketDataCollector.entity.Category;
import com.miguelangel.supermarketDataCollector.entity.PriceHistory;
import com.miguelangel.supermarketDataCollector.entity.PriceHistoryId;
import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.Supermarket;
import com.miguelangel.supermarketDataCollector.model.Categories;
import com.miguelangel.supermarketDataCollector.model.IProvider;
import com.miguelangel.supermarketDataCollector.model.OrderProvider;
import com.miguelangel.supermarketDataCollector.model.Supermarkets;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * AlcampoProvider is a service that implements the IProvider interface.
 * This service is responsible for collecting product data from the Alcampo supermarket.
 * It fetches product information from Alcampo's online store and organizes it into entities.
 *
 * @since 2024
 * @author Miguel Angel Moreno Garcia
 */
@Service
@Order(OrderProvider.ALCAMPO)
public class AlcampoProvider implements IProvider {
	private final Logger logger = LoggerFactory.getLogger(AlcampoProvider.class);
    private final Set<PriceHistory> priceHistorySet = new HashSet<>(0);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Supermarket getSupermarket() {
		return Supermarkets.ALCAMPO;
	}

	@Override
	public Set<Product> collectProducts() {
        logger.info("Starting... {}", getSupermarket().getName());
		Set<Product> products = new HashSet<>();
		Hashtable<Category, String[]> dictCategories = getDictCategories();
		dictCategories.forEach((category, categoryIds) -> {
			for (String categoryId : categoryIds) {
				String categoryUri = String.format(getMarketUri(), categoryId);
				Set<String> productIds = getProductIdsFromCategory(categoryUri);

				List<Set<String>> productGroups = new ArrayList<>();
				Set<String> currentGroup = new HashSet<>();
				int count = 0;
				for (String productId : productIds) {
					currentGroup.add(productId);
					count++;
					if (count % 50 == 0) {
						productGroups.add(new HashSet<>(currentGroup));
						currentGroup.clear();
					}
				}
				if (!currentGroup.isEmpty()) {
					productGroups.add(new HashSet<>(currentGroup));
				}

				for (Set<String> productGroup : productGroups) {
					JsonObject productsCluster = getProductsCluster(productGroup);
					if (productsCluster != null) {
						products.addAll(getProductList(productsCluster, category));
					} else {
                        logger.error("Error retrieving products cluster for category: {}", category.getName());
					}
				}
			}
		});
		return products;
	}

	private JsonObject getProductsCluster(Set<String> productIds) {
		try {
			StringJoiner joiner = new StringJoiner(",");
			productIds.forEach(joiner::add);
			String concatenatedIds = joiner.toString();

			HttpClient client = HttpClient.newHttpClient();
            String productsUri = "https://www.compraonline.alcampo.es/api/v5/products/decorate?productIds=%s";
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(String.format(productsUri, concatenatedIds)))
					.GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String responseBody = response.body();
			if (responseBody != null) {
				return new Gson().fromJson(responseBody, JsonObject.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Set<String> getProductIdsFromCategory(String categoryUri) {
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(categoryUri)).GET().build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String responseBody = response.body();
			if (responseBody != null) {
				JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
				JsonObject result = responseJson.getAsJsonObject("result");
				JsonArray productGroups = result.getAsJsonArray("productGroups");
				Set<String> productIds = new HashSet<>();
				for (JsonElement productGroup : productGroups) {
					JsonArray productsArray = productGroup.getAsJsonObject().getAsJsonArray("products");
					for (JsonElement product : productsArray) {
						String productId = product.getAsString();
						productIds.add(productId);
					}
				}
				return productIds;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashSet<>();
	}

	@Override
	public Set<Product> getProductList(JsonObject response, Category category) {
		Set<Product> products = new HashSet<>();
		JsonArray productsArray = response.getAsJsonArray("products");
		for (JsonElement productElement : productsArray) {
			JsonObject productObj = productElement.getAsJsonObject();
			String item_id = productObj.get("retailerProductId").getAsString();
            String prefixId = "AL-";
            String id = prefixId + item_id;
			String name = productObj.get("name").getAsString();
			double price = productObj.get("price").getAsJsonObject().get("current").getAsJsonObject().get("amount")
					.getAsDouble();
			double bulkPrice = productObj.get("price").getAsJsonObject().get("unit").getAsJsonObject().get("current")
					.getAsJsonObject().get("amount").getAsDouble();
			String image = productObj.get("image").getAsJsonObject().get("src").getAsString();
			Supermarket supermarket = getSupermarket();

			boolean availability = productObj.get("available").getAsBoolean();
			boolean onSale = productObj.has("offers");

            Product product = new Product(id, category, supermarket, name, image, availability, onSale);
			PriceHistoryId pricehistoryId = new PriceHistoryId(id, LocalDate.now());
			PriceHistory pricehistory = new PriceHistory(pricehistoryId, product, new BigDecimal(price),
					onSale ? new BigDecimal(price) : new BigDecimal(0), new BigDecimal(bulkPrice));
			if (isInsertable(product, products)) {
				if (isInsertable(pricehistory, priceHistorySet)) {
					product.getPriceHistories().add(pricehistory);
				}
				products.add(product);
			}

		}
		return products;
	}

	@SuppressWarnings("unchecked")
	public <T> boolean isInsertable(T object, Set<T> set) {
		if (object instanceof PriceHistory p) {
            // Check if lastPrice stored is equal to the one inserting
			Query namedQuery = entityManager.createNamedQuery("PriceHistory.lastPrice");
			namedQuery.setParameter("id", p.getId().getProductId());
			namedQuery.setParameter("price", p.getPrice());
			namedQuery.setParameter("salePrice", p.getSalePrice());
			List<Object[]> results = namedQuery.getResultList();
			if (!results.isEmpty()) {
				return false;
			}
		}

		return !set.contains(object);
	}

	@Override
	public String getMarketUri() {
        return "https://www.compraonline.alcampo.es/api/v5/products?limit=50&offset=0&retailerCategoryId=%s&sort=favorite";
	}

	@Override
	public Hashtable<Category, String[]> getDictCategories() {
		Hashtable<Category, String[]> dicCategories = new Hashtable<>();

		dicCategories.put(Categories.ACEITE_ESPECIAS_Y_SALSAS, new String[] { "OC18", "OCTomateySalsas" });
		dicCategories.put(Categories.AGUA_Y_REFRESCOS, new String[] { "OC1103", "OC1101" });
		dicCategories.put(Categories.APERITIVOS, new String[] { "OC120", });
		dicCategories.put(Categories.ARROZ_LEGUMBRES_Y_PASTA, new String[] { "OC140" });
		dicCategories.put(Categories.AZUCAR_CARAMELOS_Y_CHOCOLATE,
				new String[] { "OCAzucaryedulcorante", "OC100802", "OC100902" });
		dicCategories.put(Categories.BEBE, new String[] { "OCC13" });
		dicCategories.put(Categories.BODEGA, new String[] { "OC1107", "OC1151", "OC1152", "OC1153", "OC1156", "OC1154",
				"OC1155", "OC20042022", "OC25042023" });
		dicCategories.put(Categories.CACAO_CAFE_E_INFUSIONES,
				new String[] { "OC100806", "OC100803017", "OC100807", "OC1008" });
		dicCategories.put(Categories.CARNE, new String[] { "OC13" });
		dicCategories.put(Categories.CEREALES_Y_GALLETAS, new String[] { "OC100805", "OC100804" });
		dicCategories.put(Categories.CHARCUTERIA_Y_QUESOS, new String[] { "OC151001", "OC15", "OCQuesos" });
		dicCategories.put(Categories.CONGELADOS, new String[] { "OC200220183" });
		dicCategories.put(Categories.CONSERVAS_CALDOS_Y_CREMAS,
				new String[] { "OC100402", "OC100401", "OC1004", "OCCaldosycremas" });
		dicCategories.put(Categories.CUIDADO_DEL_CABELLO, new String[] { "OC701" });
		dicCategories.put(Categories.CUIDADO_FACIAL_Y_CORPORAL, new String[] { "OC702", "OC704", "OC70013", "OC705",
				"OC706", "OC707", "OC709", "OC7010", "OC70012", "OC70011", "OCEstuchesPerfu" });
		dicCategories.put(Categories.FITOTERAPIA_Y_PARAFARMACIA, new String[] { "OC69" });
		dicCategories.put(Categories.FRUTA_Y_VERDURA, new String[] { "OC1701", "OC1702" });
		dicCategories.put(Categories.HUEVOS_LECHE_Y_MANTEQUILLA, new String[] { "OC16" });
		dicCategories.put(Categories.LIMPIEZA_Y_HOGAR, new String[] { "OCC14" });
		dicCategories.put(Categories.MAQUILLAJE, new String[] { "OC708" });
		dicCategories.put(Categories.MARISCO_Y_PESCADO, new String[] { "OC14", "OC184" });
		dicCategories.put(Categories.MASCOTAS, new String[] { "OC062" });
		dicCategories.put(Categories.PANADERIA_Y_PASTELERIA, new String[] { "OC1281", "OC1282", "OC1009", "OC1011" });
		dicCategories.put(Categories.PIZZAS_Y_PLATOS_PREPARADOS, new String[] { "OC9410", "OC20022018" });
		dicCategories.put(Categories.POSTRES_Y_YOGURES, new String[] { "OC1007" });
		dicCategories.put(Categories.ZUMOS, new String[] { "OC1102", "OC101303" });

		return dicCategories;
	}
}