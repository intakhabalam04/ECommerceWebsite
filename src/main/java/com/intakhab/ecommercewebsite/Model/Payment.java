package com.intakhab.ecommercewebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private double paymentId;
    private LocalDateTime paymentDate;
    private double amount;
    private String paymentMethod;
    private String status;
    @OneToOne
    @JoinColumn(name = "orderId")
    private Order order;
}
