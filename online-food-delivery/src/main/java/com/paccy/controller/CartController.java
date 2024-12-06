package com.paccy.controller;

import com.paccy.model.Cart;
import com.paccy.model.CartItem;
import com.paccy.model.User;
import com.paccy.request.AddItemCartRequest;
import com.paccy.request.UpdateCartItemRequest;
import com.paccy.service.CartService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody AddItemCartRequest cartItemDTO,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        CartItem cartItem = cartService.addItemToCart(cartItemDTO, jwt);


        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @GetMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest cartItemDTO,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        CartItem cartItem = cartService.updateCartItemQuantity(cartItemDTO.getCartItemId(), cartItemDTO.getQuantity());


        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeItemFromCart(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        Cart cartItem = cartService.removeItemFromCart(id, jwt);


        return new ResponseEntity<>(cartItem, HttpStatus.OK);


}
    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.userByJwtToken(jwt);

        Cart cart = cartService.clearCart(user.getId());

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.userByJwtToken(jwt);

        Cart cart = cartService.findCartByUserId(user.getId());

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

}
