package com.intakhab.ecommercewebsite.Service;

import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.CartItem;
import com.intakhab.ecommercewebsite.Model.User;

import java.util.List;
import java.util.UUID;

public interface CartService {
    boolean addToCart(User user, UUID productId);

    List<CartItem> getFindCartProducts(User user);

    void removeFromCart(User user, Long cartItemId);

    double updateCartItemQuantity(Long cartItemId,String type);

    Cart getLastCart(User user);

    void updateCartPrice(User user);

    double getCartPrice(User user);
}
