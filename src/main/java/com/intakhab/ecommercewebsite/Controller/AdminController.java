package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Config.SecurityConfig;
import com.intakhab.ecommercewebsite.Enum.OrderStatus;
import com.intakhab.ecommercewebsite.Model.Category;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.CategoryService;
import com.intakhab.ecommercewebsite.Service.OrderService;
import com.intakhab.ecommercewebsite.Service.ProductService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @Value("${adminHomeView}")
    private String adminHomeView;

    @Value("${customerView}")
    private String customerView;

    @Value("${customerOrderView}")
    private String customerOrderView;

    @Value("${customerOrderProductsView}")
    private String customerOrderProductsView;

    @Value("${orderView}")
    private String orderView;

    @Value("${adminProductView}")
    private String adminProductView;

    @Value("${categoryView}")
    private String categoryView;

    @Value("${reportView}")
    private String reportView;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;
    private final SecurityConfig securityConfig;

    public AdminController(UserService userService, CategoryService categoryService, ProductService productService, OrderService orderService, SecurityConfig securityConfig) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        Map<String, Object> model = new HashMap<>();

        int totalUser = userService.getAllUsers().size();
        int totalCategories = categoryService.getAllCategories().size();
        int totalProducts = productService.getAllProducts().size();
        model.put("total_users", totalUser);
        model.put("total_categories", totalCategories);
        model.put("total_products", totalProducts);
        model.put("total_orders", orderService.getTotalOrders() != null ? orderService.getTotalOrders().size() : 0);
        model.put("total_cancellations", orderService.getTotalCancelledOrders() != null ? orderService.getTotalCancelledOrders().size() : 0);
        model.put("total_processing_orders", orderService.getTotalProcessingOrders() != null ? orderService.getTotalProcessingOrders().size() : 0);
        model.put("total_delivered_orders", orderService.getTotalDeliveredOrders() != null ? orderService.getTotalDeliveredOrders().size() : 0);
        model.put("total_returned_orders", orderService.getTotalReturnedOrders() != null ? orderService.getTotalReturnedOrders().size() : 0);
        model.put("recent_users", userService.findLast5User());

        model.put("user", securityConfig.getCurrentUser());

        return new ModelAndView(adminHomeView, model);
    }

    @GetMapping("/customer")
    public ModelAndView customerDetailsPage() {
        Map<String, Object> model = new HashMap<>();
        List<User> userList = userService.getAllUsers();
        model.put("userlist", userList);
        return new ModelAndView(customerView, model);
    }

    @GetMapping("/order")
    public ModelAndView orderDetailsPage() {
        Map<String, Object> model = new HashMap<>();

        List<Order> orderList = orderService.findAllOrders();
        model.put("allorders", orderList);
        return new ModelAndView(orderView, model);
    }

    @GetMapping("/product")
    public ModelAndView productDetailsPage() {
        Map<String, Object> model = new HashMap<>();
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategories();
        model.put("productList", productList);
        model.put("product", new Product());
        model.put("categoryList", categoryList);
        return new ModelAndView(adminProductView, model);
    }

    @GetMapping("/customer-order")
    public ModelAndView customerOrderPage(@RequestParam("id") UUID id) {
        Map<String, Object> model = new HashMap<>();
        User user = userService.findById(id);
        List<Order> orderList = orderService.findAllOrders(id);
        model.put("user", user);
        model.put("orderlist", orderList);
        return new ModelAndView(customerOrderView, model);
    }

    @GetMapping("/category")
    public ModelAndView categoryPage() {
        Map<String, Object> model = new HashMap<>();
        List<Category> categoryList = categoryService.getAllCategories();
        model.put("categorylist", categoryList);
        model.put("category", new Category());
        return new ModelAndView(categoryView, model);
    }

    @PostMapping("/add-category")
    public ModelAndView submitCategoryPage(@ModelAttribute("category") Category category) {
        Map<String, Object> model = new HashMap<>();
        boolean isAdded = categoryService.addNewCategory(category);
        RedirectView rd = new RedirectView("/admin/category");
        return new ModelAndView(rd, model);
    }

    @DeleteMapping("/category/delete")
    @ResponseBody
    public ResponseEntity<Boolean> deleteCategory(@RequestParam("categoryId") UUID id) {
        boolean deleted = categoryService.deleteCategory(id);
        return ResponseEntity.ok(deleted);
    }

    @PostMapping("/add-product")
    public ModelAndView submitProductPage(@ModelAttribute("product") Product product) {
        if (product.getCategory()==null) {
            RedirectView rd = new RedirectView("/admin/product");
            return new ModelAndView(rd);
        }
        boolean success = productService.addNewProduct(product);
        RedirectView rd = new RedirectView("/admin/product");
        return new ModelAndView(rd);
    }

    @DeleteMapping("/product/delete")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@RequestParam("productId") UUID id) {
        productService.delete(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    @GetMapping("/order/products/{orderId}")
    public ModelAndView viewOrderProducts(@PathVariable("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        List<Product> products = order.getCart().getProductList();
        ModelAndView modelAndView = new ModelAndView(customerOrderProductsView);
        modelAndView.addObject("order", order);
        modelAndView.addObject("products", products);

        return modelAndView;
    }

    @GetMapping("/view-reports")
    public ModelAndView viewReportsPage() {
        return new ModelAndView(reportView);
    }

    @GetMapping("/getReports")
    @ResponseBody
    public Map<String, Long> generateReports() {
        return orderService.generateReports();
    }

    @PostMapping("/updateOrderStatus")
    @ResponseBody
    public ResponseEntity<String> updateOrderStatus(@RequestParam("orderId") Long orderId, @RequestParam("status") OrderStatus status) {

        try {
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Order status updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating order status");

        }
    }
}
