package com.miguelangel.supermarketDataCollector.dao;

import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.Supermarket;

/**
 * Repository interface for performing CRUD operations on Supermarket entities.
 */
public interface ISupermarketDAO extends CrudRepository<Supermarket, Integer> {

}
