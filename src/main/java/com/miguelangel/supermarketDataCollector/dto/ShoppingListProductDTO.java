package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.ShoppingListProduct;

import java.io.Serial;

/**
 * A Data Transfer Object (DTO) representing a shopping list product.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class ShoppingListProductDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ProductDTO product;
    private ShoppingListDTO shoppinglist;
    private Integer quantity;

    /**
     * Default constructor.
     */
    public ShoppingListProductDTO() {
    }

    /**
     * Constructor with parameters.
     *
     * @param shoppinglistProduct The shopping list product entity
     */
    public ShoppingListProductDTO(ShoppingListProduct shoppinglistProduct) {
        this.product = new ProductDTO(shoppinglistProduct.getProduct());
        this.quantity = shoppinglistProduct.getQuantity();
    }

    /**
     * Gets the product associated with the shopping list product.
     *
     * @return The product associated with the shopping list product
     */
    public ProductDTO getProduct() {
        return product;
    }

    /**
     * Sets the product associated with the shopping list product.
     *
     * @param product The product associated with the shopping list product to set
     */
    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    /**
     * Gets the shopping list associated with the shopping list product.
     *
     * @return The shopping list associated with the shopping list product
     */
    public ShoppingListDTO getShoppinglist() {
        return shoppinglist;
    }

    /**
     * Sets the shopping list associated with the shopping list product.
     *
     * @param shoppinglist The shopping list associated with the shopping list product to set
     */
    public void setShoppinglist(ShoppingListDTO shoppinglist) {
        this.shoppinglist = shoppinglist;
    }

    /**
     * Gets the quantity of the shopping list product.
     *
     * @return The quantity of the shopping list product
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the shopping list product.
     *
     * @param quantity The quantity of the shopping list product to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
