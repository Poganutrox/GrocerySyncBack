package com.miguelangel.supermarketDataCollector.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Data Transfer Object (DTO) representing a response for creating or updating a shopping list.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class ShoppingListResponseDTO {

    private Integer id;
    private String name;
    private Integer creatorUserId;
    private boolean status;
    private Map<String, Integer> shoppingListProducts = new HashMap<>(0);
    private List<Integer> users = new ArrayList<>(0);

    /**
     * Gets the name of the shopping list.
     *
     * @return The name of the shopping list
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the shopping list.
     *
     * @param name The name of the shopping list to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of user IDs associated with the shopping list.
     *
     * @return The list of user IDs associated with the shopping list
     */
    public List<Integer> getUsers() {
        return users;
    }

    /**
     * Sets the list of user IDs associated with the shopping list.
     *
     * @param users The list of user IDs associated with the shopping list to set
     */
    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    /**
     * Gets the map of shopping list products, where the key is the product ID and the value is the quantity.
     *
     * @return The map of shopping list products
     */
    public Map<String, Integer> getShoppingListProducts() {
        return shoppingListProducts;
    }

    /**
     * Sets the map of shopping list products, where the key is the product ID and the value is the quantity.
     *
     * @param shoppingListProducts The map of shopping list products to set
     */
    public void setShoppingListProducts(Map<String, Integer> shoppingListProducts) {
        this.shoppingListProducts = shoppingListProducts;
    }

    /**
     * Checks if the shopping list is active or not.
     *
     * @return True if the shopping list is active, false otherwise
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Sets the status of the shopping list.
     *
     * @param status The status of the shopping list to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Gets the ID of the creator user associated with the shopping list.
     *
     * @return The ID of the creator user associated with the shopping list
     */
    public Integer getCreatorUserId() {
        return creatorUserId;
    }

    /**
     * Sets the ID of the creator user associated with the shopping list.
     *
     * @param creatorUserId The ID of the creator user associated with the shopping list to set
     */
    public void setCreatorUserId(Integer creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    /**
     * Gets the ID of the shopping list.
     *
     * @return The ID of the shopping list
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the shopping list.
     *
     * @param id The ID of the shopping list to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
