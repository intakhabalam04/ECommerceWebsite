package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Enum.OrderStatus;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Repository.OrderRepo;
import com.intakhab.ecommercewebsite.Service.OrderScheduledService;
import com.intakhab.ecommercewebsite.Service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderScheduledServiceImpl implements OrderScheduledService {

    @Value("${order.date.time.format}")
    private String dateTimeFormatPattern;

    private final OrderService orderService;
    private final OrderRepo orderRepo;

    public OrderScheduledServiceImpl(OrderService orderService, OrderRepo orderRepo) {
        this.orderService = orderService;
        this.orderRepo = orderRepo;
    }


    @Scheduled(initialDelay = 10000, fixedRateString = "${order.scheduled.fixed.rate}")
    @Override
    public void orderChangeSchedule() {
        List<Order> orderList = orderService.findAllOrders();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatPattern);
        if (!orderList.isEmpty()) {
            for (Order order : orderList) {
                if (!order.getStatus().equals(OrderStatus.RETURNED) && !order.getStatus().equals(OrderStatus.CANCELLED)) {
                    changeStatus(order, formatter);
                }
            }
        }
    }

    @Override
    public void changeStatus(Order order, DateTimeFormatter formatter) {
        LocalDateTime orderDate = LocalDateTime.parse(order.getOrderDate(), formatter);
        LocalDateTime currentDate = LocalDateTime.now();

        Random random = new Random();
        long randomNum = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        order.setTrackingNo(randomNum);

        Duration duration = Duration.between(orderDate, currentDate);
        long noOfDays = duration.toDays();
        switch ((int) noOfDays) {
            case 0:
            case 1:
                order.setStatus(OrderStatus.PENDING);
                break;
            case 2:
            case 3:
                order.setStatus(OrderStatus.PROCESSING);
                break;
            case 4:
            case 5:
                order.setStatus(OrderStatus.SHIPPED);
                break;
            case 6:
                order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
                break;
            default:
                order.setStatus(OrderStatus.DELIVERED);
                break;
        }
        orderRepo.save(order);
    }
}
