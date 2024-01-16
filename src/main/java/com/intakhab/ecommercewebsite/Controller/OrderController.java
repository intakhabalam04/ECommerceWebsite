package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.OrderService;
import com.intakhab.ecommercewebsite.Service.UserService;
import com.intakhab.ecommercewebsite.ServiceImpl.CartServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final SecurityConfig securityConfig;

    public OrderController(OrderService orderService, SecurityConfig securityConfig) {
        this.orderService = orderService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/doCheckout")
    private ModelAndView submitCheckOutPage() {
        User user = securityConfig.getCurrentUser();
        orderService.checkOrder(user);
        return new ModelAndView("/user/cart");
    }
}
