package com.uade.tpo.demo.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    
    private Long categoryId;
    private String name;
    private String description;
    
    // Constructor para crear fácilmente desde entity
    public static CategoryDto from(Long categoryId, String name, String description) {
        return new CategoryDto(categoryId, name, description);
    }
}
