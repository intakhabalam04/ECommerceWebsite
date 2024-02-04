package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Model.Category;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.CartService;
import com.intakhab.ecommercewebsite.Service.CategoryService;
import com.intakhab.ecommercewebsite.Service.ProductService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${userHomeView}")
    private String userHomeView;

    @Value("${userProductView}")
    private String productView;

    @Value("${cartView}")
    private String cartView;

    @Value("${historyView}")
    private String historyView;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CartService cartService;
    private final SecurityConfig securityConfig;

    public UserController(CategoryService categoryService, ProductService productService, CartService cartService, SecurityConfig securityConfig) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.cartService = cartService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        User user = securityConfig.getCurrentUser();
        model.addAttribute("user", user);
        return userHomeView;
    }

    @GetMapping("/product")
    public ModelAndView getProductPage() {
        Map<String, Object> model = new HashMap<>();

        List<Category> categoryList = categoryService.getAllCategories();
        List<Product> productList = productService.getAllProducts();
        model.put("categories", categoryList);
        model.put("products", productList);
        model.put("user", securityConfig.getCurrentUser());
        return new ModelAndView(productView, model);
    }

    @GetMapping("/cart")
    public ModelAndView getCartPage() {
        Map<String, Object> model = new HashMap<>();
        User user = securityConfig.getCurrentUser();
        List<Product> productLists = cartService.getFindCartProducts(user);

        double cartPrice = productLists.stream()
                .mapToDouble(product -> product.getPrice() - (product.getPrice() * product.getDiscount()) / 100)
                .sum();

        model.put("productLists", productLists);
        model.put("cartprice", cartPrice);
        model.put("user", securityConfig.getCurrentUser());

        return new ModelAndView(cartView, model);
    }

    @GetMapping("/history")
    public ModelAndView getOrderHistoryPage() {
        Map<String, Object> model = new HashMap<>();

        User user = securityConfig.getCurrentUser();
        List<Order> orderList = user.getOrderList();

        orderList.sort(Comparator.comparing(Order::getOrderId, Comparator.reverseOrder()));

        model.put("orderlist", orderList);
        model.put("user", securityConfig.getCurrentUser());

        return new ModelAndView(historyView, model);
    }

}
