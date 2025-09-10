package com.uade.tpo.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSummaryDto {
    
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Double discount;
    private Boolean active;
    private String brand;
    private String compatibility;
    private String conectionType;
    private CategorySummaryDto category;
    
    // URL de la imagen principal para mostrar en listados
    private String principalImageUrl;
    
    // Constructor vacío
    public ProductSummaryDto() {}
    
    // Constructor con parámetros principales
    public ProductSummaryDto(Long productId, String name, String description, Double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Constructor más completo para listados
    public ProductSummaryDto(Long productId, String name, String description, Double price, 
                            Integer stock, String brand, CategorySummaryDto category) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.active = true;
        this.discount = 0.0;
    }
    
    /**
     * Helper method to check if product has stock available
     */
    public boolean hasStock() {
        return stock != null && stock > 0;
    }
    
    /**
     * Helper method to check if product is on sale (has discount)
     */
    public boolean isOnSale() {
        return discount != null && discount > 0;
    }
    
    /**
     * Helper method to calculate final price after discount
     */
    public Double getFinalPrice() {
        if (price == null) return null;
        if (discount == null || discount == 0) return price;
        return price * (1 - discount / 100);
    }
    
    /**
     * Helper method to get discount amount in currency
     */
    public Double getDiscountAmount() {
        if (price == null || discount == null || discount == 0) return 0.0;
        return price * (discount / 100);
    }
    
    /**
     * Helper method to check if product is available (active and has stock)
     */
    public boolean isAvailable() {
        return active != null && active && hasStock();
    }
    
    /**
     * Helper method to get a short description (first 100 characters)
     */
    public String getShortDescription() {
        if (description == null) return null;
        if (description.length() <= 100) return description;
        return description.substring(0, 100) + "...";
    }
}