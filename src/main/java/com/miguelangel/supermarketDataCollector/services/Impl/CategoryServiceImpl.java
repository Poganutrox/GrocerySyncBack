package com.miguelangel.supermarketDataCollector.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miguelangel.supermarketDataCollector.dao.ICategoryDAO;
import com.miguelangel.supermarketDataCollector.entity.Category;
import com.miguelangel.supermarketDataCollector.services.ICategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
	
	@Autowired
	private ICategoryDAO categoryDAO;

	@Override
	public List<Category> findAll() {
		return (List<Category>) categoryDAO.findAll();
	}

	@Override
	public Category findById(int id) {
		return categoryDAO.findById(id).orElse(null);
	}

}
