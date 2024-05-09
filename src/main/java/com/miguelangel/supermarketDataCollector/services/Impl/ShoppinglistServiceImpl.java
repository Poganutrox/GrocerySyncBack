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
 * Implementation of the IShoppingListService interface that provides operations for managing shopping lists.
 * <p>
 * Author: Miguel Ángel Moreno García
 */
@Service
public class ShoppinglistServiceImpl implements IShoppingListService {
    private final Logger logger = LoggerFactory.getLogger(ShoppinglistServiceImpl.class);

    @Autowired
    IShoppingListDAO shoppingListDAO;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    IProductService productService;

    @Autowired
    IUserService userService;

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

    @Override
    public List<ShoppingList> findByUserId(int userId) {
        return shoppingListDAO.findShoppingListByUserId(userId);
    }

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

    private List<UserEntity> obtainUsersFromIds(List<Integer> usersId) {
        List<UserEntity> userEntities = new ArrayList<>();
        if (usersId != null && !usersId.isEmpty()) {
            userEntities = userService.findByIds(usersId);
        }
        return userEntities;
    }

}