package com.intakhab.ecommercewebsite.Repository;

import com.intakhab.ecommercewebsite.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartItemRepo extends JpaRepository<CartItem,Long> {
}
