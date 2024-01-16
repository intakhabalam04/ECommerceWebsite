package com.intakhab.ecommercewebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    private String orderDate;
    private String status;
    private double totalAmount;
    @OneToOne
    private Cart cart;
    @ManyToOne
    private User user;
    @OneToOne(mappedBy = "order")
    private Payment payment;
}
