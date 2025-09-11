package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.dto.OrderResponseDTO;

import java.util.Date;
import java.util.List;

public interface OrderService {
    OrderResponseDTO confirmOrder();
    List<OrderResponseDTO> getOrdersByLoggedUser();
    List<OrderResponseDTO> getOrders(Long id, Date startDate, Date endDate);
}
