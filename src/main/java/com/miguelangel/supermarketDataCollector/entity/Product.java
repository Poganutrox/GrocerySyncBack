package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

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

	public Product() {
	}

	public Product(String id) {
		this.id = id;
	}

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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Supermarket getSupermarket() {
		return this.supermarket;
	}

	public void setSupermarket(Supermarket supermarket) {
		this.supermarket = supermarket;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<PriceHistory> getPriceHistories() {
		return this.priceHistories;
	}

	public void setPriceHistories(Set<PriceHistory> priceHistories) {
		this.priceHistories = priceHistories;
	}

	public Set<ShoppingListProduct> getShoppingListProducts() {
		return this.shoppingListProducts;
	}

	public void setShoppingListProducts(Set<ShoppingListProduct> shoppingListProducts) {
		this.shoppingListProducts = shoppingListProducts;
	}

	public Set<UserEntity> getFavouriteUserEntities() {
		return favouriteUserEntities;
	}

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
