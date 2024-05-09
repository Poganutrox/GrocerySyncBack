package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NamedQueries({
        @NamedQuery(
                name = "User.timesProductAddedToShoppingList",
                query = "SELECT COUNT(e) FROM UserEntity e" +
                        " JOIN e.shoppingListsUsers s" +
                        " JOIN s.shoppingListProducts sp" +
                        " WHERE e.id = :userId" +
                        " AND :productId = sp.id.productId"
        )
})
@Entity
@Table(name = "_user")
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "last_connection")
    private LocalDate lastConnection;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creatorUser", cascade = CascadeType.ALL)
    private Set<ShoppingList> shoppingListsCreatorUser = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "shoppinglist_user", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "shopping_list_id", nullable = false, updatable = false, insertable = false)})
    private Set<ShoppingList> shoppingListsUsers = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_product_favourite", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false)})
    private Set<Product> favouriteProducts = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name= "user_id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", nullable = false)})
    private Set<Role> roles = new HashSet<>(0);

    @PrePersist
    private void encodePasswordAndAutoCreatedAt(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        setPassword(encoder.encode(password));
        setCreatedAt(LocalDate.now());
    }

    public UserEntity() {
    }

    public UserEntity(int id, String name, String lastName, String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public UserEntity(int id, String name, String lastName, String email, String phone, Set<ShoppingList> shoppingListsCreatorUser,
                      Set<ShoppingList> shoppingListsUsers, Set<Product> favouriteProducts) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.shoppingListsCreatorUser = shoppingListsCreatorUser;
        this.shoppingListsUsers = shoppingListsUsers;
        this.favouriteProducts = favouriteProducts;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ShoppingList> getShoppingListsCreatorUser() {
        return this.shoppingListsCreatorUser;
    }

    public void setShoppingListsCreatorUser(Set<ShoppingList> shoppingListsCreatorUser) {
        this.shoppingListsCreatorUser = shoppingListsCreatorUser;
    }

    public Set<ShoppingList> getShoppingListsUsers() {
        return this.shoppingListsUsers;
    }

    public void setShoppingListsUsers(Set<ShoppingList> shoppingListsUsers) {
        this.shoppingListsUsers = shoppingListsUsers;
    }

    public Set<Product> getFavouriteProducts() {
        return favouriteProducts;
    }

    public void setFavouriteProducts(Set<Product> favouriteProducts) {
        this.favouriteProducts = favouriteProducts;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(LocalDate lastConnection) {
        this.lastConnection = lastConnection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, id, lastName, name, phone, createdAt, lastConnection);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UserEntity other)) {
            return false;
        }
        return Objects.equals(email, other.email) && id == other.id && Objects.equals(lastName, other.lastName)
                && Objects.equals(name, other.name) && Objects.equals(phone, other.phone)
                && Objects.equals(lastConnection, other.lastConnection) && Objects.equals(createdAt, other.createdAt);
    }
}
