package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class PriceHistoryId implements java.io.Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Column(name = "product_id", nullable = false, length = 50)
	private String productId;
	
	@Column(name = "date", nullable = false, length = 13)
	private LocalDate date;

	public PriceHistoryId() {
	}

	public PriceHistoryId(String productId, LocalDate date) {
		this.productId = productId;
		this.date = date;
	}

	
	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public LocalDate getDate() {
		return this.date;
	}

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
