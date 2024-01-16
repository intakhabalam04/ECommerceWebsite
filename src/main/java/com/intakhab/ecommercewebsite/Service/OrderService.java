package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Model.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    void checkOrder(User user);

    List<Order> findAllOrders();

    List<Order> findAllOrders(UUID id);

    Order getOrderById(UUID orderId);
}
