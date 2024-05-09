package com.miguelangel.supermarketDataCollector.services;


import com.miguelangel.supermarketDataCollector.entity.Category;

import java.util.List;


public interface ICategoryService {

	List<Category> findAll();

	Category findById(int id);

}
