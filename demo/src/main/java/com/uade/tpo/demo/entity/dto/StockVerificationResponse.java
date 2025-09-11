package com.uade.tpo.demo.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockVerificationResponse {
    private Long productId;
    private String productName;
    private Integer requestedQuantity;
    private Integer availableStock;
    private Boolean stockAvailable;
    private String message;
    private String status;
    
    // Constructor para stock disponible
    public static StockVerificationResponse available(Long productId, String productName, 
                                                    Integer requestedQuantity, Integer availableStock) {
        return new StockVerificationResponse(
            productId,
            productName,
            requestedQuantity,
            availableStock,
            true,
            String.format("Stock disponible. Tienes %d unidades disponibles para '%s'", 
                         availableStock, productName),
            "AVAILABLE"
        );
    }
    
    // Constructor para stock insuficiente
    public static StockVerificationResponse insufficient(Long productId, String productName, 
                                                        Integer requestedQuantity, Integer availableStock) {
        return new StockVerificationResponse(
            productId,
            productName,
            requestedQuantity,
            availableStock,
            false,
            String.format("Stock insuficiente. Solicitas %d unidades pero solo hay %d disponibles para '%s'", 
                         requestedQuantity, availableStock, productName),
            "INSUFFICIENT"
        );
    }
    
    // Constructor para producto no encontrado
    public static StockVerificationResponse notFound(Long productId, Integer requestedQuantity) {
        return new StockVerificationResponse(
            productId,
            "Producto no encontrado",
            requestedQuantity,
            0,
            false,
            String.format("El producto con ID %d no existe o no está activo", productId),
            "NOT_FOUND"
        );
    }
}