package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.CartService;
import com.intakhab.ecommercewebsite.Service.OrderService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CartController {

    @Value("${checkoutView}")
    private String checkoutView;

    private final CartService cartService;
    private final SecurityConfig securityConfig;
    private final OrderService orderService;

    public CartController(CartService cartService, SecurityConfig securityConfig, OrderService orderService) {
        this.cartService = cartService;
        this.securityConfig = securityConfig;
        this.orderService = orderService;
    }
    @GetMapping("/addToCart/{productId}")
    public ModelAndView addToCart(@PathVariable("productId") UUID productId) {
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
        Map<String, Object> model = new HashMap<>();
        User user = securityConfig.getCurrentUser();
        List<Product> productLists = cartService.getFindCartProducts(user);
        double cartPrice = productLists.stream()
                .mapToDouble(product -> product.getPrice() - (product.getPrice() * product.getDiscount()) / 100)
                .sum();
        model.put("cartprice", cartPrice);
        model.put("orderId", orderService.generateOrderId());
        return new ModelAndView(checkoutView, model);
    }

    @GetMapping("/validateCoupon")
    @ResponseBody
    public boolean validateCoupon(@RequestParam String couponCode) {

        return "Intakhab".equals(couponCode);
    }

}
