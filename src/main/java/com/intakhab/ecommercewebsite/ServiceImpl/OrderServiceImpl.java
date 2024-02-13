package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Enum.OrderStatus;
import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.OrderRepo;
import com.intakhab.ecommercewebsite.Repository.ProductRepo;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import com.intakhab.ecommercewebsite.Service.DateAndTimeService;
import com.intakhab.ecommercewebsite.Service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${order.date.time.format}")
    private String dateTimeFormatPattern;

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final DateAndTimeService dateAndTimeService;

    public OrderServiceImpl(OrderRepo orderRepo, UserRepo userRepo, ProductRepo productRepo, DateAndTimeService dateAndTimeService) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.dateAndTimeService = dateAndTimeService;
    }

    @Override
    public void checkOrder(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatPattern);

        Order order = createOrder(user);
        Cart lastCart = getLastCart(user);

        if (lastCart != null && !lastCart.getProductList().isEmpty()) {
            processOrderItems(order, lastCart);
        } else {
            return;
        }

        setOrderDetailsAndSave(order, formatter);
        createNewCartForUser(user);
    }

    private Order createOrder(User user) {
        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setUser(user);
        return order;
    }

    private Cart getLastCart(User user) {
        List<Cart> userCarts = user.getCart();
        if (!userCarts.isEmpty()) {
            userCarts.sort(Comparator.comparing(Cart::getCreatedDate));
            return userCarts.get(userCarts.size() - 1);
        }
        return null;
    }

    private void processOrderItems(Order order, Cart cart) {
        double total = 0;
        for (Product product : cart.getProductList()) {
            if (product.getStockQuantity() == 0) {
                return;
            }
            product.setStockQuantity(product.getStockQuantity() - 1);
            productRepo.save(product);
            total += product.getPrice() - (product.getPrice() * product.getDiscount()) / 100;
        }
        order.setCart(cart);
        order.setTotalAmount(total);
    }

    private void setOrderDetailsAndSave(Order order, DateTimeFormatter formatter) {
        order.setOrderDate(LocalDateTime.now().format(formatter));
        order.setStatus(OrderStatus.PENDING);
        order.setExpectedDeliveryDate(LocalDate.now().plusDays(7));
        order.setTrackingNo(generateTrackingNo());
        orderRepo.save(order);
    }

    private void createNewCartForUser(User user) {
        List<Cart> cartList = user.getCart();
        Cart newCart = new Cart();
        cartList.add(newCart);
        newCart.setUser(user);
        newCart.setCreatedDate(LocalDateTime.now());
        user.setCart(cartList);
        userRepo.save(user);
    }

    private long generateTrackingNo() {
        Random random = new Random();
        return (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;

    }


    @Override
    public List<Order> findAllOrders() {
        return orderRepo.findAll(Sort.by("orderId").descending());
    }

    // Retrieve all orders for a specific user
    @Override
    public List<Order> findAllOrders(UUID id) {
        return findAllOrders().stream().filter(order -> order.getUser().getId().equals(id)).collect(Collectors.toList());
    }

    // Retrieve an order by orderId
    @Override
    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElseThrow(() -> new NoSuchElementException("Order not found"));
    }

    // Generate a new orderId
    @Override
    public Long generateOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = LocalDateTime.now().format(formatter);
        long orderDate = Long.parseLong(currentDate) * 10000;

        List<Order> allOrders = orderRepo.findAll(Sort.by("orderId").descending());

        return allOrders.isEmpty() ? orderDate + 1 : getNextOrderId(allOrders.get(0), orderDate);
    }

    // Helper method to get the next orderId based on the last order
    private Long getNextOrderId(Order lastOrder, long orderDate) {
        return (lastOrder.getOrderId() / 10000) * 10000 == orderDate ? lastOrder.getOrderId() + 1 : orderDate + 1;
    }

    // Cancel an order and update stock quantities
    @Override
    public void cancelOrder(Long orderId) {
        orderRepo.findById(orderId).ifPresent(order -> {
            order.getCart().getProductList().forEach(product -> {
                product.setStockQuantity(product.getStockQuantity() + 1);
                productRepo.save(product);
            });

            order.setStatus(OrderStatus.CANCELLED);
            orderRepo.save(order);
        });
    }


    // Retrieve all orders
    @Override
    public List<Order> getTotalOrders() {
        return orderRepo.findAll();
    }

    // Retrieve all cancelled orders
    @Override
    public List<Order> getTotalCancelledOrders() {
        return getTotalOrders().stream()
                .filter(order -> order.getStatus().equals(OrderStatus.CANCELLED))
                .collect(Collectors.toList());
    }

    // Retrieve all processing orders
    @Override
    public List<Order> getTotalProcessingOrders() {
        return getTotalOrders().stream().filter(order -> order.getStatus().equals(OrderStatus.PROCESSING)).collect(Collectors.toList());
    }

    // Retrieve all delivered orders
    @Override
    public List<Order> getTotalDeliveredOrders() {
        return getTotalOrders().stream().filter(order -> order.getStatus().equals(OrderStatus.DELIVERED)).collect(Collectors.toList());
    }

    // Retrieve all returned orders
    @Override
    public List<Order> getTotalReturnedOrders() {
        return getTotalOrders().stream().filter(order -> order.getStatus().equals(OrderStatus.RETURNED)).collect(Collectors.toList());
    }

    // Generate reports on the number of orders per date
    @Override
    public Map<String, Long> generateReports() {
        return getTotalOrders().stream().collect(Collectors.groupingBy(order -> dateAndTimeService.extractDateFromOrderDate(order.getOrderDate()), Collectors.counting()));
    }

    // Update the status of an order
    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepo.save(order);
    }

    // Process a return order and update stock quantities
    @Override
    public void returnOrder(Long orderId) {
        orderRepo.findById(orderId).ifPresent(order -> {
            order.getCart().getProductList().forEach(product -> {
                product.setStockQuantity(product.getStockQuantity() + 1);
                productRepo.save(product);
            });
            order.setStatus(OrderStatus.RETURNED);
            orderRepo.save(order);
        });
    }
}
