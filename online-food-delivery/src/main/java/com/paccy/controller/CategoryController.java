package com.paccy.controller;

import com.paccy.model.Category;
import com.paccy.model.Food;
import com.paccy.model.Restaurant;
import com.paccy.model.User;
import com.paccy.request.CreateFoodRequest;
import com.paccy.service.CategoryService;
import com.paccy.service.FoodService;
import com.paccy.service.RestaurantService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired

    private RestaurantService restaurantService;

    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.userByJwtToken(jwt);

        Category creatCategory = categoryService.createCategory(category.getName(), user.getId());



        return new ResponseEntity<>(creatCategory, HttpStatus.CREATED);
    }


    @GetMapping("/category/restaurant")
    public ResponseEntity<List<Category>> getRestaurantCategory(
                                                                @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User response =userService.userByJwtToken(jwt);
        List<Category> categories = categoryService.findCategoryByRestaurantId(response.getId());



        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

  /*  @GetMapping("/category/{id}")
    public ResponseEntity<ResponseDTO<Category>> findByCategoryId(@PathVariable Long id) throws Exception {

        ResponseDTO<Category> response = new ResponseDTO<>();
        Category category = categoryService.findCategoryById(id);

        response.setPayload(category);
        response.setMessage("Success");
        response.setHttpStatus(HttpStatus.OK);
        response.setCode("200");

        return new ResponseEntity<>(response, response.getHttpStatus());
    }*/
}
