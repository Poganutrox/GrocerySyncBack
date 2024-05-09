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

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    IProductService productService;

    @GetMapping(value = "/{id}", produces = "application/json")
    private ResponseEntity<ProductDTO> findById(@PathVariable("id") String productId) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ProductDTO productDTO = new ProductDTO(product.get());
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

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
