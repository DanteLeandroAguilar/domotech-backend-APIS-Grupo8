package com.uade.tpo.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImageDto {
    
    private Long imageId;
    private String imageUrl;
    private Boolean isPrincipal;
    
    // Constructor para crear fácilmente desde entity
    public static ProductImageDto from(Long imageId, String imageUrl, Boolean isPrincipal) {
        return new ProductImageDto(imageId, imageUrl, isPrincipal);
    }
}
