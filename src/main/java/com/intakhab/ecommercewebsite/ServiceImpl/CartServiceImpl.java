package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.CartRepo;
import com.intakhab.ecommercewebsite.Repository.OrderRepo;
import com.intakhab.ecommercewebsite.Repository.ProductRepo;
import com.intakhab.ecommercewebsite.Service.CartService;
import com.intakhab.ecommercewebsite.Service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final ProductService productService;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public CartServiceImpl(CartRepo cartRepo, ProductService productService, ProductRepo productRepo, OrderRepo orderRepo) {
        this.cartRepo = cartRepo;
        this.productService = productService;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    // Method to add a product to the user's cart
    @Override
    public boolean addToCart(User user, UUID productId) {
        Product product = productService.getProductById(productId);

        List<Cart> userCarts = user.getCart();
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
            userCarts.sort(Comparator.comparing(Cart::getCreatedDate));
            cart = userCarts.get(userCarts.size() - 1);
        }

        List<Product> productList = cart.getProductList();

        if (productList == null) {
            productList = new ArrayList<>();
            cart.setProductList(productList);
        }

        // Check if the product is already in the cart
        for (Product product1 : productList) {
            if (product1.getProductId().equals(product.getProductId())) {
                return false; // Product is already in the cart
            }
        }

        // Add the product to the cart and update repositories
        productList.add(product);
        cartRepo.save(cart);
        productRepo.save(product);

        return true; // Product added successfully
    }

    // Method to get products in the user's current cart
    @Override
    public List<Product> getFindCartProducts(User user) {
        List<Cart> userCarts = user.getCart();

        // Check if the user has any carts
        if (userCarts.isEmpty()) {
            return Collections.emptyList(); // No carts for the user
        }

        userCarts.sort(Comparator.comparing(Cart::getCreatedDate));

        // Get the latest cart and filter out products with zero stock
        if (!userCarts.isEmpty()) {
            Cart lastCart = userCarts.get(userCarts.size() - 1);

            List<Product> lastCartProductList = lastCart.getProductList();
            lastCartProductList.removeIf(product -> product.getStockQuantity() == 0);
            lastCart.setProductList(lastCartProductList);
            cartRepo.save(lastCart);

            return lastCart.getProductList();
        } else {
            return Collections.emptyList(); // No carts for the user
        }
    }

    // Method to remove a product from the user's cart
    @Override
    public void removeFromCart(User user, UUID productId) {
        List<Cart> userCarts = user.getCart();
        userCarts.sort(Comparator.comparing(Cart::getCreatedDate));
        Cart lastCart = userCarts.get(userCarts.size() - 1);

        List<Product> productList = lastCart.getProductList();
        productList.removeIf(product -> product.getProductId().equals(productId));
        lastCart.setProductList(productList);
        cartRepo.save(lastCart);
    }
}
