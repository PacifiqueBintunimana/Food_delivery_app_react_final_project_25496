package com.paccy.service;

import com.paccy.model.Category;
import com.paccy.model.Food;
import com.paccy.model.Restaurant;
import com.paccy.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

    boolean deleteFood(Long foodId) throws Exception;

    public List<Food> getAllRestaurantFoods(Long restaurantId, boolean isVegetarian, boolean isNonVeg, boolean isSeasonal, String foodCategory);

    public List<Food> searchFood(String keyword);

    public Food findFoodById(Long id) throws Exception;

    public Food updateAvailability(Long foodId) throws Exception;
}
