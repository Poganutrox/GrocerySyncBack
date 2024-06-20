package com.miguelangel.supermarketDataCollector.model;

import java.util.Hashtable;
import java.util.Set;

import com.google.gson.JsonObject;
import com.miguelangel.supermarketDataCollector.entity.Category;
import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.Supermarket;

/**
 * This interface defines the contract for a provider that collects data from a specific supermarket.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
public interface IProvider {

	/**
	 * Retrieves the supermarket associated with this provider.
	 *
	 * @return the Supermarket object representing the supermarket
	 */
	Supermarket getSupermarket();

	/**
	 * Collects the products available at the supermarket.
	 *
	 * @return a Set of Product objects representing the collected products
	 */
	Set<Product> collectProducts();

	/**
	 * Extracts the products from the provided JSON response belonging to the specified category.
	 *
	 * @param response the JSON response containing product data
	 * @param categoryProduct the category of products to extract
	 * @return a Set of Product objects belonging to the specified category
	 */
	Set<Product> getProductList(JsonObject response, Category categoryProduct);

	/**
	 * Retrieves a dictionary mapping categories to arrays of subcategories.
	 *
	 * @return a Hashtable representing the dictionary of categories and their subcategories
	 */
	Hashtable<Category, String[]> getDictCategories();

	/**
	 * Retrieves the URI of the supermarket's API.
	 *
	 * @return a String representing the URI of the supermarket
	 */
	String getMarketUri();

}
