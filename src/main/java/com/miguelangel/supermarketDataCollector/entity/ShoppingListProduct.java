package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "shoppinglist_product")
public class ShoppingListProduct implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "shoppingListId", column = @Column(name = "shopping_list_id", nullable = false)),
            @AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false, length = 50))})
    private ShoppingListProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id", nullable = false, insertable = false, updatable = false)
    private ShoppingList shoppingList;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "added_at")
    private LocalDate addedAt;

    @PrePersist
    private void addAddedDate() {
        addedAt = LocalDate.now();
    }

    public ShoppingListProduct() {
    }

    public ShoppingListProduct(ShoppingListProductId id, Product product, ShoppingList shoppingList, Integer quantity) {
        this.id = id;
        this.product = product;
        this.shoppingList = shoppingList;
        this.quantity = quantity;
    }

    public ShoppingListProductId getId() {
        return this.id;
    }

    public void setId(ShoppingListProductId id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ShoppingList getShoppingList() {
        return this.shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDate addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, quantity, shoppingList, addedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShoppingListProduct other)) {
            return false;
        }
        return Objects.equals(id, other.id) && Objects.equals(product, other.product)
                && Objects.equals(quantity, other.quantity) && Objects.equals(shoppingList, other.shoppingList)
                && Objects.equals(addedAt, other.addedAt);
    }
}
