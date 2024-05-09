package com.miguelangel.supermarketDataCollector.model.provider;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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

@Service
@Order(OrderProvider.DIA)
public class DiaProvider implements IProvider {
	private final Logger logger = LoggerFactory.getLogger(DiaProvider.class);
    private final Set<PriceHistory> priceHistorySet = new HashSet<>(0);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Set<Product> collectProducts() {
		logger.info("Starting... " + getSupermarket().getName());
		JsonObject responseJsonObj = null;
		Set<Product> productList = new HashSet<>();

		for (Category category : getDictCategories().keySet()) {
			for (String parameter : getDictCategories().get(category)) {
				int page = 1;
				boolean hasNextPage = true;

				while (hasNextPage) {
					try {
						String uriParameter = URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
						final HttpRequest request = HttpRequest.newBuilder()
								.uri(new URI(
										String.format(getMarketUri(), uriParameter, uriParameter, uriParameter, page)))
								.timeout(Duration.ofSeconds(10)).GET().build();

						final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
								HttpResponse.BodyHandlers.ofString());

						String responseStr = response.body();
						responseJsonObj = new Gson().fromJson(responseStr, JsonObject.class);

						productList.addAll(getProductList(responseJsonObj, category));
						page++;

						if (!responseJsonObj.has("page_product_analytics")) {
							hasNextPage = false;
						}
					} catch (JsonSyntaxException j) {
						logger.error("Error con el formato del Json: " + j.getMessage());
					} catch (Exception e) {
						logger.error("Error general: " + e.getMessage());
					}

				}
			}
		}

		return productList;
	}

	@Override
	public Supermarket getSupermarket() {
		return Supermarkets.DIA;
	}

	@Override
	public Set<Product> getProductList(JsonObject response, Category category) {
		Set<Product> productList = new HashSet<>();

		if (response.has("page_product_analytics")) {
			JsonObject pageProductAnalytics = response.getAsJsonObject("page_product_analytics");

			for (String productId : pageProductAnalytics.keySet()) {
				JsonObject productObj = pageProductAnalytics.getAsJsonObject(productId);

				String item_name = productObj.get("item_name").getAsString();
				String name = item_name.toUpperCase().charAt(0) + item_name.substring(1);
				String item_id = productObj.get("item_id").getAsString();
                String prefixId = "DI-";
                String id = prefixId + item_id;
                String imageUri = "https://www.dia.es/product_images/%s/%s_ISO_0_ES.jpg?imwidth=392";
                String image = String.format(imageUri, id, id);

				boolean availability = productObj.get("stock_availability").getAsBoolean();
				boolean onSale = false;

				Supermarket supermarket = getSupermarket();
				double price = productObj.get("price").getAsDouble();
				double salePrice = 0;

				if (productObj.has("discount")) {
					salePrice = productObj.get("price").getAsDouble();
					double discount = productObj.get("discount").getAsDouble();
					price += discount;
					onSale = true;

				}

				Product product = new Product(id, category, supermarket, name, image, availability, onSale);
				PriceHistoryId pricehistoryId = new PriceHistoryId(id, LocalDate.now());
				PriceHistory pricehistory = new PriceHistory(pricehistoryId, product, new BigDecimal(price),
						new BigDecimal(salePrice), new BigDecimal(0));
				if (isInsertable(product, productList)) {
					if (isInsertable(pricehistory, priceHistorySet)) {
						product.getPriceHistories().add(pricehistory);
					}

					productList.add(product);
				}
			}
		}
		return productList;
	}

	@SuppressWarnings("unchecked")
	public <T> boolean isInsertable(T object, Set<T> set) {
		if (object instanceof PriceHistory) {
			PriceHistory p = (PriceHistory) object;
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
        return "https://www.dia.es/api/v1/plp-insight/initial_analytics/%s"
                + "?filters=categories%%3A%s" + "%%3Aes&locale=es&navigation=%s&page=%d";
	}

	@Override
	public Hashtable<Category, String[]> getDictCategories() {
		Hashtable<Category, String[]> dicCategories = new Hashtable<>();

		dicCategories.put(Categories.ACEITE_ESPECIAS_Y_SALSAS,
				new String[] { "L2046", "L2047", "L2048", "L2050", "L2208" });

		dicCategories.put(Categories.AGUA_Y_REFRESCOS, new String[] { "L2107", "L2108", "L2109", "L2110", "L2111",
				"L2112", "L2114", "L2192", "L2212", "L2217" });

		dicCategories.put(Categories.APERITIVOS, new String[] { "L2096", "L2097", "L2098" });

		dicCategories.put(Categories.ARROZ_LEGUMBRES_Y_PASTA,
				new String[] { "L2042", "L2043", "L2044", "L2178", "L2191", "L2193" });

		dicCategories.put(Categories.AZUCAR_CARAMELOS_Y_CHOCOLATE,
				new String[] { "L2060", "L2061", "L2062", "L2063", "L2064", "L2228" });

		dicCategories.put(Categories.BEBE, new String[] { "L2138", "L2139", "L2140", "L2141", "L2142", "L2143" });

		dicCategories.put(Categories.BODEGA, new String[] { "L2115", "L2117", "L2118", "L2119", "L2120", "L2121",
				"L2122", "L2124", "L2125", "L2126", "L2127", "L2128", "L2129", "L2182" });

		dicCategories.put(Categories.CACAO_CAFE_E_INFUSIONES, new String[] { "L2057", "L2058", "L2059" });

		dicCategories.put(Categories.CARNE, new String[] { "L2013", "L2014", "L2015", "L2016", "L2017", "L2202" });

		dicCategories.put(Categories.CEREALES_Y_GALLETAS, new String[] { "L2065", "L2066", "L2067", "L2068", "L2216" });

		dicCategories.put(Categories.CHARCUTERIA_Y_QUESOS, new String[] { "L2001", "L2004", "L2005", "L2007", "L2008",
				"L2009", "L2010", "L2011", "L2012", "L2206" });

		dicCategories.put(Categories.CONGELADOS,
				new String[] { "L2130", "L2131", "L2132", "L2133", "L2135", "L2136", "L2137", "L2210", "L2213" });

		dicCategories.put(Categories.CONSERVAS_CALDOS_Y_CREMAS,
				new String[] { "L2092", "L2093", "L2094", "L2179", "L2180", "L2195", "L2197", "L2207" });

		dicCategories.put(Categories.CUIDADO_DEL_CABELLO, new String[] { "L2144", "L2145", "L2146", "L2147" });

		dicCategories.put(Categories.CUIDADO_FACIAL_Y_CORPORAL, new String[] { "L2148", "L2150", "L2151", "L2153",
				"L2154", "L2155", "L2156", "L2158", "L2186", "L2188", "L2211" });

		dicCategories.put(Categories.FITOTERAPIA_Y_PARAFARMACIA, new String[] { "L2183", "L2184", "L2227" });

		dicCategories.put(Categories.FRUTA_Y_VERDURA,
				new String[] { "L2022", "L2023", "L2024", "L2027", "L2028", "L2029", "L2030", "L2031", "L2181", "L2032",
						"L2033", "L2034", "L2035", "L2037", "L2038", "L2039", "L2040", "L2041", "L2196" });

		dicCategories.put(Categories.HUEVOS_LECHE_Y_MANTEQUILLA,
				new String[] { "L2051", "L2052", "L2053", "L2054", "L2055", "L2056" });

		dicCategories.put(Categories.LIMPIEZA_Y_HOGAR, new String[] { "L2159", "L2160", "L2161", "L2162", "L2163",
				"L2164", "L2166", "L2167", "L2168", "L2169", "L2170", "L2171", "L2173", "L2209", "L2226" });

		dicCategories.put(Categories.MARISCO_Y_PESCADO, new String[] { "L2019", "L2020", "L2021", "L2194" });

		dicCategories.put(Categories.MASCOTAS, new String[] { "L2174", "L2175", "L2176" });

		dicCategories.put(Categories.PANADERIA_Y_PASTELERIA,
				new String[] { "L2069", "L2070", "L2071", "L2072", "L2075", "L2076", "L2077" });

		dicCategories.put(Categories.PIZZAS_Y_PLATOS_PREPARADOS,
				new String[] { "L2101", "L2102", "L2103", "L2105", "L2106" });

		dicCategories.put(Categories.POSTRES_Y_YOGURES, new String[] { "L2078", "L2079", "L2080", "L2081", "L2082",
				"L2083", "L2084", "L2085", "L2086", "L2087", "L2088", "L2089" });

		dicCategories.put(Categories.ZUMOS, new String[] { "L2113" });

		return dicCategories;
	}

}