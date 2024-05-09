package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.Supermarket;

public class SupermarketDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String url;

    public SupermarketDTO() {
    }

    public SupermarketDTO(Supermarket supermarket) {
        this.id = supermarket.getId();
        this.name = supermarket.getName();
        this.url = supermarket.getUrl();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
