package com.miguelangel.supermarketDataCollector.services;

import java.util.List;
import java.util.Optional;

import com.miguelangel.supermarketDataCollector.dto.ShoppingListResponseDTO;
import com.miguelangel.supermarketDataCollector.entity.ShoppingList;

/**
 * Interface defining operations available for managing shopping lists.
 * <p>
 * Author: Miguel Ángel Moreno García
 */
public interface IShoppingListService {

	/**
	 * Retrieves all shopping lists.
	 *
	 * @return List of all shopping lists.
	 */
	List<ShoppingList> findAll();

	/**
	 * Finds a shopping list by its unique identifier.
	 *
	 * @param shoppingListId The unique identifier of the shopping list.
	 * @return The found shopping list, or null if not found.
	 */
	ShoppingList findById(int shoppingListId);

	/**
	 * Finds shopping lists by user ID.
	 *
	 * @param userId The user ID.
	 * @return List of shopping lists associated with the user.
	 */
	List<ShoppingList> findByUserId(int userId);

	/**
	 * Saves a shopping list.
	 *
	 * @param shoppinglist The shopping list to save.
	 * @return The saved shopping list.
	 */
	ShoppingList save(ShoppingList shoppinglist);

	/**
	 * Deletes a shopping list.
	 *
	 * @param shoppinglist The shopping list to delete.
	 * @return The deleted shopping list.
	 */
	ShoppingList delete(ShoppingList shoppinglist);

	ShoppingList createShoppingList(ShoppingListResponseDTO shoppingListResponseDTO);
	Optional<ShoppingList> findShoppingListByUniqueCode(String uniqueCode);
	ShoppingList updateShoppingList(ShoppingListResponseDTO shoppingListResponseDTO);

	void deleteShoppingListByUserId(int userId, int shoppingListId);

	void removeChanges(int userId, int shoppingListId);
}
