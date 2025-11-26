package com.uade.tpo.demo.entity.dto;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private int amount;
    private double price;
    private double discount; // Porcentaje de descuento (0-100)
    private double finalPrice; // Precio final con descuento aplicado
    private String room;

    /**
     * Calcula el precio final aplicando el descuento al precio base
     * y multiplicando por la cantidad
     */
    public double getFinalPrice() {
        double priceWithDiscount = price * (1 - discount / 100.0);
        return priceWithDiscount * amount;
    }

    /**
     * Calcula el precio unitario final (con descuento aplicado)
     */
    public double getUnitFinalPrice() {
        return price * (1 - discount / 100.0);
    }
}
