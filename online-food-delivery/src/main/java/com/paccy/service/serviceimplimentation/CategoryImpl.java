package com.paccy.service.serviceimplimentation;

import com.paccy.model.Category;
import com.paccy.model.Restaurant;
import com.paccy.repository.CategoryRepository;
import com.paccy.service.CategoryService;
import com.paccy.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantService restaurantService;
    @Override
    public Category createCategory(String name, Long UserId) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(UserId);

        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);

        return categoryRepository.save(category);

    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long id) throws Exception {
       Restaurant restaurant=restaurantService.getRestaurantByUserId(id);
        return categoryRepository.findByRestaurantId(restaurant.getId());
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new Exception("Category Not Found");
        }

        return optionalCategory.get();
    }
}
