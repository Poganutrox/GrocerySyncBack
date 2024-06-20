package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;

/**
 * Represents a shopping list within the supermarket data collection system.
 * This class is mapped to the "shoppinglist" table in the database.
 * Each shopping list has a unique identifier, a creator user, a name, a creation date, a status,
 * a unique share code, and associations with users and products.
 *
 * @since 2024
 * @author Miguel Angel Moreno Garcia
 */
@Entity
@Table(name = "shoppinglist")
public class ShoppingList implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_user_id")
    private UserEntity creatorUser;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "creation_date", length = 29)
    private LocalDate creationDate;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "unique_share_code")
    private String uniqueShareCode;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "shoppinglist_user", joinColumns = {
            @JoinColumn(name = "shopping_list_id", nullable = false, updatable = false, insertable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)})
    private Set<UserEntity> userEntities = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shoppingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingListProduct> shoppingListProducts = new HashSet<>(0);

    @PrePersist
    private void createUniqueShareCode() {
        uniqueShareCode = UUID.randomUUID().toString();
    }

    /**
     * Default constructor.
     */
    public ShoppingList() {
    }

    /**
     * Constructs a ShoppingList with the specified id.
     *
     * @param id the id of the shopping list
     */
    public ShoppingList(int id) {
        this.id = id;
    }

    /**
     * Constructs a ShoppingList with the specified id, creator user, name, creation date, status, users, and products.
     *
     * @param id the id of the shopping list
     * @param creatorUser the creator user of the shopping list
     * @param name the name of the shopping list
     * @param creationDate the creation date of the shopping list
     * @param status the status of the shopping list
     * @param userEntities the users associated with the shopping list
     * @param shoppingListProducts the products associated with the shopping list
     */
    public ShoppingList(int id, UserEntity creatorUser, String name, LocalDate creationDate, Boolean status, Set<UserEntity> userEntities,
                        Set<ShoppingListProduct> shoppingListProducts) {
        this.id = id;
        this.creatorUser = creatorUser;
        this.name = name;
        this.creationDate = creationDate;
        this.status = status;
        this.userEntities = userEntities;
        this.shoppingListProducts = shoppingListProducts;
    }

    /**
     * Constructs a ShoppingList with the specified creator user, name, creation date, status, users, and products.
     *
     * @param userEntity the creator user of the shopping list
     * @param name the name of the shopping list
     * @param creationDate the creation date of the shopping list
     * @param status the status of the shopping list
     * @param userEntities the users associated with the shopping list
     * @param shoppingListProducts the products associated with the shopping list
     */
    public ShoppingList(UserEntity userEntity, String name, LocalDate creationDate, Boolean status, Set<UserEntity> userEntities,
                        Set<ShoppingListProduct> shoppingListProducts) {
        this.creatorUser = userEntity;
        this.name = name;
        this.creationDate = creationDate;
        this.status = status;
        this.userEntities = userEntities;
        this.shoppingListProducts = shoppingListProducts;
    }

    /**
     * Returns the id of the shopping list.
     *
     * @return the id of the shopping list
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id of the shopping list.
     *
     * @param id the id of the shopping list
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the creator user of the shopping list.
     *
     * @return the creator user of the shopping list
     */
    public UserEntity getUser() {
        return this.creatorUser;
    }

    /**
     * Sets the creator user of the shopping list.
     *
     * @param userEntity the creator user of the shopping list
     */
    public void setUser(UserEntity userEntity) {
        this.creatorUser = userEntity;
    }

    /**
     * Returns the name of the shopping list.
     *
     * @return the name of the shopping list
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the shopping list.
     *
     * @param name the name of the shopping list
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the creation date of the shopping list.
     *
     * @return the creation date of the shopping list
     */
    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    /**
     * Sets the creation date of the shopping list.
     *
     * @param creationDate the creation date of the shopping list
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns the status of the shopping list.
     *
     * @return the status of the shopping list
     */
    public Boolean getStatus() {
        return this.status;
    }

    /**
     * Sets the status of the shopping list.
     *
     * @param status the status of the shopping list
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * Returns the users associated with the shopping list.
     *
     * @return the users associated with the shopping list
     */
    public Set<UserEntity> getUsers() {
        return this.userEntities;
    }

    /**
     * Sets the users associated with the shopping list.
     *
     * @param userEntities the users associated with the shopping list
     */
    public void setUsers(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    /**
     * Returns the products associated with the shopping list.
     *
     * @return the products associated with the shopping list
     */
    public Set<ShoppingListProduct> getShoppingListProducts() {
        return this.shoppingListProducts;
    }

    /**
     * Sets the products associated with the shopping list.
     *
     * @param shoppingListProducts the products associated with the shopping list
     */
    public void setShoppingListProducts(Set<ShoppingListProduct> shoppingListProducts) {
        this.shoppingListProducts = shoppingListProducts;
    }

    /**
     * Returns the creator user of the shopping list.
     *
     * @return the creator user of the shopping list
     */
    public UserEntity getCreatorUser() {
        return creatorUser;
    }

    /**
     * Sets the creator user of the shopping list.
     *
     * @param creatorUser the creator user of the shopping list
     */
    public void setCreatorUser(UserEntity creatorUser) {
        this.creatorUser = creatorUser;
    }

    /**
     * Returns the unique share code of the shopping list.
     *
     * @return the unique share code of the shopping list
     */
    public String getUniqueShareCode() {
        return uniqueShareCode;
    }

    /**
     * Sets the unique share code of the shopping list.
     *
     * @param uniqueShareCode the unique share code of the shopping list
     */
    public void setUniqueShareCode(String uniqueShareCode) {
        this.uniqueShareCode = uniqueShareCode;
    }

    /**
     * Returns the users associated with the shopping list.
     *
     * @return the users associated with the shopping list
     */
    public Set<UserEntity> getUserEntities() {
        return userEntities;
    }

    /**
     * Sets the users associated with the shopping list.
     *
     * @param userEntities the users associated with the shopping list
     */
    public void setUserEntities(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }


    @Override
    public int hashCode() {
        return Objects.hash(creationDate, creatorUser, id, name, status, uniqueShareCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShoppingList other)) {
            return false;
        }
        return Objects.equals(creationDate, other.creationDate) && Objects.equals(creatorUser, other.creatorUser)
                && id == other.id && Objects.equals(name, other.name)
                && Objects.equals(status, other.status) && Objects.equals(uniqueShareCode, other.uniqueShareCode);
    }

}
