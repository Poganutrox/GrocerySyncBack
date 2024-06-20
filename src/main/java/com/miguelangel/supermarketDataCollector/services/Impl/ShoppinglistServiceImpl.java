package com.miguelangel.supermarketDataCollector.services.Impl;

import java.time.LocalDate;
import java.util.*;

import com.miguelangel.supermarketDataCollector.dto.ShoppingListResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.miguelangel.supermarketDataCollector.dao.IShoppingListDAO;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;
import com.miguelangel.supermarketDataCollector.entity.Product;
import com.miguelangel.supermarketDataCollector.entity.ShoppingList;
import com.miguelangel.supermarketDataCollector.entity.ShoppingListProduct;
import com.miguelangel.supermarketDataCollector.entity.ShoppingListProductId;
import com.miguelangel.supermarketDataCollector.services.IProductService;
import com.miguelangel.supermarketDataCollector.services.IShoppingListService;
import com.miguelangel.supermarketDataCollector.services.IUserService;

import jakarta.persistence.EntityManager;

/**
 * Implementation of the IShoppingListService interface that provides operations for managing shopping lists
 *
 *  @since 2024
 *  @author Miguel Angel Moreno Garcia
 */
@Service
public class ShoppinglistServiceImpl implements IShoppingListService {
    private final Logger logger = LoggerFactory.getLogger(ShoppinglistServiceImpl.class);

    private final IShoppingListDAO shoppingListDAO;
    private final IProductService productService;
    private final IUserService userService;

    @Autowired
    public ShoppinglistServiceImpl(IShoppingListDAO shoppingListDAO, IUserService userService, IProductService productService) {
        this.shoppingListDAO = shoppingListDAO;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Saves a shopping list in the system.
     *
     * @param shoppinglist The shopping list to save.
     * @return The saved shopping list.
     * @throws IllegalArgumentException          If the shopping list contains invalid data.
     * @throws OptimisticLockingFailureException If there is a concurrency issue when saving the shopping list.
     * @throws RuntimeException                  If an unexpected error occurs while saving the shopping list.
     */
    @Override
    public ShoppingList save(ShoppingList shoppinglist) {
        try {
            return shoppingListDAO.save(shoppinglist);
        } catch (IllegalArgumentException e) {
            logger.error("Error saving the shopping list: Invalid argument", e);
            throw new IllegalArgumentException("The shopping list could not be saved due to an invalid argument.");
        } catch (OptimisticLockingFailureException e) {
            logger.error("Error saving the shopping list: Update conflict", e);
            throw new OptimisticLockingFailureException(
                    "The shopping list could not be saved due to an update conflict.");
        } catch (Exception e) {
            logger.error("Unexpected error while saving the shopping list", e);
            throw new RuntimeException("An unexpected error occurred while saving the shopping list.");
        }
    }

    /**
     * Deletes a shopping list from the system.
     *
     * @param shoppinglist The shopping list to delete.
     * @return The deleted shopping list.
     * @throws IllegalArgumentException          If the shopping list contains invalid data.
     * @throws OptimisticLockingFailureException If there is a concurrency issue when deleting the shopping list.
     * @throws RuntimeException                  If an unexpected error occurs while deleting the shopping list.
     */
    @Override
    public ShoppingList delete(ShoppingList shoppinglist) {
        try {
            shoppingListDAO.delete(shoppinglist);
            return shoppinglist;
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting the shopping list: Invalid argument", e);
            throw new IllegalArgumentException("The shopping list could not be deleted due to an invalid argument.");
        } catch (OptimisticLockingFailureException e) {
            logger.error("Error deleting the shopping list: Update conflict", e);
            throw new OptimisticLockingFailureException(
                    "The shopping list could not be deleted due to an update conflict.");
        } catch (Exception e) {
            logger.error("Unexpected error while deleting the shopping list", e);
            throw new RuntimeException("An unexpected error occurred while deleting the shopping list.");
        }
    }

    /**
     * Retrieves a shopping list by its unique identifier.
     *
     * @param shoppingListId The unique identifier of the shopping list.
     * @return The found shopping list.
     * @throws IllegalArgumentException If no shopping list is found with the given ID.
     */
    @Override
    public ShoppingList findById(int shoppingListId) {
        return shoppingListDAO.findById(shoppingListId).orElseThrow(
                () -> new IllegalArgumentException("Shopping list with ID " + shoppingListId + " not found"));

    }

    /**
     * Retrieves all shopping lists associated with a given user ID.
     *
     * @param userId The ID of the user.
     * @return A list of shopping lists associated with the user.
     */
    @Override
    public List<ShoppingList> findByUserId(int userId) {
        return shoppingListDAO.findShoppingListByUserId(userId);
    }

    /**
     * Updates a shopping list based on the information provided in the ShoppingListResponseDTO.
     *
     * @param shoppingListResponseDTO The DTO containing the updated shopping list information.
     * @return The updated shopping list entity.
     * @throws IllegalArgumentException If the provided shopping list ID does not exist.
     */
    @Override
    public ShoppingList updateShoppingList(ShoppingListResponseDTO shoppingListResponseDTO) {
        Integer id = shoppingListResponseDTO.getId();
        String name = shoppingListResponseDTO.getName();
        Boolean status = shoppingListResponseDTO.isStatus();
        List<Integer> usersId = shoppingListResponseDTO.getUsers();
        Map<String, Integer> shoppingListProducts = shoppingListResponseDTO.getShoppingListProducts();

        ShoppingList oldShoppingList = findById(id);

        List<UserEntity> userEntities = obtainUsersFromIds(usersId);
        List<ShoppingListProduct> shoppingListProductsToSave = obtainProductsFromIds(id, oldShoppingList, shoppingListProducts);

        ShoppingList updatedShoppingList = new ShoppingList(
                oldShoppingList.getId(),
                oldShoppingList.getCreatorUser(),
                name,
                oldShoppingList.getCreationDate(),
                status,
                new HashSet<>(userEntities),
                new HashSet<>(shoppingListProductsToSave));
        return shoppingListDAO.save(updatedShoppingList);
    }


    /**
     * Deletes a shopping list associated with a specific user.
     *
     * @param userId         The ID of the user.
     * @param shoppingListId The ID of the shopping list to delete.
     * @throws EntityNotFoundException If the shopping list or user does not exist.
     */
    @Override
    public void deleteShoppingListByUserId(int userId, int shoppingListId) {
        Optional<ShoppingList> shoppingListOpt = shoppingListDAO.findById(shoppingListId);
        Optional<UserEntity> userOpt = userService.findById(userId);

        if (shoppingListOpt.isEmpty() || userOpt.isEmpty()) {
            throw new EntityNotFoundException();
        }

        UserEntity user = userOpt.get();
        ShoppingList shoppingList = shoppingListOpt.get();

        if (shoppingList.getCreatorUser() == user) {
            shoppingList.setStatus(false);
        } else {
            shoppingList.getUsers().remove(user);
        }
        shoppingListDAO.save(shoppingList);

    }

    /**
     * Removes changes made to a shopping list associated with a specific user.
     *
     * @param userId         The ID of the user.
     * @param shoppingListId The ID of the shopping list.
     * @throws EntityNotFoundException If the shopping list or user does not exist.
     */
    @Override
    public void removeChanges(int userId, int shoppingListId) {
        Optional<ShoppingList> shoppingListOpt = shoppingListDAO.findById(shoppingListId);
        Optional<UserEntity> userOpt = userService.findById(userId);

        if (shoppingListOpt.isEmpty() || userOpt.isEmpty()) {
            throw new EntityNotFoundException();
        }

        UserEntity user = userOpt.get();
        ShoppingList shoppingList = shoppingListOpt.get();

        if (shoppingList.getCreatorUser() == user) {
            shoppingList.setStatus(true);
        } else {
            shoppingList.getUsers().add(user);
        }

        shoppingListDAO.save(shoppingList);
    }


    /**
     * Creates a new shopping list based on the information provided in the ShoppingListResponseDTO.
     *
     * @param shoppingListResponseDTO The DTO containing the information of the new shopping list.
     * @return The newly created shopping list entity.
     * @throws IllegalArgumentException If the creator user ID is invalid.
     */
    @Override
    public ShoppingList createShoppingList(ShoppingListResponseDTO shoppingListResponseDTO) {
        Integer creatorUserId = shoppingListResponseDTO.getCreatorUserId();
        String name = shoppingListResponseDTO.getName();
        Boolean status = shoppingListResponseDTO.isStatus();
        List<Integer> usersId = shoppingListResponseDTO.getUsers();
        Map<String, Integer> shoppingListProducts = shoppingListResponseDTO.getShoppingListProducts();

        List<UserEntity> userEntities = obtainUsersFromIds(usersId);
        LocalDate creationDate = LocalDate.now();

        Optional<UserEntity> creatorUser = userService.findById(creatorUserId);
        if (creatorUser.isEmpty()) {
            throw new IllegalArgumentException("Invalid id for the creator user");
        }

        ShoppingList shoppingList = new ShoppingList(creatorUser.get(), name, creationDate, status, new HashSet<>(userEntities),
                new HashSet<>());
        ShoppingList newShoppingList = shoppingListDAO.save(shoppingList);
        List<ShoppingListProduct> shoppingListProductsToSave = obtainProductsFromIds(newShoppingList.getId(), newShoppingList, shoppingListProducts);

        if (!shoppingListProductsToSave.isEmpty()) {
            newShoppingList.getShoppingListProducts().addAll(shoppingListProductsToSave);
            shoppingListDAO.save(newShoppingList);
        }

        return newShoppingList;
    }

    /**
     * Retrieves a shopping list by its unique code.
     *
     * @param uniqueCode The unique code of the shopping list.
     * @return An Optional containing the found shopping list if it exists, otherwise an empty Optional.
     */
    @Override
    public Optional<ShoppingList> findShoppingListByUniqueCode(String uniqueCode) {
        return shoppingListDAO.findShoppingListByUniqueCode(uniqueCode);
    }

    /**
     * Retrieves all shopping lists in the system.
     *
     * @return List of all shopping lists.
     */
    @Override
    public List<ShoppingList> findAll() {
        try {
            return (List<ShoppingList>) shoppingListDAO.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving all shopping lists", e);
            return null;
        }
    }

    /**
     * Retrieves a list of ShoppingListProduct entities based on the provided product IDs and quantities.
     *
     * @param id                   The ID of the shopping list.
     * @param oldShoppingList      The existing shopping list entity.
     * @param shoppingListProducts A map containing product IDs as keys and their corresponding quantities.
     * @return A list of ShoppingListProduct entities.
     * @throws IllegalArgumentException If any of the provided product IDs does not exist in the database.
     */
    private List<ShoppingListProduct> obtainProductsFromIds(Integer id, ShoppingList oldShoppingList, Map<String, Integer> shoppingListProducts) {
        List<ShoppingListProduct> shoppingListProductsToSave = new ArrayList<>();
        for (String productId : shoppingListProducts.keySet()) {
            Integer quantity = shoppingListProducts.get(productId);

            Optional<Product> product = productService.findById(productId);
            if (product.isPresent()) {
                ShoppingListProduct shoppinglistProduct = new ShoppingListProduct(
                        new ShoppingListProductId(id, product.get().getId()), product.get()
                        , oldShoppingList, quantity);
                shoppingListProductsToSave.add(shoppinglistProduct);
            } else {
                throw new IllegalArgumentException("The productId " + productId + " doesn't exist in the data base");
            }
        }
        return shoppingListProductsToSave;
    }

    /**
     * Retrieves a list of UserEntity entities based on the provided user IDs.
     *
     * @param usersId The list of user IDs.
     * @return A list of UserEntity entities.
     */
    private List<UserEntity> obtainUsersFromIds(List<Integer> usersId) {
        List<UserEntity> userEntities = new ArrayList<>();
        if (usersId != null && !usersId.isEmpty()) {
            userEntities = userService.findByIds(usersId);
        }
        return userEntities;
    }

}