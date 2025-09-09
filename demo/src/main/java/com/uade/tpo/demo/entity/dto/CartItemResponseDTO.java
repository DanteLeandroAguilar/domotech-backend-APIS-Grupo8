package com.uade.tpo.demo.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartItemResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private int amount;
    private double price;
}

