package com.miguelangel.supermarketDataCollector.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.miguelangel.supermarketDataCollector.entity.ShoppingList;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface IShoppingListDAO extends CrudRepository<ShoppingList, Integer> {

    @Query("SELECT s FROM ShoppingList s JOIN s.userEntities u WHERE u.id = :userId AND s.status = true")
    List<ShoppingList> findShoppingListByUserId(@Param("userId") int userId);

    @Query("SELECT s FROM ShoppingList s WHERE s.uniqueShareCode = :uniqueCode")
    Optional<ShoppingList> findShoppingListByUniqueCode(@Param("uniqueCode") String uniqueCode);
}
