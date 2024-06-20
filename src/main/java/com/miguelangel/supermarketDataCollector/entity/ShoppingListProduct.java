package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Represents a product within a shopping list, including its quantity and the date it was added.
 * This class is mapped to the "shoppinglist_product" table in the database.
 */
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

    /**
     * Sets the added date to the current system date before persisting the entity.
     */
    @PrePersist
    private void addAddedDate() {
        addedAt = LocalDate.now();
    }

    /**
     * Default constructor.
     */
    public ShoppingListProduct() {
    }

    /**
     * Constructs a ShoppingListProduct with the specified id, product, shopping list, and quantity.
     *
     * @param id           the composite key of the shopping list product
     * @param product      the product associated with the shopping list product
     * @param shoppingList the shopping list associated with the shopping list product
     * @param quantity     the quantity of the product in the shopping list
     */
    public ShoppingListProduct(ShoppingListProductId id, Product product, ShoppingList shoppingList, Integer quantity) {
        this.id = id;
        this.product = product;
        this.shoppingList = shoppingList;
        this.quantity = quantity;
    }

    /**
     * Returns the composite key of the shopping list product.
     *
     * @return the composite key of the shopping list product
     */
    public ShoppingListProductId getId() {
        return this.id;
    }

    /**
     * Sets the composite key of the shopping list product.
     *
     * @param id the composite key of the shopping list product
     */
    public void setId(ShoppingListProductId id) {
        this.id = id;
    }

    /**
     * Returns the product associated with the shopping list product.
     *
     * @return the product associated with the shopping list product
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Sets the product associated with the shopping list product.
     *
     * @param product the product associated with the shopping list product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Returns the shopping list associated with the shopping list product.
     *
     * @return the shopping list associated with the shopping list product
     */
    public ShoppingList getShoppingList() {
        return this.shoppingList;
    }

    /**
     * Sets the shopping list associated with the shopping list product.
     *
     * @param shoppingList the shopping list associated with the shopping list product
     */
    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    /**
     * Returns the quantity of the product in the shopping list.
     *
     * @return the quantity of the product in the shopping list
     */
    public Integer getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the quantity of the product in the shopping list.
     *
     * @param quantity the quantity of the product in the shopping list
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the date the product was added to the shopping list.
     *
     * @return the date the product was added to the shopping list
     */
    public LocalDate getAddedAt() {
        return addedAt;
    }

    /**
     * Sets the date the product was added to the shopping list.
     *
     * @param addedAt the date the product was added to the shopping list
     */
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
