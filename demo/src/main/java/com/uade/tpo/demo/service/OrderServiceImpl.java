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
import com.uade.tpo.demo.entity.mapper.OrderMapper;
import com.uade.tpo.demo.entity.dto.OrderResponseDTO;

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
    public OrderResponseDTO confirmOrder(Long cartId) {
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
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(new Date());
        order.setTotal(total);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order = orderRepository.save(order);
        for (OrderDetail detail : details) {
            detail.setOrder(order);
        }
        orderDetailService.saveAllOrderDetails(details);
        cartService.deactivateCart(cartId);
        return OrderMapper.toOrderResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderMapper::toOrderResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(OrderMapper::toOrderResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByLoggedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orders = orderRepository.findByUserId(user.getUserId());
        return orders.stream().map(OrderMapper::toOrderResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByDate(Date date) {
        List<Order> orders = orderRepository.findByOrderDate(new java.sql.Date(date.getTime()));
        return orders.stream().map(OrderMapper::toOrderResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByDateRange(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream().map(OrderMapper::toOrderResponseDTO).toList();
    }
}
