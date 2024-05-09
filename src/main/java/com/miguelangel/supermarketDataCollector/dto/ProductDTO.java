package com.miguelangel.supermarketDataCollector.dto;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;

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


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSupermarketId() {
        return supermarketId;
    }

    public void setSupermarketId(Integer supermarketId) {
        this.supermarketId = supermarketId;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getAvailable() {
        return this.available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getOnSale() {
        return this.onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public Set<PriceHistoryDTO> getPriceHistories() {
        return this.priceHistories;
    }

    public void setPriceHistories(Set<PriceHistoryDTO> priceHistories) {
        this.priceHistories = priceHistories;
    }

    public Set<Integer> getUsersIdFavourite() {
        return usersIdFavourite;
    }

    public void setUsersIdFavourite(Set<Integer> usersIdFavourite) {
        this.usersIdFavourite = usersIdFavourite;
    }

    public Long getTimesProductInShoppingList() {
        return timesProductInShoppingList;
    }

    public void setTimesProductInShoppingList(Long timesProductInShoppingList) {
        this.timesProductInShoppingList = timesProductInShoppingList;
    }

    public Double getPriceVariation() {
        return priceVariation;
    }

    public void setPriceVariation(Double priceVariation) {
        this.priceVariation = priceVariation;
    }
}

