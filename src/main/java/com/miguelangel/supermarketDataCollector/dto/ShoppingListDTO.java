package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.ShoppingList;

import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ShoppingListDTO implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private UserDTO creatorUser;
    private String name;
    private String creationDate;
    private String uniqueShareCode;
    private Boolean status;
    private Set<UserDTO> users = new HashSet<>();
    private Set<ShoppingListProductDTO> shoppinglistProducts = new HashSet<>();

    public ShoppingListDTO() {
    }

    public ShoppingListDTO(ShoppingList shoppinglist) {
        this.id = shoppinglist.getId();
        this.creatorUser = new UserDTO(shoppinglist.getUser());
        this.name = shoppinglist.getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.creationDate = shoppinglist.getCreationDate().format(formatter);
        this.status = shoppinglist.getStatus();
        this.uniqueShareCode = shoppinglist.getUniqueShareCode();
        shoppinglist.getUsers().forEach(u ->{
            users.add(new UserDTO(u));
        });
        shoppinglist.getShoppingListProducts().forEach(s -> {
            shoppinglistProducts.add(new ShoppingListProductDTO(s));
        });
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserDTO getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserDTO creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public Set<ShoppingListProductDTO> getShoppinglistProducts() {
        return shoppinglistProducts;
    }

    public void setShoppinglistProducts(Set<ShoppingListProductDTO> shoppinglistProducts) {
        this.shoppinglistProducts = shoppinglistProducts;
    }

    public String getUniqueShareCode() {
        return uniqueShareCode;
    }

    public void setUniqueShareCode(String uniqueShareCode) {
        this.uniqueShareCode = uniqueShareCode;
    }
}
