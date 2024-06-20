package com.miguelangel.supermarketDataCollector.dto;

import com.miguelangel.supermarketDataCollector.entity.ShoppingList;

import java.io.Serial;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) for representing shopping lists.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
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

    /**
     * Default constructor.
     */
    public ShoppingListDTO() {
    }

    /**
     * Constructs a ShoppingListDTO object from a ShoppingList entity.
     *
     * @param shoppinglist The ShoppingList entity to construct the DTO from
     */
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


    /**
     * Gets the ID of the shopping list.
     *
     * @return The ID of the shopping list
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the shopping list.
     *
     * @param id The ID of the shopping list to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the creator user of the shopping list.
     *
     * @return The creator user of the shopping list
     */
    public UserDTO getCreatorUser() {
        return creatorUser;
    }

    /**
     * Sets the creator user of the shopping list.
     *
     * @param creatorUser The creator user of the shopping list to set
     */
    public void setCreatorUser(UserDTO creatorUser) {
        this.creatorUser = creatorUser;
    }

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
     * Gets the creation date of the shopping list.
     *
     * @return The creation date of the shopping list
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the shopping list.
     *
     * @param creationDate The creation date of the shopping list to set
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the status of the shopping list.
     *
     * @return The status of the shopping list
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * Sets the status of the shopping list.
     *
     * @param status The status of the shopping list to set
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * Gets the users associated with the shopping list.
     *
     * @return The users associated with the shopping list
     */
    public Set<UserDTO> getUsers() {
        return users;
    }

    /**
     * Sets the users associated with the shopping list.
     *
     * @param users The users associated with the shopping list to set
     */
    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    /**
     * Gets the shopping list products.
     *
     * @return The shopping list products
     */
    public Set<ShoppingListProductDTO> getShoppinglistProducts() {
        return shoppinglistProducts;
    }

    /**
     * Sets the shopping list products.
     *
     * @param shoppinglistProducts The shopping list products to set
     */
    public void setShoppinglistProducts(Set<ShoppingListProductDTO> shoppinglistProducts) {
        this.shoppinglistProducts = shoppinglistProducts;
    }

    /**
     * Gets the unique share code of the shopping list.
     *
     * @return The unique share code of the shopping list
     */
    public String getUniqueShareCode() {
        return uniqueShareCode;
    }

    /**
     * Sets the unique share code of the shopping list.
     *
     * @param uniqueShareCode The unique share code of the shopping list to set
     */
    public void setUniqueShareCode(String uniqueShareCode) {
        this.uniqueShareCode = uniqueShareCode;
    }

}
