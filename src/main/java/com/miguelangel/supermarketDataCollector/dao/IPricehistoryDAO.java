package com.miguelangel.supermarketDataCollector.dao;

import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.PriceHistory;
import com.miguelangel.supermarketDataCollector.entity.PriceHistoryId;

/**
 * Repository interface for performing CRUD operations on PriceHistory entities.
 */
public interface IPricehistoryDAO extends CrudRepository<PriceHistory, PriceHistoryId>{

}
