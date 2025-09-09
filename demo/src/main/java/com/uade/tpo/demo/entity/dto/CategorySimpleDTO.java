package com.uade.tpo.demo.entity.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySimpleDTO {
    private Long categoryId;
    private String name;
    private String description;
}
