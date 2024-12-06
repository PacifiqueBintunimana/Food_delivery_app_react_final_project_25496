package com.paccy.request;

import lombok.Data;

@Data
public class IngredientsRequest {
    private String name;
    private Long categoryId;
    private Long restaurantId;
}
