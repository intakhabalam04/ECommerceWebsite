package com.intakhab.ecommercewebsite.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;
    private LocalDateTime createdDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @OneToOne(mappedBy = "cart")
    private Order order;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> cartItemList;
    private double cartPrice;
}
