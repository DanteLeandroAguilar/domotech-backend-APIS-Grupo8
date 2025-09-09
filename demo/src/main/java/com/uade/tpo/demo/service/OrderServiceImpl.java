package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.OrderDetail;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.enums.OrderStatus;
import com.uade.tpo.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public Order confirmOrder(Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        List<CartItem> items = cartService.getCartItems(cartId);
        if (items.isEmpty()) throw new RuntimeException("El carrito está vacío");
        double total = 0.0;
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : items) {
            Product product = productService.getProductById(item.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            if (product.getStock() < item.getAmount()) throw new RuntimeException("Stock insuficiente para el producto " + product.getName());
            productService.updateStock(product.getProductId(), product.getStock() - item.getAmount());
            double appliedDiscount = 0.0;
            double subtotal = (product.getPrice() - appliedDiscount) * item.getAmount();
            OrderDetail detail = new OrderDetail(product, item.getAmount(), product.getPrice(), appliedDiscount, subtotal);
            total += detail.getSubtotal();
            details.add(detail);
        }
        Order orden = new Order();
        orden.setUser(cart.getUser());
        orden.setOrderDate(new Date());
        orden.setTotal(total);
        orden.setOrderStatus(OrderStatus.CONFIRMED);
        orden = orderRepository.save(orden);
        for (OrderDetail detail : details) {
            detail.setOrder(orden);
        }
        orderDetailService.saveAllOrderDetails(details);
        cartService.deactivateCart(cartId);
        return orden;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByLoggedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderRepository.findByUserId(user.getUserId());
    }

    @Override
    public List<Order> getOrdersByDate(Date date) {
        return orderRepository.findByOrderDate(new java.sql.Date(date.getTime()));
    }

    @Override
    public List<Order> getOrdersByDateRange(Date startDate, Date endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
}
