package com.miguelangel.supermarketDataCollector.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListResponseDTO {
    private Integer id;
    private String name;
    private Integer creatorUserId;
    private boolean status;
    private Map<String, Integer> shoppingListProducts = new HashMap<>(0);
    private List<Integer> users = new ArrayList<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public Map<String, Integer> getShoppingListProducts() {
        return shoppingListProducts;
    }

    public void setShoppingListProducts(Map<String, Integer> shoppingListProducts) {
        this.shoppingListProducts = shoppingListProducts;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Integer creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
