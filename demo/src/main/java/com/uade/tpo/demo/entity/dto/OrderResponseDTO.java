package com.uade.tpo.demo.entity.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import com.uade.tpo.demo.entity.enums.OrderStatus;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private Date orderDate;
    private OrderStatus orderStatus;
    private double total;
    private List<OrderDetailResponseDTO> details;
}

