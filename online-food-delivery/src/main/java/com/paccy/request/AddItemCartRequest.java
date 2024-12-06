package com.paccy.request;

import lombok.Data;

import java.util.List;
@Data
public class AddItemCartRequest {


    private Long foodId;
    private int quantity;
    private List<String> ingredients;
    private Long cartItemId;
}
