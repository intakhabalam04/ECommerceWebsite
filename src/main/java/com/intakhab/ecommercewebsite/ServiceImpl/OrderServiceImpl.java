package com.intakhab.ecommercewebsite.ServiceImpl;

import com.intakhab.ecommercewebsite.Model.Cart;
import com.intakhab.ecommercewebsite.Model.Order;
import com.intakhab.ecommercewebsite.Model.Product;
import com.intakhab.ecommercewebsite.Model.User;
import com.intakhab.ecommercewebsite.Repository.CartRepo;
import com.intakhab.ecommercewebsite.Repository.OrderRepo;
import com.intakhab.ecommercewebsite.Repository.UserRepo;
import com.intakhab.ecommercewebsite.Service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;

    public OrderServiceImpl(OrderRepo orderRepo, UserRepo userRepo, CartRepo cartRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
    }

    @Override
    public void checkOrder(User user) {


            double total = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");

        Order order = new Order();
        order.setUser(user);

        // Use the last added cart
        List<Cart> userCarts = user.getCart();
        if (!userCarts.isEmpty()) {
            userCarts.sort(Comparator.comparing(Cart::getCreatedDate));
            Cart lastCart = userCarts.get(userCarts.size() - 1);
            order.setCart(lastCart);
            for (Product product : lastCart.getProductList()) {
                total += product.getPrice();
                System.out.println("price"+product.getPrice());
            }

        }



        order.setOrderDate(LocalDateTime.now().format(formatter));
        order.setTotalAmount(total);
        order.setStatus("DONE");


        System.out.println(order.getCart().getCartId());
        System.out.println(order.getOrderId());

        orderRepo.save(order);

        // Create a new cart for the user
        List<Cart> cartList = user.getCart();
        Cart newCart = new Cart();
        cartList.add(newCart);
        newCart.setUser(user);
        newCart.setCreatedDate(LocalDateTime.now());
        user.setCart(cartList);
        userRepo.save(user);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepo.findAll(Sort.by("orderDate").descending());
    }

    @Override
    public List<Order> findAllOrders(UUID id) {
        System.out.println("1");
        List<Order> orderListById=new ArrayList<>();
        System.out.println("3");
        for(Order order:findAllOrders()){
            if (order.getUser().getId().equals(id)){
                orderListById.add(order);
            }
        }
        System.out.println("2");
        return orderListById;
    }

    @Override
    public Order getOrderById(UUID orderId) {
        return orderRepo.findById(orderId).get();
    }
}
