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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

    @Override
    public boolean addToCart(User user, UUID productId) {
        Product product = productService.getProductById(productId);

        List<Cart> userCarts = user.getCart();
        Cart cart;

        if (userCarts.isEmpty()) {
            // If the user has no carts, create a new one
            cart = new Cart();
            cart.setUser(user);
            cart.setCreatedDate(LocalDateTime.now()); // Set the createdDate
            userCarts.add(cart);
        } else {
            // Sort carts based on creation time in ascending order
            userCarts.sort(Comparator.comparing(Cart::getCreatedDate));
            // Use the latest cart for the user
            cart = userCarts.get(userCarts.size() - 1);
        }

        List<Product> productList = cart.getProductList();

        if (productList == null) {
            productList = new ArrayList<>();
            cart.setProductList(productList);
        }

        for (Product product1 : productList) {
            if (product1.getProductId().equals(product.getProductId())) {
                return false;
            }
        }

        productList.add(product);
        cartRepo.save(cart);

        // Update product stock quantity
        product.setStockQuantity(product.getStockQuantity() - 1);
        productRepo.save(product);

        return true;
    }




    @Override
    public List<Product> getFindCartProducts(User user) {
        List<Cart> userCarts = user.getCart();
        userCarts.sort(Comparator.comparing(Cart::getCreatedDate));
        Cart lastCart = userCarts.get(userCarts.size() - 1);
        return lastCart.getProductList();
    }

    @Override
    public void removeFromCart(User user, UUID productId) {
        Cart cart = user.getCart().get(user.getCart().size()-1);
        List<Product> productList = cart.getProductList();
        productList.removeIf(product -> product.getProductId().equals(productId));
        cart.setProductList(productList);
        cartRepo.save(cart);
    }
}
