package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.CartItem;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.CartItemRepo;
import com.intakhab.ecommercewebsite.Repository.CartRepo;
import com.intakhab.ecommercewebsite.Repository.OrderRepo;
import com.intakhab.ecommercewebsite.Repository.ProductRepo;
import com.intakhab.ecommercewebsite.Service.CartService;
import com.intakhab.ecommercewebsite.Service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service

public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final ProductService productService;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final CartItemRepo cartItemRepo;


    public CartServiceImpl(CartRepo cartRepo, ProductService productService, ProductRepo productRepo, OrderRepo orderRepo, CartItemRepo cartItemRepo) {
        this.cartRepo = cartRepo;
        this.productService = productService;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.cartItemRepo = cartItemRepo;
    }

    // Method to add a product to the user's cart
    @Override
    public boolean addToCart(User user, UUID productId) {

        Product product = productService.getProductById(productId);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setSubTotal(product.getPrice());

        List<Cart> userCarts = user.getCartList();
        Cart cart;

        // Check if the user has any carts
        if (userCarts.isEmpty()) {
            // If no cart exists, create a new one
            cart = new Cart();
            cart.setUser(user);
            cart.setCreatedDate(LocalDateTime.now());
            userCarts.add(cart);
        } else {
            // If carts exist, sort them and get the latest cart
            cart = getLastCart(user);
        }

        List<CartItem> cartItems = cart.getCartItemList();

        if (cartItems == null) {
            cartItems = new ArrayList<>();
            cart.setCartItemList(cartItems);
        }

        // Check if the product is already in the cart
        for (CartItem cartItem1 : cartItems) {
            if (cartItem1.getProduct().equals(cartItem.getProduct())) {
                return false; // Product is already in the cart
            }
        }

        // Add the product to the cart and update repositories
        cartItems.add(cartItem);
        cartRepo.save(cart);
        cartItemRepo.save(cartItem);

        return true; // Product added successfully
    }

    // Method to get products in the user's current cart
    @Override
    public List<CartItem> getFindCartProducts(User user) {
        List<Cart> userCarts = user.getCartList();

        // Check if the user has any carts
        if (userCarts.isEmpty()) {
            return Collections.emptyList(); // No carts for the user
        }
        userCarts.sort(Comparator.comparing(Cart::getCreatedDate));

        // Get the latest cart and filter out products with zero stock
        if (!userCarts.isEmpty()) {
            Cart lastCart = userCarts.get(userCarts.size() - 1);

            List<CartItem> lastCartItems = lastCart.getCartItemList();
            lastCartItems.removeIf(cartItem -> cartItem.getProduct().getStockQuantity() == 0);
            lastCart.setCartItemList(lastCartItems);
            cartRepo.save(lastCart);

            return lastCart.getCartItemList();
        } else {
            return Collections.emptyList(); // No carts for the user
        }
    }

    // Method to remove a product from the user's cart
    @Override
    public void removeFromCart(User user, Long cartItemId) {
        List<Cart> userCarts = user.getCartList();
        userCarts.sort(Comparator.comparing(Cart::getCreatedDate));
        Cart lastCart = userCarts.get(userCarts.size() - 1);

        List<CartItem> cartItems = lastCart.getCartItemList();
        cartItems.removeIf(cartItem -> cartItem.getId().equals(cartItemId));
        lastCart.setCartItemList(cartItems);

        CartItem cartItem = cartItemRepo.findById(cartItemId).get();

        cartRepo.save(lastCart);
    }

    @Override
    public double updateCartItemQuantity(Long cartItemId, String type) {
        CartItem cartItem = cartItemRepo.findById(cartItemId).get();
        int quantity = cartItem.getQuantity();
        if (type.equals("inc")) {
            quantity++;
        } else {
            quantity--;
        }
        cartItem.setSubTotal(quantity * cartItem.getProduct().getPrice());
        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);
        return cartItem.getSubTotal();
    }

    @Override
    public Cart getLastCart(User user) {
        List<Cart> cartList = user.getCartList();
        cartList.sort(Comparator.comparing((Cart::getCreatedDate)));
        return cartList.get(cartList.size() - 1);
    }

    @Override
    public void updateCartPrice(User user) {
        Cart lastCart = getLastCart(user);
        List<CartItem> cartItems = lastCart.getCartItemList();
        lastCart.setCartPrice(cartItems.stream()
                .mapToDouble(CartItem::getSubTotal).sum());
        cartRepo.save(lastCart);

    }

    @Override
    public double getCartPrice(User user) {
        Cart lastCart = getLastCart(user);
        return lastCart.getCartPrice();
    }
}
