package com.miguelangel.supermarketDataCollector.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Represents the price history of a product within the supermarket data collection system.
 * Each record stores the price, sale price, and bulk price of a product on a specific date.
 * This class is mapped to the "pricehistory" table in the database.
 *
 * @since 2024
 * @author Miguel Angel Moreno Garcia
 */
@Entity
@Table(name = "pricehistory")
@NamedQueries({ @NamedQuery(name = "PriceHistory.lastPrice", query = """
        SELECT p.price, p.salePrice\s
        FROM PriceHistory p
        WHERE p.id.productId = :id\s
        AND p.price = :price
        AND p.salePrice = :salePrice
        ORDER BY p.id.date DESC\s
        LIMIT 1""") })
public class PriceHistory implements java.io.Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false, length = 50)),
			@AttributeOverride(name = "date", column = @Column(name = "date", nullable = false, length = 13)) })
	private PriceHistoryId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
	private Product product;

	@Column(name = "price", precision = 19, scale = 2)
	private BigDecimal price;

	@Column(name = "sale_price", precision = 19, scale = 2)
	private BigDecimal salePrice;

	@Column(name = "bulk_price", precision = 19, scale = 2)
	private BigDecimal bulkPrice;

	/**
	 * Default constructor.
	 */
	public PriceHistory() {
	}

	/**
	 * Constructs a PriceHistory with the specified id and product.
	 *
	 * @param id the composite id of the price history
	 * @param product the associated product
	 */
	public PriceHistory(PriceHistoryId id, Product product) {
		this.id = id;
		this.product = product;
	}

	/**
	 * Constructs a PriceHistory with the specified id, product, price, sale price, and bulk price.
	 *
	 * @param id the composite id of the price history
	 * @param product the associated product
	 * @param price the price of the product
	 * @param salePrice the sale price of the product
	 * @param bulkPrice the bulk price of the product
	 */
	public PriceHistory(PriceHistoryId id, Product product, BigDecimal price, BigDecimal salePrice, BigDecimal bulkPrice) {
		this.id = id;
		this.product = product;
		this.price = price;
		this.salePrice = salePrice;
		this.bulkPrice = bulkPrice;
	}

	/**
	 * Returns the id of the price history.
	 *
	 * @return the id of the price history
	 */
	public PriceHistoryId getId() {
		return this.id;
	}

	/**
	 * Sets the id of the price history.
	 *
	 * @param id the id of the price history
	 */
	public void setId(PriceHistoryId id) {
		this.id = id;
	}

	/**
	 * Returns the associated product.
	 *
	 * @return the associated product
	 */
	public Product getProduct() {
		return this.product;
	}

	/**
	 * Sets the associated product.
	 *
	 * @param product the associated product
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * Returns the price of the product.
	 *
	 * @return the price of the product
	 */
	public BigDecimal getPrice() {
		return this.price;
	}

	/**
	 * Sets the price of the product.
	 *
	 * @param price the price of the product
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Returns the sale price of the product.
	 *
	 * @return the sale price of the product
	 */
	public BigDecimal getSalePrice() {
		return this.salePrice;
	}

	/**
	 * Sets the sale price of the product.
	 *
	 * @param salePrice the sale price of the product
	 */
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * Returns the bulk price of the product.
	 *
	 * @return the bulk price of the product
	 */
	public BigDecimal getBulkPrice() {
		return this.bulkPrice;
	}

	/**
	 * Sets the bulk price of the product.
	 *
	 * @param bulkPrice the bulk price of the product
	 */
	public void setBulkPrice(BigDecimal bulkPrice) {
		this.bulkPrice = bulkPrice;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bulkPrice, id, price, product, salePrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PriceHistory other)) {
			return false;
		}
        return Objects.equals(bulkPrice, other.bulkPrice) && Objects.equals(id, other.id)
				&& Objects.equals(price, other.price) && Objects.equals(product, other.product)
				&& Objects.equals(salePrice, other.salePrice);
	}

	@Override
	public String toString() {
		return "PriceHistory [id=" + id + "]";
	}

}
