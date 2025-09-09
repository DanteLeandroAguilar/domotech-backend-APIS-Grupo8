package com.uade.tpo.demo.dto;

import lombok.Data;

@Data
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
    
    // Constructor vacío
    public ProductSummaryDto() {}
    
    // Constructor con parámetros principales
    public ProductSummaryDto(Long productId, String name, String description, Double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}