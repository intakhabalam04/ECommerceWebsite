package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Enum.OrderStatus;
import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Model.User;
import org.aspectj.weaver.ast.Or;
import org.eclipse.angus.mail.imap.protocol.BODYSTRUCTURE;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderService {
    void checkOrder(User user);

    List<Order> findAllOrders();

    List<Order> findAllOrders(UUID id);

    Order getOrderById(Long orderId);

    Long generateOrderId();


    void cancelOrder(Long orderId);

    List<Order> getTotalOrders();

    List<Order> getTotalCancelledOrders();

    List<Order> getTotalProcessingOrders();

    List<Order> getTotalDeliveredOrders();
    List<Order> getTotalReturnedOrders();
    Map<String, Long> generateReports();

    void updateOrderStatus(Long orderId, OrderStatus status);

    void returnOrder(Long orderId);
}
