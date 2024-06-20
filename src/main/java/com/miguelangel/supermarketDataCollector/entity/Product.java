package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

/**
 * Represents a product in the supermarket data collection system.
 * This class is mapped to the "product" table in the database.
 * A product belongs to a category and a supermarket, and it can have multiple price histories and shopping list associations.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
@Entity
@Table(name = "product")
public class Product implements java.io.Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 50)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supermarket_id")
	private Supermarket supermarket;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private String image;

	@Column(name = "available")
	private Boolean available;

	@Column(name = "onsale")
	private Boolean onSale;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
	private Set<PriceHistory> priceHistories = new HashSet<>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
	private Set<ShoppingListProduct> shoppingListProducts = new HashSet<>(0);

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_product_favourite", joinColumns = {
			@JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false)}, inverseJoinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false, insertable = false)})
	private Set<UserEntity> favouriteUserEntities = new HashSet<>(0);

	/**
	 * Default constructor.
	 */
	public Product() {
	}

	/**
	 * Constructs a Product with the specified ID.
	 *
	 * @param id the ID of the product
	 */
	public Product(String id) {
		this.id = id;
	}

	/**
	 * Constructs a Product with the specified attributes.
	 *
	 * @param id          the ID of the product
	 * @param category    the category of the product
	 * @param supermarket the supermarket where the product is sold
	 * @param name        the name of the product
	 * @param image       the image URL of the product
	 * @param available   the availability status of the product
	 * @param onSale      the sale status of the product
	 */
	public Product(String id, Category category, Supermarket supermarket, String name, String image, Boolean available,
				   Boolean onSale) {
		this.id = id;
		this.category = category;
		this.supermarket = supermarket;
		this.name = name;
		this.image = image;
		this.available = available;
		this.onSale = onSale;
	}

	/**
	 * Returns the ID of the product.
	 *
	 * @return the ID of the product
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the ID of the product.
	 *
	 * @param id the ID of the product
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the category of the product.
	 *
	 * @return the category of the product
	 */
	public Category getCategory() {
		return this.category;
	}

	/**
	 * Sets the category of the product.
	 *
	 * @param category the category of the product
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * Returns the supermarket where the product is sold.
	 *
	 * @return the supermarket where the product is sold
	 */
	public Supermarket getSupermarket() {
		return this.supermarket;
	}

	/**
	 * Sets the supermarket where the product is sold.
	 *
	 * @param supermarket the supermarket where the product is sold
	 */
	public void setSupermarket(Supermarket supermarket) {
		this.supermarket = supermarket;
	}

	/**
	 * Returns the name of the product.
	 *
	 * @return the name of the product
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the product.
	 *
	 * @param name the name of the product
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the image URL of the product.
	 *
	 * @return the image URL of the product
	 */
	public String getImage() {
		return this.image;
	}

	/**
	 * Sets the image URL of the product.
	 *
	 * @param image the image URL of the product
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Returns the availability status of the product.
	 *
	 * @return the availability status of the product
	 */
	public Boolean getAvailable() {
		return this.available;
	}

	/**
	 * Sets the availability status of the product.
	 *
	 * @param available the availability status of the product
	 */
	public void setAvailable(Boolean available) {
		this.available = available;
	}

	/**
	 * Returns the sale status of the product.
	 *
	 * @return the sale status of the product
	 */
	public Boolean getOnSale() {
		return this.onSale;
	}

	/**
	 * Sets the sale status of the product.
	 *
	 * @param onSale the sale status of the product
	 */
	public void setOnSale(Boolean onSale) {
		this.onSale = onSale;
	}

	/**
	 * Returns the set of price histories associated with the product.
	 *
	 * @return the set of price histories
	 */
	public Set<PriceHistory> getPriceHistories() {
		return this.priceHistories;
	}

	/**
	 * Sets the set of price histories associated with the product.
	 *
	 * @param priceHistories the set of price histories
	 */
	public void setPriceHistories(Set<PriceHistory> priceHistories) {
		this.priceHistories = priceHistories;
	}

	/**
	 * Returns the set of shopping list products associated with the product.
	 *
	 * @return the set of shopping list products
	 */
	public Set<ShoppingListProduct> getShoppingListProducts() {
		return this.shoppingListProducts;
	}

	/**
	 * Sets the set of shopping list products associated with the product.
	 *
	 * @param shoppingListProducts the set of shopping list products
	 */
	public void setShoppingListProducts(Set<ShoppingListProduct> shoppingListProducts) {
		this.shoppingListProducts = shoppingListProducts;
	}

	/**
	 * Returns the set of users who have marked the product as a favorite.
	 *
	 * @return the set of favorite user entities
	 */
	public Set<UserEntity> getFavouriteUserEntities() {
		return favouriteUserEntities;
	}

	/**
	 * Sets the set of users who have marked the product as a favorite.
	 *
	 * @param favouriteUserEntities the set of favorite user entities
	 */
	public void setFavouriteUserEntities(Set<UserEntity> favouriteUserEntities) {
		this.favouriteUserEntities = favouriteUserEntities;
	}

	@Override
	public int hashCode() {
		return Objects.hash(available, category, id, image, name, onSale, supermarket);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Product other)) {
			return false;
		}
        return Objects.equals(available, other.available) && Objects.equals(category, other.category)
				&& Objects.equals(id, other.id) && Objects.equals(image, other.image)
				&& Objects.equals(name, other.name) && Objects.equals(onSale, other.onSale)
				&& Objects.equals(supermarket, other.supermarket);
	}
}
