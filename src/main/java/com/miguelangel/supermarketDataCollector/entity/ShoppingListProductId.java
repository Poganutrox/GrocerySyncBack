package com.miguelangel.supermarketDataCollector.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serial;
import java.util.Objects;

@Embeddable
public class ShoppingListProductId implements java.io.Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Column(name = "shopping_list_id", nullable = false)
	private int shoppingListId;
	
	@Column(name = "product_id", nullable = false, length = 50)
	private String productId;

	public ShoppingListProductId() {
	}

	public ShoppingListProductId(int shoppingListId, String productId) {
		this.shoppingListId = shoppingListId;
		this.productId = productId;
	}

	
	public int getShoppingListId() {
		return this.shoppingListId;
	}

	public void setShoppingListId(int shoppingListId) {
		this.shoppingListId = shoppingListId;
	}

	
	public String getProductId() {
		return this.productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ShoppingListProductId castOther))
			return false;

        return (this.getShoppingListId() == castOther.getShoppingListId())
				&& ((Objects.equals(this.getProductId(), castOther.getProductId())) || (this.getProductId() != null
						&& castOther.getProductId() != null && this.getProductId().equals(castOther.getProductId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getShoppingListId();
		result = 37 * result + (getProductId() == null ? 0 : this.getProductId().hashCode());
		return result;
	}

}
