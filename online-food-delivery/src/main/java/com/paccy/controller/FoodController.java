package com.paccy.controller;

import com.paccy.model.Food;
import com.paccy.model.Restaurant;
import com.paccy.model.User;
import com.paccy.request.CreateFoodRequest;
import com.paccy.service.FoodService;
import com.paccy.service.RestaurantService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired

    private RestaurantService restaurantService;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(
            @RequestParam String name,
            @RequestBody CreateFoodRequest foodDTO,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.userByJwtToken(jwt);

        List<Food> food = foodService.searchFood(name);



        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFood(
            @RequestParam boolean vegeterian,
            @RequestParam boolean seasonal,
            @RequestParam boolean nonveg,
            @RequestParam (required = false)String food_category,
            @PathVariable Long restaurantId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.userByJwtToken(jwt);

        List<Food> food = foodService.getAllRestaurantFoods(restaurantId,vegeterian,nonveg,seasonal,food_category);



        return new ResponseEntity<>(food, HttpStatus.OK);
    }

}
