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

@RestController
@RequestMapping("/api/shoppingList")
public class ShoppingListController {
    //private final Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

    @Autowired
    IShoppingListService shoppingListService;

    @Autowired
    IUserService userService;

    @GetMapping(produces = "application/json")
    private List<ShoppingListDTO> getShoppingLists() {
        List<ShoppingList> shoppingLists = shoppingListService.findAll();
        return shoppingLists.stream().map(ShoppingListDTO::new)
                .collect(Collectors.toList());
    }

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
