package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.OrderService;
import com.intakhab.ecommercewebsite.Service.UserService;
import com.intakhab.ecommercewebsite.ServiceImpl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class OrderController {

    @Value("${cartView}")
    private String cartView;

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
        return new ModelAndView(cartView);
    }

    @GetMapping("/cancelOrder/{orderId}")
    private ModelAndView cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return new ModelAndView(new RedirectView("/user/history"));
    }

    @GetMapping("/returnOrder/{orderId}")
    private ModelAndView returnOrder(@PathVariable("orderId") Long orderId){
        orderService.returnOrder(orderId);
        return new ModelAndView(new RedirectView("/user/history"));
    }


}
