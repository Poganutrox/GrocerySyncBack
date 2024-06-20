package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a category within the supermarket data collection system.
 * Each category can have multiple associated products.
 * This class is mapped to the "category" table in the database.
 *
 * @since 2024
 * @author Miguel Angel Moreno Garcia
 */
@Entity
@Table(name = "category")
public class Category implements java.io.Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "name")
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	private Set<Product> products = new HashSet<Product>(0);

	/**
	 * Default constructor.
	 */
	public Category() {
	}

	/**
	 * Constructs a Category with the specified id.
	 *
	 * @param id the id of the category
	 */
	public Category(int id) {
		this.id = id;
	}

	/**
	 * Constructs a Category with the specified id and name.
	 *
	 * @param id the id of the category
	 * @param name the name of the category
	 */
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Returns the id of the category.
	 *
	 * @return the id of the category
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the id of the category.
	 *
	 * @param id the id of the category
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the name of the category.
	 *
	 * @return the name of the category
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the category.
	 *
	 * @param name the name of the category
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the set of products associated with this category.
	 *
	 * @return the set of products
	 */
	public Set<Product> getProducts() {
		return this.products;
	}

	/**
	 * Sets the products associated with this category.
	 *
	 * @param products the set of products
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Category other)) {
			return false;
		}
        return id == other.id && Objects.equals(name, other.name);
	}

}
