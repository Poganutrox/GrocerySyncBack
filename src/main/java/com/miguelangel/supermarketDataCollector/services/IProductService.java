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
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
public interface IProductService {

    /**
     * Finds products based on specified filters.
     *
     * @param filters A map of filters to apply.
     * @param page    The page number of results.
     * @param size    The maximum number of results per page.
     * @return A page of ProductDTO objects matching the filters.
     */
    Page<ProductDTO> findBy(Map<String, Object> filters, int page, int size);

    /**
     * Finds a product by its unique identifier.
     *
     * @param id The unique identifier of the product.
     * @return An Optional containing the found product, or empty if not found.
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

    /**
     * Sets a product as a favorite for a specific user.
     *
     * @param productId The unique identifier of the product.
     * @param userId    The user ID who marks the product as favorite.
     * @return true if the operation was successful, false otherwise.
     */
    boolean setProductFavourite(String productId, int userId);

    /**
     * Retrieves the favorite products of a specific user.
     *
     * @param userId The user ID whose favorite products are to be retrieved.
     * @return List of favorite products for the specified user.
     */
    List<Product> getFavouriteProducts(int userId);

    /**
     * Retrieves the number of times a product appears in a user's shopping list.
     *
     * @param productId The unique identifier of the product.
     * @param userId    The user ID whose shopping list is being checked.
     * @return The number of times the product appears in the user's shopping list.
     */
    Long getTimesProductInShoppingList(String productId, int userId);

    /**
     * Retrieves the price variation of a product for a specific user.
     *
     * @param productId The unique identifier of the product.
     * @param userId    The user ID for whom the price variation is being calculated.
     * @return The price variation of the product for the specified user.
     */
    Double getPriceVariation(String productId, Integer userId);
}
