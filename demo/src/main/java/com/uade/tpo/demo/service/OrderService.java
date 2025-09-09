package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Order;
import java.util.Date;
import java.util.List;

public interface OrderService {
    Order confirmOrder(Long cartId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getOrdersByLoggedUser();
    List<Order> getOrdersByDate(Date date);
    List<Order> getOrdersByDateRange(Date startDate, Date endDate);
}

