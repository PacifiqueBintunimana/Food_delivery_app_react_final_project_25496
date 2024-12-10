package com.paccy.request;

import com.paccy.model.Category;
import com.paccy.model.IngredientsItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateFoodRequest {
    private String name;
    private String discription;
    private  Long Price;
    private Category category;
     private List<String> images;
     private Long restaurantId;
    private boolean vegetarian;

    private boolean seasonal;
    private List<IngredientsItem> ingredients;


}
