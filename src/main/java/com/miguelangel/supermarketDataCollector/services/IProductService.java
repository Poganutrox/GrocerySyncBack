package com.miguelangel.supermarketDataCollector.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.miguelangel.supermarketDataCollector.dto.ProductDTO;
import com.miguelangel.supermarketDataCollector.entity.Product;
import org.springframework.data.domain.Page;

/**
 * Interface defining operations available for managing products.
 * <p>
 * Author: Miguel Ángel Moreno García
 */
public interface IProductService {

    /**
     * Finds products based on specified filters.
     *
     * @param filters A map of filters to apply.
     * @return A list of products matching the filters.
     */
    Page<ProductDTO> findBy(Map<String, Object> filters, int page, int size);

    /**
     * Finds a product by its unique identifier.
     *
     * @param id The unique identifier of the product.
     * @return An Optional that may contain the found product, or empty if not found.
     */
    Optional<Product> findById(String id);

    /**
     * Finds products by their unique identifiers.
     *
     * @param ids List of product unique identifiers.
     * @return List of found products.
     */
    List<Product> findByIds(List<String> ids);

    /**
     * Saves a product in the system.
     *
     * @param product The product to save.
     * @return true if the product was saved successfully, false otherwise.
     */
    boolean save(Product product);

    /**
     * Saves a set of products in the system.
     *
     * @param productList The set of products to save.
     * @return true if all products were saved successfully, false otherwise.
     */
    boolean saveAllProducts(Set<Product> productList);

    boolean setProductFavourite(String productId, int userId);

    List<Product> getFavouriteProducts(int userId);

    Long getTimesProductInShoppingList(String productId, int userId);

    Double getPriceVariation(String productId, Integer userId);
}
