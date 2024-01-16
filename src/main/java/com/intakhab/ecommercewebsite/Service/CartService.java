package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;

import java.util.List;
import java.util.UUID;

public interface CartService {
    boolean addToCart(User user, UUID productId);

    List<Product> getFindCartProducts(User user);

    void removeFromCart(User user, UUID productId);

}
