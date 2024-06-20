package com.miguelangel.supermarketDataCollector.services;

import java.util.List;
import java.util.Optional;

import com.miguelangel.supermarketDataCollector.dto.ShoppingListResponseDTO;
import com.miguelangel.supermarketDataCollector.entity.ShoppingList;

/**
 * Interface defining operations available for managing shopping lists.
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
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

	/**
	 * Creates a new shopping list based on the provided DTO.
	 *
	 * @param shoppingListResponseDTO The DTO containing information for creating the shopping list.
	 * @return The newly created shopping list.
	 */
	ShoppingList createShoppingList(ShoppingListResponseDTO shoppingListResponseDTO);

	/**
	 * Finds a shopping list by its unique code.
	 *
	 * @param uniqueCode The unique code of the shopping list.
	 * @return An Optional containing the found shopping list, or empty if not found.
	 */
	Optional<ShoppingList> findShoppingListByUniqueCode(String uniqueCode);

	/**
	 * Updates an existing shopping list based on the provided DTO.
	 *
	 * @param shoppingListResponseDTO The DTO containing information for updating the shopping list.
	 * @return The updated shopping list.
	 */
	ShoppingList updateShoppingList(ShoppingListResponseDTO shoppingListResponseDTO);

	/**
	 * Deletes a shopping list associated with a specific user.
	 *
	 * @param userId          The user ID whose shopping list is to be deleted.
	 * @param shoppingListId  The ID of the shopping list to be deleted.
	 */
	void deleteShoppingListByUserId(int userId, int shoppingListId);

	/**
	 * Removes all changes made to a shopping list associated with a specific user.
	 *
	 * @param userId          The user ID whose shopping list changes are to be removed.
	 * @param shoppingListId  The ID of the shopping list for which changes are to be removed.
	 */
	void removeChanges(int userId, int shoppingListId);
}
