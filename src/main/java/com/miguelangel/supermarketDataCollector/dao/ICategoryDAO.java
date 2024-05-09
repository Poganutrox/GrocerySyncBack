package com.miguelangel.supermarketDataCollector.dao;

import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.Category;

public interface ICategoryDAO extends CrudRepository<Category, Integer> {}
