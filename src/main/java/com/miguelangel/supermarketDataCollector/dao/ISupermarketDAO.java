package com.miguelangel.supermarketDataCollector.dao;

import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.Supermarket;

public interface ISupermarketDAO extends CrudRepository<Supermarket, Integer> {

}
