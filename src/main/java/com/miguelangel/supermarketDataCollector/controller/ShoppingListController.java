package com.miguelangel.supermarketDataCollector.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.miguelangel.supermarketDataCollector.dto.ShoppingListResponseDTO;
import com.miguelangel.supermarketDataCollector.entity.UserEntity;
import com.miguelangel.supermarketDataCollector.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.miguelangel.supermarketDataCollector.dto.ShoppingListDTO;
import com.miguelangel.supermarketDataCollector.entity.ShoppingList;
import com.miguelangel.supermarketDataCollector.services.IShoppingListService;

/**
 * Controller for handling shoppingList-related HTTP requests.
 *
 * @since 2024
 * @author Miguel Ángel Moreno García
 */
@RestController
@RequestMapping("/api/shoppingList")
public class ShoppingListController {
    @Autowired
    IShoppingListService shoppingListService;

    @Autowired
    IUserService userService;

    /**
     * Retrieves a list of shopping lists.
     *
     * @return a list of ShoppingListDTO objects representing the shopping lists
     */
    @GetMapping(produces = "application/json")
    private List<ShoppingListDTO> getShoppingLists() {
        List<ShoppingList> shoppingLists = shoppingListService.findAll();
        return shoppingLists.stream().map(ShoppingListDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves shopping lists associated with a specific user.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing the shopping lists associated with the user and an HTTP status code
     */
    @GetMapping(value = "/findByUserId", produces = "application/json")
    private ResponseEntity<List<ShoppingListDTO>> getShoppingListByUserId(@RequestParam(value = "userId") int userId) {
        List<ShoppingList> shoppingLists;
        try {
            shoppingLists = shoppingListService.findByUserId(userId);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<ShoppingListDTO> shoppingListDTO = shoppingLists.stream().map(ShoppingListDTO::new).toList();
        return new ResponseEntity<>(shoppingListDTO, HttpStatus.OK);
    }

    /**
     * Saves a new shopping list.
     *
     * @param shoppingListResponseDTO the shopping list information
     * @return a ResponseEntity containing the newly created shopping list and an HTTP status code
     */
    @PostMapping
    private ResponseEntity<ShoppingListDTO> saveShoppingList(
            @RequestBody ShoppingListResponseDTO shoppingListResponseDTO) {
        try {
            // Check for null values
            if (shoppingListResponseDTO.getName() == null || shoppingListResponseDTO.getCreatorUserId() == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Create shopping list
            ShoppingList newShoppingList = shoppingListService.createShoppingList(shoppingListResponseDTO);
            ShoppingListDTO shoppingListDTO = new ShoppingListDTO(newShoppingList);
            return new ResponseEntity<>(shoppingListDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing shopping list.
     *
     * @param shoppingListResponseDTO the updated shopping list information
     * @return a ResponseEntity containing the updated shopping list and an HTTP status code
     */
    @PutMapping(value = "/update")
    private ResponseEntity<ShoppingListDTO> updateShoppingList(
            @RequestBody ShoppingListResponseDTO shoppingListResponseDTO
    ) {
        try {
            ShoppingList shoppingList = shoppingListService.updateShoppingList(shoppingListResponseDTO);
            ShoppingListDTO shoppingListDTO = new ShoppingListDTO(shoppingList);
            return new ResponseEntity<>(shoppingListDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Imports a shopping list using a unique code.
     *
     * @param uniqueCode       the unique code of the shopping list to import
     * @param applicantUserId  the ID of the user requesting to import the shopping list
     * @return a ResponseEntity indicating the success or failure of the import operation and an HTTP status code
     */
    @GetMapping(value = "/import")
    private ResponseEntity<String> importShoppingList(
            @RequestParam(value = "uniqueCode") String uniqueCode,
            @RequestParam(value = "applicantUserId") Integer applicantUserId
    ) {
        Optional<ShoppingList> shoppingListOpt = shoppingListService.findShoppingListByUniqueCode(uniqueCode);
        Optional<UserEntity> applicantUserOpt = userService.findById(applicantUserId);

        if (shoppingListOpt.isEmpty() || applicantUserOpt.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ShoppingList shoppingList = shoppingListOpt.get();
        shoppingList.getUsers().add(applicantUserOpt.get());
        try {
            shoppingListService.save(shoppingList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Deletes a shopping list associated with a user.
     *
     * @param userId         the ID of the user
     * @param shoppingListId the ID of the shopping list to delete
     * @return a ResponseEntity indicating the success or failure of the deletion operation and an HTTP status code
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<Boolean> deleteShoppingListByUserId(
            @RequestParam("userId") int userId,
            @RequestParam("shoppingListId") int shoppingListId
    ){
        try{
            shoppingListService.deleteShoppingListByUserId(userId, shoppingListId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes changes made to a shopping list by a user.
     *
     * @param userId         the ID of the user
     * @param shoppingListId the ID of the shopping list
     * @return a ResponseEntity indicating the success or failure of the operation and an HTTP status code
     */
    @GetMapping(value = "/removeChanges")
    public ResponseEntity<Boolean> removeChanges(
            @RequestParam("userId") int userId,
            @RequestParam("shoppingListId") int shoppingListId
    ){
        try{
            shoppingListService.removeChanges(userId, shoppingListId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
