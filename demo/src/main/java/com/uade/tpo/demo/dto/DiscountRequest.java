package com.uade.tpo.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DiscountRequest {
    
    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100%")
    private Double discountPercentage;
    
    // Constructor vacío
    public DiscountRequest() {}
}