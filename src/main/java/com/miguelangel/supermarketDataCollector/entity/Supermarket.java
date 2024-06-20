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
 * Represents a supermarket entity in the system.
 * This class is mapped to the "supermarket" table in the database.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
@Entity
@Table(name = "supermarket")
public class Supermarket implements java.io.Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "url", length = 100)
	private String url;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supermarket")
	private Set<Product> products = new HashSet<Product>(0);

	/**
	 * Default constructor.
	 */
	public Supermarket() {
	}

	/**
	 * Constructs a Supermarket with the specified id.
	 *
	 * @param id the id of the supermarket
	 */
	public Supermarket(int id) {
		this.id = id;
	}

	/**
	 * Constructs a Supermarket with the specified id, name, and URL.
	 *
	 * @param id   the id of the supermarket
	 * @param name the name of the supermarket
	 * @param url  the URL of the supermarket
	 */
	public Supermarket(int id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	/**
	 * Retrieves the id of the supermarket.
	 *
	 * @return the id of the supermarket
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the id of the supermarket.
	 *
	 * @param id the id of the supermarket
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Retrieves the name of the supermarket.
	 *
	 * @return the name of the supermarket
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the supermarket.
	 *
	 * @param name the name of the supermarket
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the URL of the supermarket.
	 *
	 * @return the URL of the supermarket
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Sets the URL of the supermarket.
	 *
	 * @param url the URL of the supermarket
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Retrieves the set of products associated with this supermarket.
	 *
	 * @return the set of products
	 */
	public Set<Product> getProducts() {
		return this.products;
	}

	/**
	 * Sets the products associated with this supermarket.
	 *
	 * @param products the set of products
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Supermarket other)) {
			return false;
		}
        return id == other.id && Objects.equals(name, other.name)&& Objects.equals(url, other.url);
	}
}
