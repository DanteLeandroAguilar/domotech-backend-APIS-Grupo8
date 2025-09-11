package com.uade.tpo.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateRequest {
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Max(value = 99999, message = "Stock cannot exceed 99,999 units")
    private Integer stock;
    
    // Razón opcional para el cambio de stock (para auditoría)
    private String reason;
    
    // Tipo de operación: SET (establecer), ADD (agregar), SUBTRACT (restar)
    private StockOperation operation = StockOperation.SET;
    
    // Constructor vacío
    public StockUpdateRequest() {}
    
    // Constructor básico
    public StockUpdateRequest(Integer stock) {
        this.stock = stock;
        this.operation = StockOperation.SET;
    }
    
    // Constructor con operación específica
    public StockUpdateRequest(Integer stock, StockOperation operation) {
        this.stock = stock;
        this.operation = operation;
    }
    
    // Constructor completo con razón
    public StockUpdateRequest(Integer stock, StockOperation operation, String reason) {
        this.stock = stock;
        this.operation = operation;
        this.reason = reason;
    }
    
    /**
     * Enum para definir el tipo de operación de stock
     */
    public enum StockOperation {
        SET,        // Establecer stock absoluto
        ADD,        // Agregar al stock actual
        SUBTRACT    // Restar del stock actual
    }
    
    /**
     * Helper method to validate stock operation
     */
    public boolean isValidOperation() {
        return operation != null && 
               (operation == StockOperation.SET || 
                operation == StockOperation.ADD || 
                operation == StockOperation.SUBTRACT);
    }
    
    /**
     * Helper method to check if reason is provided for audit trail
     */
    public boolean hasReason() {
        return reason != null && !reason.trim().isEmpty();
    }
}