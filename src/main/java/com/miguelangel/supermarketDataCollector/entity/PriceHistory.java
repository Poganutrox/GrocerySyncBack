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

	public PriceHistory() {
	}

	public PriceHistory(PriceHistoryId id, Product product) {
		this.id = id;
		this.product = product;
	}

	public PriceHistory(PriceHistoryId id, Product product, BigDecimal price, BigDecimal salePrice,
						BigDecimal bulkPrice) {
		this.id = id;
		this.product = product;
		this.price = price;
		this.salePrice = salePrice;
		this.bulkPrice = bulkPrice;
	}

	public PriceHistoryId getId() {
		return this.id;
	}

	public void setId(PriceHistoryId id) {
		this.id = id;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getSalePrice() {
		return this.salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getBulkPrice() {
		return this.bulkPrice;
	}

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
