package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.OrderDetail;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.enums.OrderStatus;
import com.uade.tpo.demo.repository.OrderRepository;
import com.uade.tpo.demo.exceptions.EmptyCartException;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public OrderResponseDTO confirmOrder() {
        Cart cart = cartService.getActiveCartByLoggedUser();
        List<CartItem> items = cartService.getCartItems(cart.getCartId());
        if (items.isEmpty()) throw new EmptyCartException("El carrito está vacío, no se puede procesar la orden");
        double total = 0.0;
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : items) {
            Product product = productService.getProductById(item.getProduct().getProductId());
            if (product == null) {
                throw new ProductNotFoundException("Producto no encontrado con ID: " + item.getProduct().getProductId());
            }
            productService.decreaseStock(product.getProductId(), item.getAmount());
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
        cartService.deactivateCart(cart.getCartId());
        return OrderMapper.toOrderResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByLoggedUser() {
        User user = userService.getLoggedUser();
        List<Order> orders = orderRepository.findByUserId(user.getUserId());
        return orders.stream().map(OrderMapper::toOrderResponseDTO).toList();
    }

    @Override
    public List<OrderResponseDTO> getOrders(Long userId, Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findOrdersBy(userId, startDate, endDate);
        return orders.stream().map(OrderMapper::toOrderResponseDTO).toList();
    }
}

