package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.ShoppingListProduct;

import java.io.Serial;

public class ShoppingListProductDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ProductDTO product;
    private ShoppingListDTO shoppinglist;
    private Integer quantity;

    public ShoppingListProductDTO() {
    }

    public ShoppingListProductDTO(ShoppingListProduct shoppinglistProduct) {
        this.product = new ProductDTO(shoppinglistProduct.getProduct());
        this.quantity = shoppinglistProduct.getQuantity();
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public ShoppingListDTO getShoppinglist() {
        return shoppinglist;
    }

    public void setShoppinglist(ShoppingListDTO shoppinglist) {
        this.shoppinglist = shoppinglist;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
