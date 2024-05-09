package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;

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

    public ShoppingList() {
    }

    public ShoppingList(int id) {
        this.id = id;
    }

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

    public ShoppingList(UserEntity userEntity, String name, LocalDate creationDate, Boolean status, Set<UserEntity> userEntities,
                        Set<ShoppingListProduct> shoppingListProducts) {
        this.creatorUser = userEntity;
        this.name = name;
        this.creationDate = creationDate;
        this.status = status;
        this.userEntities = userEntities;
        this.shoppingListProducts = shoppingListProducts;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return this.creatorUser;
    }

    public void setUser(UserEntity userEntity) {
        this.creatorUser = userEntity;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<UserEntity> getUsers() {
        return this.userEntities;
    }

    public void setUsers(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public Set<ShoppingListProduct> getShoppingListProducts() {
        return this.shoppingListProducts;
    }

    public void setShoppingListProducts(Set<ShoppingListProduct> shoppingListProducts) {
        this.shoppingListProducts = shoppingListProducts;
    }

    public UserEntity getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserEntity creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getUniqueShareCode() {
        return uniqueShareCode;
    }

    public void setUniqueShareCode(String uniqueShareCode) {
        this.uniqueShareCode = uniqueShareCode;
    }

    public Set<UserEntity> getUserEntities() {
        return userEntities;
    }

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
