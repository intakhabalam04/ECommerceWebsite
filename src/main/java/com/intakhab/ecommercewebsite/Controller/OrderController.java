package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Enum.OrderStatus;
import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Order;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Value("${cartView}")
    private String cartView;

    @Value("${orderTrackView}")
    private String orderTrackView;

    @Value("${errorView}")
    private String errorview;

    private final OrderService orderService;
    private final SecurityConfig securityConfig;

    public OrderController(OrderService orderService, SecurityConfig securityConfig) {
        this.orderService = orderService;
        this.securityConfig = securityConfig;
    }


    @GetMapping("/doCheckout")
    public ModelAndView submitCheckOutPage() {
        User user = securityConfig.getCurrentUser();
        orderService.checkOrder(user);
        return new ModelAndView(cartView);
    }

    @GetMapping("/cancelOrder/{orderId}")
    public ModelAndView cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return new ModelAndView(new RedirectView("/user/history"));
    }

    @GetMapping("/returnOrder/{orderId}")
    public ModelAndView returnOrder(@PathVariable("orderId") Long orderId){
        orderService.returnOrder(orderId);
        return new ModelAndView(new RedirectView("/user/history"));
    }

    @GetMapping("/order/track/{orderId}")
    public ModelAndView getOrderTrackingPage(@PathVariable("orderId") Long orderId) {
        Map<String, Object> map = new HashMap<>();
        Order order = orderService.getOrderById(orderId);

        // Check if the order exists
        if (order != null) {
            // Check if the current user is authorized to view the order details
            if (order.getUser().equals(securityConfig.getCurrentUser())) {
                map.put("order", order);
                return new ModelAndView(orderTrackView, map);
            } else {
                // Redirect to an error page or handle unauthorized access
                return new ModelAndView(errorview); // Change "errorPage" to your actual error page name
            }
        } else {
            // Redirect to an error page or handle order not found
            return new ModelAndView(errorview); // Change "orderNotFoundPage" to your actual error page name
        }
    }

    @GetMapping("/order/getOrderStatus/{orderId}")
    @ResponseBody
    public OrderStatus getOrderStatus(@PathVariable ("orderId") Long orderId){
        System.out.println(orderId);
        return orderService.getOrderById(orderId).getStatus();
    }





}
