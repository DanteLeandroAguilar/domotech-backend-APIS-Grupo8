package com.uade.tpo.demo.entity.dto;

import com.uade.tpo.demo.entity.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    private OrderStatus orderStatus;
}

