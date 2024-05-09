package com.miguelangel.supermarketDataCollector.model;

import java.util.Hashtable;
import java.util.Set;

import com.google.gson.JsonObject;
import com.miguelangel.supermarketDataCollector.entity.Category;
import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.Supermarket;

public interface IProvider {
	
	Supermarket getSupermarket();

	Set<Product> collectProducts(); 
	
	Set<Product> getProductList(JsonObject response, Category categoryProduct);

	Hashtable<Category, String[]> getDictCategories();

	String getMarketUri();
}
