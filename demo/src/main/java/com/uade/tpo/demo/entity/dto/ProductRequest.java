package com.uade.tpo.demo.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price must be less than 1,000,000")
    private Double price;
    
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Max(value = 99999, message = "Stock cannot exceed 99,999")
    private Integer stock;
    
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100%")
    private Double discount = 0.0;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    @Size(max = 50, message = "Brand name cannot exceed 50 characters")
    private String brand;
    
    @Size(max = 200, message = "Compatibility cannot exceed 200 characters")
    private String compatibility;
    
    @Pattern(regexp = "^(WIFI|BLUETOOTH|ZIGBEE|ETHERNET|USB)$", 
             message = "Connection type must be one of: WIFI, BLUETOOTH, ZIGBEE, ETHERNET, USB")
    private String conectionType; // WIFI, BLUETOOTH, ZIGBEE, etc.
    
    private Boolean active;
    
    // Constructor vacío
    public ProductRequest() {}
}