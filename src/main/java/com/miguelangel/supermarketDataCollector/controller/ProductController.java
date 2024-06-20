package com.miguelangel.supermarketDataCollector.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.miguelangel.supermarketDataCollector.dto.ProductDTO;
import com.miguelangel.supermarketDataCollector.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.miguelangel.supermarketDataCollector.services.IProductService;

/**
 * Controller for handling product-related HTTP requests.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    IProductService productService;

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return a ResponseEntity containing the ProductDTO object and an HTTP status code
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    private ResponseEntity<ProductDTO> findById(@PathVariable("id") String productId) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ProductDTO productDTO = new ProductDTO(product.get());
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    /**
     * Retrieves products based on specified filters.
     *
     * @param productId       the ID of the product
     * @param productName     the name of the product
     * @param categoryId      the ID of the category
     * @param supermarketIds  the IDs of the supermarkets
     * @param onSale          whether the product is on sale
     * @param priceSort       sorting order for price
     * @param alphabeticSort  sorting order for name
     * @param page            page number
     * @param size            page size
     * @return a Page containing ProductDTO objects
     */
    @GetMapping(value = "/findBy", produces = "application/json")
    private Page<ProductDTO> getProductsBy(
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Set<Integer> supermarketIds,
            @RequestParam(required = false) Boolean onSale,
            @RequestParam(required = false) Integer priceSort,
            @RequestParam(required = false) Integer alphabeticSort,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        Map<String, Object> filters = new HashMap<>();
        if (productName != null) productName = productName.toLowerCase();
        if (productId != null) productId = productId.toUpperCase();
        if (onSale) {
            filters.put("onSale", true);
        }

        filters.put("productId", productId);
        filters.put("productName", productName);
        filters.put("categoryId", categoryId);
        filters.put("supermarketIds", supermarketIds);
        filters.put("priceSort", priceSort);
        filters.put("alphabeticSort", alphabeticSort);

        return productService.findBy(filters, page, size);
    }

    /**
     * Set a product as favourite for a specific user.
     *
     * @param userId the ID of the user
     * @param productId the ID of the product
     * @return a ResponseEntity containing a boolean and an HTTP status code
     */
    @GetMapping("/favourite")
    private ResponseEntity<Boolean> setProductFavourites(
            @RequestParam(value = "productId") String productId,
            @RequestParam(value = "userId") int userId) {

        boolean success = productService.setProductFavourite(productId, userId);
        if (success) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the favourite products of a user.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing a Set of ProductDTO objects and an HTTP status code
     */
    @GetMapping("/user/favourite")
    private ResponseEntity<Set<ProductDTO>> getProductFavourites(
            @RequestParam(value = "userId") int userId) {
        try {
            Set<ProductDTO> favouriteProducts = productService.getFavouriteProducts(userId).stream()
                    .map(ProductDTO::new)
                    .sorted(Comparator.comparing(ProductDTO::getName))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            return new ResponseEntity<>(favouriteProducts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the number of times a product has been added to shopping lists by a user.
     *
     * @param productId the ID of the product
     * @param userId    the ID of the user
     * @return a ResponseEntity containing the number of times the product has been added and an HTTP status code
     */
    @GetMapping("/added")
    private ResponseEntity<Long> getTimesProductAdded(
            @RequestParam String productId,
            @RequestParam Integer userId
    ) {
        try {
            long repeatedTimes = productService.getTimesProductInShoppingList(productId, userId);
            return new ResponseEntity<>(repeatedTimes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(-1L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the price variation of a product for a specific user.
     *
     * @param productId the ID of the product
     * @param userId    the ID of the user
     * @return a ResponseEntity containing the price variation of the product and an HTTP status code
     */
    @GetMapping("/variation")
    private ResponseEntity<Double> getPriceVariation(
            @RequestParam String productId,
            @RequestParam Integer userId
    ) {
        try {
            Double priceVariation = productService.getPriceVariation(productId, userId);
            return new ResponseEntity<>(priceVariation, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
