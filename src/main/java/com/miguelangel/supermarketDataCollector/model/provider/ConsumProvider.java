package com.miguelangel.supermarketDataCollector.model.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * ConsumProvider is a service that implements the IProvider interface.
 * This service is responsible for collecting product data from the Consum supermarket.
 * It fetches product information from Consum's online store and organizes it into entities.
 *
 * @since 2024
 * @author Miguel Angel Moreno Garcia
 */
@Service
@Order(OrderProvider.CONSUM)
public class ConsumProvider implements IProvider {
	private final Logger logger = LoggerFactory.getLogger(ConsumProvider.class);
    private final Set<PriceHistory> priceHistorySet = new HashSet<>(0);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Supermarket getSupermarket() {
		return Supermarkets.CONSUM;
	}

	@Override
	public Set<Product> collectProducts() {
        logger.info("Starting... {}", getSupermarket().getName());
		Set<Product> productList = new HashSet<>();

		try {
			for (Category category : getDictCategories().keySet()) {
				for (String parameter : getDictCategories().get(category)) {
					int page = 1;
					int offset = 0;
					boolean hasNextPage = true;

					while (hasNextPage) {
						String uriParameter = URLEncoder.encode(parameter, StandardCharsets.UTF_8);
						HttpClient client = HttpClient.newHttpClient();
						HttpRequest request = HttpRequest.newBuilder()
								.uri(URI.create(String.format(getMarketUri(), page, offset, uriParameter))).GET()
								.build();

						HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
						JsonObject responseJsonObj = new Gson().fromJson(response.body(), JsonObject.class);
						productList.addAll(getProductList(responseJsonObj, category));
						page++;
						offset += 20;

						if (!responseJsonObj.get("hasMore").getAsBoolean()) {
							hasNextPage = false;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productList;
	}

	@Override
	public Set<Product> getProductList(JsonObject response, Category category) {
		Set<Product> productList = new HashSet<>();

		if (response.has("products")) {
			JsonArray pageProducts = response.getAsJsonArray("products");

			for (JsonElement productElement : pageProducts) {
				JsonObject productObj = productElement.getAsJsonObject();
				JsonObject productData = productObj.get("productData").getAsJsonObject();

				String name = productData.get("description").getAsString();
				String item_id = productObj.get("id").getAsString();
                String prefixId = "CO-";
                String id = prefixId + item_id;
				String image = productData.get("imageURL").getAsString();
				boolean availability = !productData.get("availability").getAsString().equals("0");
				Supermarket supermarket = getSupermarket();

				double price = 0;
				double salePrice = 0;
				double bulkPrice = 0;
				boolean onSale = false;
				JsonObject priceData = productObj.get("priceData").getAsJsonObject();
				if (priceData != null && priceData.has("prices")) {
					JsonArray prices = priceData.getAsJsonArray("prices");

					if (prices.size() > 1) {
						salePrice = prices.get(1).getAsJsonObject().get("value").getAsJsonObject().get("centAmount")
								.getAsDouble();
						bulkPrice = prices.get(1).getAsJsonObject().get("value").getAsJsonObject().get("centUnitAmount")
								.getAsDouble();
						price = salePrice;
						onSale = true;
					} else {
						price = prices.get(0).getAsJsonObject().get("value").getAsJsonObject().get("centAmount")
								.getAsDouble();
						bulkPrice = prices.get(0).getAsJsonObject().get("value").getAsJsonObject().get("centUnitAmount")
								.getAsDouble();
					}

					Product product = new Product(id, category, supermarket, name, image, availability, onSale);
					PriceHistoryId pricehistoryId = new PriceHistoryId(id, LocalDate.now());
					PriceHistory pricehistory = new PriceHistory(pricehistoryId, product, new BigDecimal(price),
							new BigDecimal(salePrice), new BigDecimal(bulkPrice));
					if (isInsertable(product, productList)) {
						if (isInsertable(pricehistory, priceHistorySet)) {
							product.getPriceHistories().add(pricehistory);

						}
						
						productList.add(product);
					}
				}
			}
		}
		return productList;
	}

	@SuppressWarnings("unchecked")
	public <T> boolean isInsertable(T object, Set<T> set) {
		if(object instanceof PriceHistory) {
			PriceHistory p = (PriceHistory)object;
			// Check if lastPrice stored is equal to the one inserting
			Query namedQuery = entityManager.createNamedQuery("PriceHistory.lastPrice");
			namedQuery.setParameter("id", p.getId().getProductId());
			namedQuery.setParameter("price", p.getPrice());
			namedQuery.setParameter("salePrice", p.getSalePrice());
			List<Object[]> results = namedQuery.getResultList();
			if(!results.isEmpty()) {
				return false;
			}
		}
		
		return !set.contains(object);
	}

	@Override
	public String getMarketUri() {
        return "https://tienda.consum.es/api/rest/V1.0/catalog/product?"
                + "page=%d&limit=20&offset=%d&orderById=5&showRecommendations=false&categories=%s";
	}

	@Override
	public Hashtable<Category, String[]> getDictCategories() {
		Hashtable<Category, String[]> dicCategories = new Hashtable<>();

		dicCategories.put(Categories.ACEITE_ESPECIAS_Y_SALSAS,
				new String[] { "1526", "1601", "1616", "1631", "4504", "1591" });

		dicCategories.put(Categories.AGUA_Y_REFRESCOS, new String[] { "1720", "2484", "2482" });

		dicCategories.put(Categories.APERITIVOS, new String[] { "1970", "5262" });

		dicCategories.put(Categories.ARROZ_LEGUMBRES_Y_PASTA, new String[] { "1640", "1654", "1649", "1659" });

		dicCategories.put(Categories.AZUCAR_CARAMELOS_Y_CHOCOLATE,
				new String[] { "4051", "5036", "4003", "1868", "3999" });

		dicCategories.put(Categories.BEBE, new String[] { "2297" });

		dicCategories.put(Categories.BODEGA, new String[] { "1713", "1707", "1701" });

		dicCategories.put(Categories.CACAO_CAFE_E_INFUSIONES, new String[] { "1878", "1868", "1928", "5164" });

		dicCategories.put(Categories.CARNE, new String[] { "4058", "2148" });

		dicCategories.put(Categories.CEREALES_Y_GALLETAS, new String[] { "1894", "1912", "5260", "2559" });

		dicCategories.put(Categories.CHARCUTERIA_Y_QUESOS, new String[] { "4093", "2019", "4070", "2005" });

		dicCategories.put(Categories.CONGELADOS, new String[] { "1783" });

		dicCategories.put(Categories.CONSERVAS_CALDOS_Y_CREMAS,
				new String[] { "2490", "1542", "1577", "1564", "5261" });

		dicCategories.put(Categories.CUIDADO_DEL_CABELLO, new String[] { "1475" });

		dicCategories.put(Categories.CUIDADO_FACIAL_Y_CORPORAL,
				new String[] { "1403", "1413", "5131", "5129", "1452", "1463" });

		dicCategories.put(Categories.FITOTERAPIA_Y_PARAFARMACIA, new String[] { "2243" });

		dicCategories.put(Categories.FRUTA_Y_VERDURA, new String[] { "2179", "2187" });

		dicCategories.put(Categories.HUEVOS_LECHE_Y_MANTEQUILLA,
				new String[] { "2056", "2064", "2065", "2071", "2091", "2094", "2100", "2290" });

		dicCategories.put(Categories.LIMPIEZA_Y_HOGAR, new String[] { "1239" });

		dicCategories.put(Categories.MARISCO_Y_PESCADO, new String[] { "2171" });

		dicCategories.put(Categories.MASCOTAS, new String[] { "2469" });

		dicCategories.put(Categories.PANADERIA_Y_PASTELERIA, new String[] { "1859", "4004", "5153", "1646", "4108" });

		dicCategories.put(Categories.PIZZAS_Y_PLATOS_PREPARADOS, new String[] { "1793", "1669", "1833" });

		dicCategories.put(Categories.POSTRES_Y_YOGURES, new String[] { "2103", "2123" });

		dicCategories.put(Categories.ZUMOS, new String[] { "1779" });

		return dicCategories;
	}
}
