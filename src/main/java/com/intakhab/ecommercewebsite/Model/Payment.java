package com.intakhab.ecommercewebsite.Model;

import com.intakhab.ecommercewebsite.Enum.PaymentStatus;
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
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @OneToOne
    @JoinColumn(name = "orderId")
    private Order order;
}
