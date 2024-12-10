package com.paccy.controller;


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

@RestController
@RequestMapping("/api/admin/restaurants")

public class AdminRestaurantController {


    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

        @PostMapping()
        public ResponseEntity<Restaurant> createRestaurant(
                @RequestBody CreatRestaurantRequest res,
                @RequestHeader("Authorization") String jwt
        ) throws Exception {


            User user = userService.userByJwtToken(jwt);
            Restaurant restaurant = restaurantService.createRestaurant(res, user);



            return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Restaurant> updateRestaurant(
                @RequestBody CreatRestaurantRequest res,
                @RequestHeader("Authorization") String jwt,
                @PathVariable Long id
        ) throws Exception {


            User user = userService.userByJwtToken(jwt);
            Restaurant restaurant = restaurantService.updateRestaurant(id, res);



            return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
        }
        @DeleteMapping("/{id}")
        public ResponseEntity<MessageResponse> deleteRestaurant(
                @RequestHeader("Authorization") String jwt,
                @PathVariable Long id
        ) throws Exception {


            User user = userService.userByJwtToken(jwt);
            restaurantService.deleteRestaurant(id);
            MessageResponse mes=new MessageResponse();
            mes.setMessage("restaurant deleted successfully");
            return new ResponseEntity<>(mes, HttpStatus.OK);
        }
    @PutMapping("/{id}/status")
    public ResponseEntity<Restaurant> updateRestaurantStatus(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {


        User user = userService.userByJwtToken(jwt);
        Restaurant restaurant=restaurantService.updateStatus(id);
        MessageResponse mes=new MessageResponse();
        mes.setMessage("restaurant deleted successfully");
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }


        @GetMapping("/user")
        public ResponseEntity<Restaurant> findRestaurantByOwner(@RequestHeader("Authorization") String jwt) throws Exception {

            User user = userService.userByJwtToken(jwt);

            Restaurant restaurant = restaurantService.getRestaurantByUserId(user.getId());



            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        }


}
