package com.paccy.service.serviceimplimentation;

import com.paccy.model.Category;
import com.paccy.model.Food;
import com.paccy.model.Restaurant;
import com.paccy.repository.FoodRepository;
import com.paccy.request.CreateFoodRequest;
import com.paccy.service.FoodService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

   @Autowired
   private FoodRepository foodRepository;
    @Override
    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant) {
        Food food = new Food();
        food.setFoodCategory(category);
        food.setRestaurant(restaurant);
        food.setDescription(req.getDiscription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice(req.getPrice());
        food.setIngredientsItems(req.getIngredients());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegetarian());

        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    public boolean deleteFood(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setRestaurant(null);

        foodRepository.save(food);

        return true;
    }

    @Override
    public List<Food> getAllRestaurantFoods(Long restaurantId, boolean isVegetarian, boolean isNonVeg, boolean isSeasonal, String foodCategory) {
        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);

        if (isVegetarian && isSeasonal) {
            foods = foods.stream()
                    .filter(food -> food.isVegetarian() && food.isSeasonal())
                    .collect(Collectors.toList());
        } else if (isVegetarian) {
            foods = foods.stream()
                    .filter(Food::isVegetarian)
                    .collect(Collectors.toList());
        }

        if (isNonVeg && isSeasonal) {
            foods = foods.stream()
                    .filter(food -> !food.isVegetarian() && food.isSeasonal())
                    .collect(Collectors.toList());
        } else if (isNonVeg) {
            foods = foods.stream()
                    .filter(food -> !food.isVegetarian())
                    .collect(Collectors.toList());
        }

        if (foodCategory != null && !foodCategory.trim().isEmpty()) {
            foods = foods.stream()
                    .filter(food -> food.getFoodCategory() != null && food.getFoodCategory().getName().equals(foodCategory))
                    .collect(Collectors.toList());
        }

        return foods;
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long id) throws Exception {
        Optional<Food> foodOptional = foodRepository.findById(id);

        if (foodOptional.isEmpty()) {
            throw new BadRequestException("Food Does not Exist");
        }

        return foodOptional.get();
    }

    @Override
    public Food updateAvailability(Long foodId) throws Exception {
        Food food = findFoodById(foodId);
        food.setAvailable(!food.isAvailable());

        return foodRepository.save(food);
    }
}
