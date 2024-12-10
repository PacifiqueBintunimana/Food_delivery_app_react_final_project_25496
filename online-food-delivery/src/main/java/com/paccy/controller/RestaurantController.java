package com.paccy.controller;

import com.paccy.dto.RestaurantDto;
import com.paccy.model.Restaurant;
import com.paccy.model.User;
import com.paccy.request.CreatRestaurantRequest;
import com.paccy.response.MessageResponse;
import com.paccy.service.RestaurantService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>>SearchRestaurant(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String keyword
    ) throws Exception {


        User user = userService.userByJwtToken(jwt);
        List<Restaurant> restaurant = restaurantService.searchRestaurant(keyword);



        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<Restaurant>>getAllRestaurant(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {


        User user = userService.userByJwtToken(jwt);
        List<Restaurant> restaurant = restaurantService.getAllRestaurant();



        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant>findById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {


        User user = userService.userByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findById(id);



        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    @PutMapping("/{id}/add-favorites")
    public ResponseEntity<RestaurantDto>addtoFavorites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {


        User user = userService.userByJwtToken(jwt);
        RestaurantDto restaurant = restaurantService.addToFavorites(id, user);



        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
