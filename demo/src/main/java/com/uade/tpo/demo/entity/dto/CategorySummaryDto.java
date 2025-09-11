package com.uade.tpo.demo.entity.dto;

import lombok.Data;

@Data
public class CategorySummaryDto {
    
    private Long categoryId;
    private String name;
    private String description;
    
    // Constructor vacío
    public CategorySummaryDto() {}
    
    // Constructor con parámetros
    public CategorySummaryDto(Long categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }
}