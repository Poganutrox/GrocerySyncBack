package com.miguelangel.supermarketDataCollector.controller;

import com.miguelangel.supermarketDataCollector.dto.CategoryDTO;
import com.miguelangel.supermarketDataCollector.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping
    private ResponseEntity<List<CategoryDTO>> getCategories(){
        List<CategoryDTO> categoryList;
        try{
            categoryList = categoryService.findAll().stream().map(CategoryDTO::new).toList();
            return new ResponseEntity<>(categoryList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
