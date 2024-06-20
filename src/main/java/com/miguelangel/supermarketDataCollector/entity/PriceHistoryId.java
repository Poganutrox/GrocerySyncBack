package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Represents the composite primary key for the PriceHistory entity.
 * This class is mapped to the embedded primary key of the "pricehistory" table in the database.
 * The primary key consists of a product ID and a date.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
@Embeddable
public class PriceHistoryId implements java.io.Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Column(name = "product_id", nullable = false, length = 50)
	private String productId;
	
	@Column(name = "date", nullable = false, length = 13)
	private LocalDate date;

	/**
	 * Default constructor.
	 */
	public PriceHistoryId() {
	}

	/**
	 * Constructs a PriceHistoryId with the specified product ID and date.
	 *
	 * @param productId the ID of the product
	 * @param date the date of the price record
	 */
	public PriceHistoryId(String productId, LocalDate date) {
		this.productId = productId;
		this.date = date;
	}

	/**
	 * Returns the product ID.
	 *
	 * @return the product ID
	 */
	public String getProductId() {
		return this.productId;
	}

	/**
	 * Sets the product ID.
	 *
	 * @param productId the product ID
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * Returns the date.
	 *
	 * @return the date
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean equals(Object other) {
	    if (this == other) return true;
	    if (other == null) return false;
	    if (!(other instanceof PriceHistoryId castOther)) return false;

        return Objects.equals(productId, castOther.productId) &&
	           Objects.equals(date.getYear(), castOther.date.getYear()) &&
	           Objects.equals(date.getMonth(), castOther.date.getMonth()) &&
	           Objects.equals(date.getDayOfMonth(), castOther.date.getDayOfMonth());
	}

	public int hashCode() {
	    return Objects.hash(productId, date.getYear(), date.getMonth(), date.getDayOfMonth());
	}


	@Override
	public String toString() {
		return "PriceHistoryId [productId=" + productId + ", date=" + date + "]";
	}
	

}
