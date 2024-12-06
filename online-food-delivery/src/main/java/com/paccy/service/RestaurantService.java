package com.paccy.service;

import com.paccy.dto.RestaurantDto;
import com.paccy.model.Restaurant;
import com.paccy.model.User;
import com.paccy.request.CreatRestaurantRequest;

import java.util.List;

public interface RestaurantService {
    public Restaurant createRestaurant(CreatRestaurantRequest req, User user);

    public Restaurant updateRestaurant(Long restaurantId, CreatRestaurantRequest updateRestaurant)throws Exception;

    public void deleteRestaurant(Long restaurantId)throws Exception;

    public List<Restaurant> getAllRestaurant();

    public List<Restaurant> searchRestaurant(String keyword);

    public Restaurant updateStatus(Long id) throws Exception;

    public Restaurant findById(Long id) throws Exception;

    public RestaurantDto addToFavorites(Long restaurantId, User user) throws Exception;


    public Restaurant getRestaurantByUserId(Long id) throws Exception;
}
