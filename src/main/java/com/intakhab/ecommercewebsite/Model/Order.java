package com.intakhab.ecommercewebsite.Model;

import com.intakhab.ecommercewebsite.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "orders")
@Data
public class Order {
    @Id
    private Long orderId;
    private String orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private double totalAmount;
    @OneToOne
    private Cart cart;
    @ManyToOne
    private User user;
    @OneToOne(mappedBy = "order")
    private Payment payment;
}
