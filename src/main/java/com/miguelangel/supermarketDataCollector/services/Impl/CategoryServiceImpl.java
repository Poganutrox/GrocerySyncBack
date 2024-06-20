package com.miguelangel.supermarketDataCollector.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelangel.supermarketDataCollector.dao.ICategoryDAO;
import com.miguelangel.supermarketDataCollector.entity.Category;
import com.miguelangel.supermarketDataCollector.services.ICategoryService;

import java.util.List;

/**
 * Implementation of the ICategoryService interface providing methods to manage categories.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private ICategoryDAO categoryDAO;

	/**
	 * Retrieves all categories.
	 *
	 * @return List of all categories.
	 */
	@Override
	public List<Category> findAll() {
		return (List<Category>) categoryDAO.findAll();
	}

	/**
	 * Finds a category by its unique identifier.
	 *
	 * @param id The unique identifier of the category.
	 * @return The found category, or null if not found.
	 */
	@Override
	public Category findById(int id) {
		return categoryDAO.findById(id).orElse(null);
	}
}
