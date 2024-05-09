package com.miguelangel.supermarketDataCollector.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface IProductDAO extends CrudRepository<Product, String>{

    @Query("SELECT p FROM Product p JOIN p.favouriteUserEntities fu WHERE fu.id = :userId")
    List<Product> findFavouriteProducts(@Param("userId") int userId);

}
