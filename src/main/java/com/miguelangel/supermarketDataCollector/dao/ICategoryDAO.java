package com.miguelangel.supermarketDataCollector.dao;

import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.Category;

/**
 * Repository interface for performing CRUD operations on Category entities.
 */
public interface ICategoryDAO extends CrudRepository<Category, Integer> {}
