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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

/**
 * MercadonaProvider is a service that implements the IProvider interface.
 * This service is responsible for collecting product data from the Mercadona supermarket.
 * It fetches product information from Mercadona's online store and organizes it into entities.
 *
 * @since 2024
 * @author Miguel Angel Moreno Garcia
 */
@Service
@Order(OrderProvider.MERCADONA)
public class MercadonaProvider implements IProvider {
    private final Logger logger = LoggerFactory.getLogger(MercadonaProvider.class);
    private final Set<PriceHistory> priceHistorySet = new HashSet<>(0);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Product> collectProducts() {
        logger.info("Starting... {}", getSupermarket().getName());
        JsonObject responseJsonObj = null;
        Set<Product> productList = new HashSet<>();

        try {
            for (Category category : getDictCategories().keySet()) {
                for (String parameter : getDictCategories().get(category)) {
                    String uriParameter = URLEncoder.encode(parameter, StandardCharsets.UTF_8);
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI(String.format(getMarketUri(), uriParameter))).timeout(Duration.ofSeconds(10))
                            .GET().build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    String responseBody = response.body();
                    try {
                        responseJsonObj = new Gson().fromJson(responseBody, JsonObject.class);
                    } catch (JsonSyntaxException e) {
                        logger.error(String.format(getMarketUri(), uriParameter));
                    }

                    assert responseJsonObj != null;
                    productList.addAll(getProductList(responseJsonObj, category));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }

    @Override
    public Set<Product> getProductList(JsonObject response, Category categoryProduct) {

        Set<Product> productList = new HashSet<>();

        JsonArray responseJsonList = null;

        final JsonElement responseJsonListElement = response.get("categories");
        if (responseJsonListElement != null && !responseJsonListElement.isJsonNull()) {
            responseJsonList = responseJsonListElement.getAsJsonArray();
            for (JsonElement category : responseJsonList) {
                final JsonObject categoryJson = category.getAsJsonObject();
                final JsonElement productsJsonListElement = categoryJson.get("products");
                if (productsJsonListElement != null && !productsJsonListElement.isJsonNull()) {
                    JsonArray productsJsonList = productsJsonListElement.getAsJsonArray();
                    for (JsonElement product : productsJsonList) {
                        final JsonObject productJson = product.getAsJsonObject();

                        final String item_id = productJson.get("id").getAsString().trim();
                        String prefixId = "ME-";
                        final String id = prefixId + item_id;
                        final String item_name = productJson.get("display_name").getAsString().trim();
                        final String image = productJson.get("thumbnail").getAsString().trim();
                        final boolean availability = productJson.get("published").getAsBoolean();
                        final Supermarket supermarket = getSupermarket();

                        final JsonObject priceObj = productJson.get("price_instructions").getAsJsonObject();
                        double price = priceObj.get("unit_price").getAsDouble();
                        final double bulkPrice = priceObj.get("bulk_price").getAsDouble();
                        boolean onSale = false;

                        final JsonElement previousPriceElement = priceObj.get("previous_unit_price");
                        double salePrice = 0.0;

                        if (previousPriceElement != null && !previousPriceElement.isJsonNull()) {
                            salePrice = price;
                            price = previousPriceElement.getAsFloat();
                            onSale = true;

                        }

                        String unit = "";
                        if (priceObj.get("reference_format") != null) {
                            unit = priceObj.get("reference_format").getAsString().trim();

                        }

                        final JsonElement packagingElement = productJson.get("packaging");
                        String packaging = "";
                        if (packagingElement != null && !packagingElement.isJsonNull()) {
                            packaging = packagingElement.getAsString().trim();
                            ;
                        }

                        String size = packaging + "  " + unit;
                        String name = item_name + " " + size;

                        Product newProduct = new Product(id, categoryProduct, supermarket, name, image, availability,
                                onSale);
                        PriceHistoryId pricehistoryId = new PriceHistoryId(id, LocalDate.now());
                        PriceHistory pricehistory = new PriceHistory(pricehistoryId, newProduct, new BigDecimal(price),
                                new BigDecimal(salePrice), new BigDecimal(bulkPrice));
                        if (isInsertable(newProduct, productList)) {
                            if (isInsertable(pricehistory, priceHistorySet)) {
                                newProduct.getPriceHistories().add(pricehistory);
                            }
                            productList.add(newProduct);
                        }
                    }

                } else {
                    logger.error("Error recuperando productos Mercadona");
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
        return "https://tienda.mercadona.es/api/categories/%s";
    }

    @Override
    public Supermarket getSupermarket() {
        return Supermarkets.MERCADONA;
    }


    @Override
    public Hashtable<Category, String[]> getDictCategories() {
        Hashtable<Category, String[]> dicCategories = new Hashtable<>();

        dicCategories.put(Categories.ACEITE_ESPECIAS_Y_SALSAS, new String[]{"112", "115", "116", "117"});
        dicCategories.put(Categories.AGUA_Y_REFRESCOS, new String[]{"156", "163", "158", "159", "161", "162"});
        dicCategories.put(Categories.APERITIVOS, new String[]{"135", "133", "132"});
        dicCategories.put(Categories.ARROZ_LEGUMBRES_Y_PASTA, new String[]{"118", "121", "120"});
        dicCategories.put(Categories.AZUCAR_CARAMELOS_Y_CHOCOLATE, new String[]{"89", "95", "92", "97", "90"});
        dicCategories.put(Categories.BEBE, new String[]{"216", "219", "218", "217"});
        dicCategories.put(Categories.BODEGA,
                new String[]{"164", "166", "181", "174", "168", "170", "173", "171", "169"});
        dicCategories.put(Categories.CACAO_CAFE_E_INFUSIONES, new String[]{"86", "81", "83", "84", "88"});
        dicCategories.put(Categories.CARNE, new String[]{"46", "38", "47", "37", "42", "43", "44", "40", "45"});
        dicCategories.put(Categories.CEREALES_Y_GALLETAS, new String[]{"78", "80", "79"});
        dicCategories.put(Categories.CHARCUTERIA_Y_QUESOS,
                new String[]{"48", "52", "49", "51", "50", "58", "54", "56", "53"});
        dicCategories.put(Categories.CONGELADOS,
                new String[]{"147", "148", "154", "155", "150", "149", "151", "884", "152", "145"});
        dicCategories.put(Categories.CONSERVAS_CALDOS_Y_CREMAS,
                new String[]{"122", "123", "127", "130", "129", "126"});
        dicCategories.put(Categories.CUIDADO_DEL_CABELLO, new String[]{"201", "199", "203", "202"});
        dicCategories.put(Categories.CUIDADO_FACIAL_Y_CORPORAL,
                new String[]{"192", "189", "185", "191", "188", "187", "186", "190", "194", "196", "198"});
        dicCategories.put(Categories.FITOTERAPIA_Y_PARAFARMACIA, new String[]{"213", "214"});
        dicCategories.put(Categories.FRUTA_Y_VERDURA, new String[]{"27", "28", "29"});
        dicCategories.put(Categories.HUEVOS_LECHE_Y_MANTEQUILLA, new String[]{"77", "72", "75"});
        dicCategories.put(Categories.LIMPIEZA_Y_HOGAR, new String[]{"226", "237", "241", "234", "235", "233", "231",
                "230", "232", "229", "243", "238", "239", "244"});
        dicCategories.put(Categories.MAQUILLAJE, new String[]{"206", "207", "208", "210", "212"});
        dicCategories.put(Categories.MARISCO_Y_PESCADO, new String[]{"32", "34", "31", "36", "789"});
        dicCategories.put(Categories.MASCOTAS, new String[]{"222", "221", "225"});
        dicCategories.put(Categories.PANADERIA_Y_PASTELERIA,
                new String[]{"65", "66", "69", "59", "60", "62", "64", "68", "71"});
        dicCategories.put(Categories.PIZZAS_Y_PLATOS_PREPARADOS, new String[]{"897", "138", "140", "142"});
        dicCategories.put(Categories.POSTRES_Y_YOGURES,
                new String[]{"105", "110", "111", "106", "103", "109", "108", "104", "107"});
        dicCategories.put(Categories.ZUMOS, new String[]{"99", "100", "143", "98"});

        return dicCategories;
    }
}