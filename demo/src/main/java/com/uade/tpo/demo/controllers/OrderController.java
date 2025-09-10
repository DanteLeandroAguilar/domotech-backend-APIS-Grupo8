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

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponseDTO> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/me")
    public List<OrderResponseDTO> getOrdersByLoggedUser() {
        return orderService.getOrdersByLoggedUser();
    }

    @GetMapping("/date")
    public List<OrderResponseDTO> getOrdersByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return orderService.getOrdersByDate(date);
    }

    @GetMapping("/date-range")
    public List<OrderResponseDTO> getOrdersByDateRange(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        return orderService.getOrdersByDateRange(start, end);
    }
}
