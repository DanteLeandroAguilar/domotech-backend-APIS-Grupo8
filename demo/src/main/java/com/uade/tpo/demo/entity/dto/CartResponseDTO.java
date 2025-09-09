package com.uade.tpo.demo.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long cartId;
    private Long userId;
    private boolean active;
    private List<CartItemResponseDTO> items;
}

