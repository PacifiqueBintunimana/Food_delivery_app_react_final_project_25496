package com.paccy.service.serviceimplimentation;

import com.paccy.model.Cart;
import com.paccy.model.CartItem;
import com.paccy.model.Food;
import com.paccy.model.User;
import com.paccy.repository.CartItemRepository;
import com.paccy.repository.CartRepository;
import com.paccy.repository.FoodRepository;
import com.paccy.request.AddItemCartRequest;
import com.paccy.service.CartService;
import com.paccy.service.FoodService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public CartItem addItemToCart(AddItemCartRequest cartItemDTO, String jwt) throws Exception {
        User user = userService.userByJwtToken(jwt);
        Food food = foodService.findFoodById(cartItemDTO.getFoodId());

        Cart cart = cartRepository.findByCustomerId(user.getId());

        for (CartItem cartItem : cart.getCartItems()) {
            if ( cartItem.getFood().equals(food)){
                int newQuantity = cartItem.getQuantity() + cartItemDTO.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(cartItemDTO.getQuantity());
        newCartItem.setIngredients(cartItemDTO.getIngredients());
        newCartItem.setTotalPrice(cartItemDTO.getQuantity() * food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        cart.getCartItems().add(savedCartItem);

        return savedCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        if (cartItem.isEmpty()) {
            throw new Exception("Cart Item Not Found");
        }

        CartItem cartItem1 = cartItem.get();
        cartItem1.setQuantity(quantity);

        cartItem1.setTotalPrice(cartItem1.getFood().getPrice() * quantity);

        return cartItemRepository.save(cartItem1);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user = userService.userByJwtToken(jwt);

        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isEmpty()) {
            throw new Exception("Cart Item Not Found");
        }

        CartItem item = cartItemOptional.get();

        cart.getCartItems().remove(item);

        return cartRepository.save(cart);
    }

    @Override
    public Long calculateTotal(Cart cart) throws Exception {
        long total = 0L;

        for (CartItem cartItem : cart.getCartItems()) {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }

        return total;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {

        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (optionalCart.isEmpty()) {
            throw new Exception("Cart Not Found With Id " + id);
        }

        return optionalCart.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        //User user=userService.userByJwtToken(jwt);
       Cart cart= cartRepository.findByCustomerId(userId);
        cart.setTotal(calculateTotal(cart));

         return cart;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
       // User user=userService.userByJwtToken(jwt);
        Cart cart = findCartByUserId(userId);
        cart.getCartItems().clear();

        return cartRepository.save(cart);
    }
}
