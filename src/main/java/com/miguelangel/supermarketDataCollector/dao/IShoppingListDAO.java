package com.miguelangel.supermarketDataCollector.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.ShoppingList;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on ShoppingList entities.
 */
@Transactional
public interface IShoppingListDAO extends CrudRepository<ShoppingList, Integer> {

    /**
     * Finds shopping lists associated with a user by their user ID and status.
     *
     * @param userId the ID of the user
     * @return a list of shopping lists associated with the user and with status set to true
     */
    @Query("SELECT s FROM ShoppingList s JOIN s.userEntities u WHERE u.id = :userId AND s.status = true")
    List<ShoppingList> findShoppingListByUserId(@Param("userId") int userId);


    /**
     * Finds a shopping list by its unique share code.
     *
     * @param uniqueCode the unique share code of the shopping list
     * @return an optional containing the shopping list if found, otherwise empty
     */
    @Query("SELECT s FROM ShoppingList s WHERE s.uniqueShareCode = :uniqueCode")
    Optional<ShoppingList> findShoppingListByUniqueCode(@Param("uniqueCode") String uniqueCode);
}
