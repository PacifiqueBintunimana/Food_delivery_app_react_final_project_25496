package com.paccy.controller;

import com.paccy.model.IngredientCategory;
import com.paccy.model.IngredientsItem;
import com.paccy.request.IngredientsCategoryRequest;
import com.paccy.request.IngredientsRequest;
import com.paccy.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientsController {
    @Autowired
    private IngredientsService ingredientsService;

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestBody IngredientsCategoryRequest ingredientCategoryDTO) throws Exception {

        IngredientCategory ingredientCategory = ingredientsService.createIngredientCategory(ingredientCategoryDTO.getName(), ingredientCategoryDTO.getRestaurantId());


        return new ResponseEntity<>(ingredientCategory, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<IngredientsItem> createIngredientItem(@RequestBody IngredientsRequest ingredient) throws Exception {

        IngredientsItem ingredientItem = ingredientsService.createIngredientItem(ingredient.getRestaurantId(), ingredient.getName(), ingredient.getCategoryId());


        return new ResponseEntity<>(ingredientItem, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<IngredientsItem> updateIngredientStock(@PathVariable Long id) throws Exception {

        IngredientsItem ingredientItem = ingredientsService.updateStock(id);


        return new ResponseEntity<>(ingredientItem, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientsItem>> restaurantIngredient(@PathVariable Long id) throws Exception {

        List<IngredientsItem> ingredientItem = ingredientsService.findRestaurantIngredients(id);


        return new ResponseEntity<>(ingredientItem, HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientCategory

                        (@PathVariable Long id) throws Exception {

            List<IngredientCategory> ingredientItem = ingredientsService.findIngredientCategoryByRestaurantId(id);


            return new ResponseEntity<>(ingredientItem, HttpStatus.CREATED);
        }

    }
