package com.paccy.controller;

import com.paccy.model.Food;
import com.paccy.model.Restaurant;
import com.paccy.model.User;
import com.paccy.request.CreateFoodRequest;
import com.paccy.response.MessageResponse;
import com.paccy.service.FoodService;
import com.paccy.service.RestaurantService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController@RequestMapping("/api/admin/food")
public class AdminFoodController {
    @Autowired
    private FoodService foodService;
    @Autowired
    private UserService userService;
    @Autowired

    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity <Food>  createFood(
            @RequestBody CreateFoodRequest foodDTO,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
             User user=userService.userByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findById(foodDTO.getRestaurantId());

        Food food = foodService.createFood(foodDTO, foodDTO.getCategory(), restaurant);



        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(
            @PathVariable Long id,
    @RequestHeader("Authorization") String jwt) throws Exception {
         User user =userService.userByJwtToken(jwt);
         foodService.deleteFood(id);

        MessageResponse response=new MessageResponse();
        response.setMessage("Food Deleted Successfully");


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food>updateFoodAvailabilityStatus
               (@PathVariable Long id,
                @RequestHeader("Authorization") String jwt) throws Exception {
            User user =userService.userByJwtToken(jwt);
            Food food= foodService.updateAvailability(id);

            MessageResponse response=new MessageResponse();
            response.setMessage("Food updated Successfully");


            return new ResponseEntity<>(food, HttpStatus.CREATED);
        }

}

