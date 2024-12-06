package com.paccy.service;

import com.paccy.model.Order;
import com.paccy.model.User;
import com.paccy.request.OrderRequest;

import java.util.List;

public interface OrderService {
    public Order createOrder(OrderRequest orderDTO, User user) throws Exception;

    public Order updateOrder(Long orderId, String orderStatus) throws Exception;

    public void canselOrder(Long orderId) throws Exception;

    public List<Order> getUserOrders(Long userId) throws Exception ;

    public List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception;

    public Order findOrderById(Long orderId) throws Exception;

}
