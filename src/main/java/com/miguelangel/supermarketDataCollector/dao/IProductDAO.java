package com.miguelangel.supermarketDataCollector.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Product entities.
 */
@Transactional
public interface IProductDAO extends CrudRepository<Product, String>{

    /**
     * Finds the favorite products of a user.
     *
     * @param userId the ID of the user
     * @return a list of favorite products associated with the user
     */
    @Query("SELECT p FROM Product p JOIN p.favouriteUserEntities fu WHERE fu.id = :userId")
    List<Product> findFavouriteProducts(@Param("userId") int userId);

}
