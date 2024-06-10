package com.intakhab.ecommercewebsite.Model;

import com.intakhab.ecommercewebsite.Enum.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true,updatable = false,nullable = false)
    private String emailId;
    @Column(unique = true,nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String address;
    private String token;
    private long tokenExpiryTime;
    private int pincode;
    private String emailUserPhone;
    @Lob
    @Column(length = 1048576)
    private byte[] profileImg;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Cart> cartList;
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Order> orderList;
    private LocalDate registerDate;
}
