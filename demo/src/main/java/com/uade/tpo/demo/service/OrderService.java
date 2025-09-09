package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.dto.OrderResponseDTO;

import java.util.Date;
import java.util.List;

public interface OrderService {
    OrderResponseDTO confirmOrder(Long cartId);
    List<OrderResponseDTO> getAllOrders();
    List<OrderResponseDTO> getOrdersByUserId(Long userId);
    List<OrderResponseDTO> getOrdersByLoggedUser();
    List<OrderResponseDTO> getOrdersByDate(Date date);
    List<OrderResponseDTO> getOrdersByDateRange(Date startDate, Date endDate);
}

