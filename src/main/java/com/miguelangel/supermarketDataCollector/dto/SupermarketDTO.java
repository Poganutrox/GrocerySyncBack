package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.Supermarket;

import java.io.Serial;

/**
 * A Data Transfer Object (DTO) representing a supermarket.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class SupermarketDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String url;

    /**
     * Default constructor for SupermarketDTO.
     */
    public SupermarketDTO() {
    }

    /**
     * Constructor for SupermarketDTO with a Supermarket entity parameter.
     *
     * @param supermarket The Supermarket entity from which to create the DTO
     */
    public SupermarketDTO(Supermarket supermarket) {
        this.id = supermarket.getId();
        this.name = supermarket.getName();
        this.url = supermarket.getUrl();
    }

    /**
     * Gets the ID of the supermarket.
     *
     * @return The ID of the supermarket
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the supermarket.
     *
     * @param id The ID of the supermarket to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the supermarket.
     *
     * @return The name of the supermarket
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the supermarket.
     *
     * @param name The name of the supermarket to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the URL of the supermarket.
     *
     * @return The URL of the supermarket
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the supermarket.
     *
     * @param url The URL of the supermarket to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
