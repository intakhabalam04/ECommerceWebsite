package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Model.Category;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.CartService;
import com.intakhab.ecommercewebsite.Service.CategoryService;
import com.intakhab.ecommercewebsite.Service.ProductService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CartService cartService;
    private final SecurityConfig securityConfig;

    public UserController(UserService userService,
                          CategoryService categoryService,
                          ProductService productService,
                          CartService cartService,
                          SecurityConfig securityConfig
    ) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.cartService = cartService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        User user = securityConfig.getCurrentUser();
        model.addAttribute("user", user);
        return "User/home";
    }

    @GetMapping("/product")
    public ModelAndView getProductPage() {
        String viewName = "User/product";
        Map<String, Object> model = new HashMap<>();

        List<Category> categoryList = categoryService.getAllCategories();
        List<Product> productList = productService.getAllProducts();
        model.put("categories", categoryList);
        model.put("products", productList);
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/cart")
    public ModelAndView getCartPage() {
        String viewName = "User/cart";
        Map<String, Object> model = new HashMap<>();
        User user = securityConfig.getCurrentUser();
        List<Product> productLists = cartService.getFindCartProducts(user);
        System.out.println(productLists);
        double cartPrice = 0;
        for (Product product : productLists) {
            cartPrice += product.getPrice();
        }
        model.put("productLists", productLists);
        model.put("cartprice", cartPrice);

        return new ModelAndView(viewName, model);
    }
}
