package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.Category;

/**
 * Data Transfer Object (DTO) for transferring category information between layers.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class CategoryDTO {
    private int id;
    private String name;

    /**
     * Constructs a CategoryDTO with the specified id and name.
     *
     * @param id   the id of the category
     * @param name the name of the category
     */
    public CategoryDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructs a CategoryDTO from a Category entity.
     *
     * @param category the Category entity
     */
    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    /**
     * Retrieves the id of the category.
     *
     * @return the id of the category
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the category.
     *
     * @param id the id of the category
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the category.
     *
     * @return the name of the category
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name the name of the category
     */
    public void setName(String name) {
        this.name = name;
    }
}
