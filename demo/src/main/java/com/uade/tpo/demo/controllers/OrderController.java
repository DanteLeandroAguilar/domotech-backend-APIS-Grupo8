package com.uade.tpo.demo.controllers;

import com.uade.tpo.demo.entity.dto.OrderResponseDTO;
import com.uade.tpo.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/confirm")
    public OrderResponseDTO confirmOrder() {
        return orderService.confirmOrder();
    }

    @GetMapping("/me")
    public List<OrderResponseDTO> getOrdersByLoggedUser() {
        return orderService.getOrdersByLoggedUser();
    }

    @GetMapping
    public List<OrderResponseDTO> getOrders(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return orderService.getOrders(userId, startDate, endDate);
    }
}
