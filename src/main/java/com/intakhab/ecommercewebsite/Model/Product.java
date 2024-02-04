package com.intakhab.ecommercewebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;
import java.util.UUID;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private String productName;
    private String productDescription;
    private double price;
    private int stockQuantity;
    @Lob
    @Column(length = 1048576)
    private byte[] productImg;
    private double discount;
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
