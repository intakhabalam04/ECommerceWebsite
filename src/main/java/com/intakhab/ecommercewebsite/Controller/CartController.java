package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.CartService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CartController {

    private final CartService cartService;
    private final SecurityConfig securityConfig;

    public CartController(CartService cartService, SecurityConfig securityConfig) {
        this.cartService = cartService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/addToCart/{productId}")
    public ModelAndView addToCart(@PathVariable("productId") UUID productId) {
        String viewName = "/user/product";
        Map<String, Object> model = new HashMap<>();

        User user = securityConfig.getCurrentUser();
        boolean isAdded = cartService.addToCart(user, productId);

        return new ModelAndView(new RedirectView("/user/product"));
    }

    @GetMapping("/removeFromCart/{productId}")
    public ModelAndView removeFromCart(@PathVariable("productId") UUID productId) {
        User user = securityConfig.getCurrentUser();
        cartService.removeFromCart(user, productId);
        return new ModelAndView(new RedirectView("/user/cart"));
    }

    @GetMapping("/checkout")
    public ModelAndView getCheckoutPage() {
        String viewName = "User/checkout";
        Map<String, Object> model = new HashMap<>();
        User user = securityConfig.getCurrentUser();
        List<Product> productLists = cartService.getFindCartProducts(user);
        double cartPrice = 0;
        for (Product product : productLists) {
            cartPrice += product.getPrice();
        }
        model.put("cartprice", cartPrice);
        return new ModelAndView(viewName, model);
    }

}
