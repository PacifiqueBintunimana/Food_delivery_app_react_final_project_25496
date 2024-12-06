package com.paccy.controller;

import com.paccy.model.CartItem;
import com.paccy.model.Order;
import com.paccy.model.User;
import com.paccy.request.AddItemCartRequest;
import com.paccy.request.OrderRequest;
import com.paccy.service.OrderService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest req,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
      User user=userService.userByJwtToken(jwt);
       Order order = orderService.createOrder(req, user);


        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(
        @RequestHeader("Authorization") String jwt
    ) throws Exception {
            User user=userService.userByJwtToken(jwt);
           List <Order> order = orderService.getUserOrders(user.getId());


            return new ResponseEntity<>(order, HttpStatus.OK);
        }
}
