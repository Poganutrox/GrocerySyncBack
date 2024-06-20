package com.miguelangel.supermarketDataCollector.dto;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;

/**
 * Data Transfer Object (DTO) for transferring product information.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
public class ProductDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer categoryId;
    private Integer supermarketId;
    private String name;
    private String image;
    private Boolean available;
    private Boolean onSale;
    private Double priceVariation;
    private Long timesProductInShoppingList;
    private Set<PriceHistoryDTO> priceHistories = new HashSet<>(0);
    private Set<Integer> usersIdFavourite = new HashSet<>(0);

    /**
     * Constructs a ProductDTO from a Product entity.
     *
     * @param product the Product entity
     */
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.available = product.getAvailable();
        this.onSale = product.getOnSale();
        this.categoryId = product.getCategory().getId();
        this.supermarketId = product.getSupermarket().getId();
        this.priceHistories = product.getPriceHistories().stream().map(PriceHistoryDTO::new).collect(Collectors.toSet());
        this.usersIdFavourite = product.getFavouriteUserEntities().stream().map(UserEntity::getId).collect(Collectors.toSet());
    }


    /**
     * Retrieves the unique identifier of the product.
     *
     * @return the product identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param id the product identifier to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return the product name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the category identifier of the product.
     *
     * @return the category identifier
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category identifier of the product.
     *
     * @param categoryId the category identifier to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Retrieves the supermarket identifier of the product.
     *
     * @return the supermarket identifier
     */
    public Integer getSupermarketId() {
        return supermarketId;
    }

    /**
     * Sets the supermarket identifier of the product.
     *
     * @param supermarketId the supermarket identifier to set
     */
    public void setSupermarketId(Integer supermarketId) {
        this.supermarketId = supermarketId;
    }

    /**
     * Retrieves the image URL of the product.
     *
     * @return the image URL
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Sets the image URL of the product.
     *
     * @param image the image URL to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Retrieves the availability status of the product.
     *
     * @return true if the product is available, false otherwise
     */
    public Boolean getAvailable() {
        return this.available;
    }

    /**
     * Sets the availability status of the product.
     *
     * @param available the availability status to set
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * Retrieves the sale status of the product.
     *
     * @return true if the product is on sale, false otherwise
     */
    public Boolean getOnSale() {
        return this.onSale;
    }

    /**
     * Sets the sale status of the product.
     *
     * @param onSale the sale status to set
     */
    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    /**
     * Retrieves the price histories associated with the product.
     *
     * @return a set of price history DTOs
     */
    public Set<PriceHistoryDTO> getPriceHistories() {
        return this.priceHistories;
    }

    /**
     * Sets the price histories associated with the product.
     *
     * @param priceHistories the price histories to set
     */
    public void setPriceHistories(Set<PriceHistoryDTO> priceHistories) {
        this.priceHistories = priceHistories;
    }

    /**
     * Retrieves the user identifiers of users who have favorited the product.
     *
     * @return a set of user identifiers
     */
    public Set<Integer> getUsersIdFavourite() {
        return usersIdFavourite;
    }

    /**
     * Sets the user identifiers of users who have favorited the product.
     *
     * @param usersIdFavourite the user identifiers to set
     */
    public void setUsersIdFavourite(Set<Integer> usersIdFavourite) {
        this.usersIdFavourite = usersIdFavourite;
    }

    /**
     * Retrieves the number of times the product has been added to shopping lists.
     *
     * @return the number of times the product has been added
     */
    public Long getTimesProductInShoppingList() {
        return timesProductInShoppingList;
    }

    /**
     * Sets the number of times the product has been added to shopping lists.
     *
     * @param timesProductInShoppingList the number of times the product has been added
     */
    public void setTimesProductInShoppingList(Long timesProductInShoppingList) {
        this.timesProductInShoppingList = timesProductInShoppingList;
    }

    /**
     * Retrieves the price variation of the product.
     *
     * @return the price variation
     */
    public Double getPriceVariation() {
        return priceVariation;
    }

    /**
     * Sets the price variation of the product.
     *
     * @param priceVariation the price variation to set
     */
    public void setPriceVariation(Double priceVariation) {
        this.priceVariation = priceVariation;
    }

}

