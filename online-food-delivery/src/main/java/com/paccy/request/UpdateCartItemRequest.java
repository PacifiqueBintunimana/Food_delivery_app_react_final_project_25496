package com.paccy.request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private Long CartItemId;
    private int quantity;
}
