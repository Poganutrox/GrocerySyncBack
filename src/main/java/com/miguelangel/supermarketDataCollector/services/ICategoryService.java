/**
 * Interface defining the services available for manipulating categories in the system.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
package com.miguelangel.supermarketDataCollector.services;

import com.miguelangel.supermarketDataCollector.entity.Category;

import java.util.List;

public interface ICategoryService {

	/**
	 * Retrieves all available categories in the system.
	 *
	 * @return A list of Category objects representing all available categories.
	 */
	List<Category> findAll();

	/**
	 * Finds a category by its unique identifier.
	 *
	 * @param id The unique identifier of the category to find.
	 * @return The category corresponding to the provided identifier, or null if not found.
	 */
	Category findById(int id);
}
