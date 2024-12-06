package com.paccy.service;

import com.paccy.model.Cart;
import com.paccy.model.CartItem;
import com.paccy.request.AddItemCartRequest;

public interface CartService {


    public CartItem addItemToCart(AddItemCartRequest cartItemDTO, String jwt) throws Exception;

    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;

    public Long calculateTotal(Cart cart) throws Exception;

    public Cart findCartById(Long id) throws Exception;

    public Cart findCartByUserId(Long userId) throws Exception;

    public Cart clearCart(Long userId) throws Exception;

}
