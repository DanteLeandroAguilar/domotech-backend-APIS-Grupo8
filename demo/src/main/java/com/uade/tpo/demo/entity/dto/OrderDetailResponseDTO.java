package com.uade.tpo.demo.entity.dto;

import lombok.Data;

@Data
public class OrderDetailResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double appliedDiscount;
    private double subtotal;
}

