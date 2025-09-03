package com.uade.tpo.demo.entity.dto;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private boolean success;
    private String message;
    private Long categoryId;
    private String name;
    private String description;

    public CategoryResponseDTO() {}

    public CategoryResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CategoryResponseDTO(boolean success, String message, Long categoryId, String name, String description) {
        this.success = success;
        this.message = message;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }
}
