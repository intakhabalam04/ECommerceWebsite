package com.intakhab.ecommercewebsite.Controller;

import com.intakhab.ecommercewebsite.Model.Category;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Service.CategoryService;
import com.intakhab.ecommercewebsite.Service.OrderService;
import com.intakhab.ecommercewebsite.Service.ProductService;
import com.intakhab.ecommercewebsite.Service.UserService;
import org.aspectj.weaver.ast.Or;
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

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(UserService userService, CategoryService categoryService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        String viewName = "Admin/home";
        Map<String, Object> model = new HashMap<>();

        int totalUser = userService.getAllUsers().size();
        int totalCategories = categoryService.getAllCategories().size();
        int totalProducts = productService.getAllProducts().size();
        model.put("total_users", totalUser);
        model.put("total_categories", totalCategories);
        model.put("total_products", totalProducts);

        return new ModelAndView(viewName, model);
    }

    @GetMapping("/customer")
    public ModelAndView customerDetailsPage() {
        Map<String, Object> model = new HashMap<>();
        List<User> userList = userService.getAllUsers();
        model.put("userlist", userList);
        String viewName = "Admin/customer";
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/order")
    public ModelAndView orderDetailsPage() {
        String viewName = "Admin/order";
        Map<String,Object> model=new HashMap<>();

        List<Order> orderList=orderService.findAllOrders();
        model.put("allorders",orderList);
        return new ModelAndView(viewName,model);
    }

    @GetMapping("/product")
    public ModelAndView productDetailsPage() {
        String viewName = "Admin/product";
        Map<String, Object> model = new HashMap<>();
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategories();
        model.put("productList", productList);
        model.put("product", new Product());
        model.put("categoryList", categoryList);
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/customer-order")
    public ModelAndView customerOrderPage(@RequestParam("id") UUID id) {
        String viewName = "Admin/customer-order";
        Map<String, Object> model = new HashMap<>();
        User user = userService.findById(id);
        System.out.println("4");
        List<Order> orderList=orderService.findAllOrders(id);
        System.out.println("5");
        model.put("user", user);
        model.put("orderlist",orderList);
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/category")
    public ModelAndView categoryPage() {
        String viewName = "Admin/category";
        Map<String, Object> model = new HashMap<>();
        List<Category> categoryList = categoryService.getAllCategories();
        model.put("categorylist", categoryList);
        model.put("category", new Category());
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/add-category")
    public ModelAndView submitCategoryPage(@ModelAttribute("category") Category category) {
        Map<String, Object> model = new HashMap<>();
        boolean isAdded = categoryService.addNewCategory(category);
        RedirectView rd = new RedirectView("/admin/category");
        return new ModelAndView(rd, model);
    }

    @GetMapping("/category/delete")
    public ModelAndView deleteCategory(@RequestParam("categoryId") UUID id) {
        categoryService.deleteCategory(id);
        RedirectView rd = new RedirectView("/admin/category");
        return new ModelAndView(rd);
    }

    @PostMapping("/add-product")
    public ModelAndView submitProductPage(@ModelAttribute("product") Product product) {

        System.out.println(product.getProductImg());

        Map<String, Object> model = new HashMap<>();
        boolean success = productService.addNewProduct(product);
        RedirectView rd = new RedirectView("/admin/product");
        return new ModelAndView(rd);
    }

    @GetMapping("/product/delete")
    public ModelAndView deleteProduct(@RequestParam("productId") UUID id) {
        productService.delete(id);
        RedirectView rd = new RedirectView("/admin/product");
        return new ModelAndView(rd);
    }

    @GetMapping("/order/products/{orderId}")
    public ModelAndView viewOrderProducts(@PathVariable("orderId") UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        List<Product> products = order.getCart().getProductList();
        ModelAndView modelAndView = new ModelAndView("/admin/customer-order-products");
        modelAndView.addObject("order", order);
        modelAndView.addObject("products", products);

        return modelAndView;
    }



}
