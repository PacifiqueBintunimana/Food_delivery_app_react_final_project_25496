package com.paccy.service.serviceimplimentation;

import com.paccy.model.IngredientCategory;
import com.paccy.model.IngredientsItem;
import com.paccy.model.Restaurant;
import com.paccy.repository.IngredientCategoryRepository;
import com.paccy.repository.IngredientsItemRepository;
import com.paccy.service.IngredientsService;
import com.paccy.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientsImpl implements IngredientsService {

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;

    @Autowired
    private IngredientsItemRepository ingredientsItemRepository;

    @Autowired
    private RestaurantService restaurantService;


        @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {

            Restaurant restaurant = restaurantService.findById(restaurantId);

            IngredientCategory ingredientCategory = new IngredientCategory();
            ingredientCategory.setRestaurant(restaurant);
            ingredientCategory.setName(name);

            return ingredientCategoryRepository.save(ingredientCategory);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {
        Optional<IngredientCategory> ingredientCategory = ingredientCategoryRepository.findById(id);

        if (ingredientCategory.isEmpty()) {
            throw new Exception("Ingredient Category Not Found");
        }

        return ingredientCategory.get();
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception {
            restaurantService.findById(id);

        return ingredientCategoryRepository.findByRestaurantId(id);

    }

    @Override
    public List<IngredientsItem> findRestaurantIngredients(Long id) {
        return ingredientsItemRepository.findByRestaurantId(id);
    }

    @Override
    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long ingredientCategoryId) throws Exception {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        IngredientCategory category = findIngredientCategoryById(ingredientCategoryId);

        IngredientsItem item = new IngredientsItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(category);

        IngredientsItem ingredientsItem = ingredientsItemRepository.save(item);
        category.getIngredientsItems().add(ingredientsItem);

        return ingredientsItem;
    }

    @Override
    public IngredientsItem updateStock(Long id) throws Exception {
        Optional<IngredientsItem> ingredientsItemOptional = ingredientsItemRepository.findById(id);

        if (ingredientsItemOptional.isEmpty()) {
            throw new Exception("Ingredient Not Found");
        }

        IngredientsItem ingredientsItem = ingredientsItemOptional.get();
        ingredientsItem.setInStoke(!ingredientsItem.isInStoke());

        return ingredientsItemRepository.save(ingredientsItem);
    }
}
