package com.paccy.controller;

import com.paccy.model.Order;
import com.paccy.model.User;
import com.paccy.request.OrderRequest;
import com.paccy.service.OrderService;
import com.paccy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/admin")
public class AdminOrdercontroller {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @GetMapping("/order/restaurant/{id}")
    public ResponseEntity<List<Order>> getOrderHistory(
            @PathVariable Long id,
            @RequestParam(required = false)String order_status,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.userByJwtToken(jwt);
        List <Order> order = orderService.getRestaurantOrders(id,order_status);


        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PutMapping("/order/{id}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @PathVariable String orderstatus,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.userByJwtToken(jwt);
        Order order = orderService.updateOrder(id,orderstatus);


        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
