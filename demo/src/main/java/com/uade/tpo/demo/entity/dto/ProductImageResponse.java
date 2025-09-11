package com.uade.tpo.demo.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImageResponse {
    
    private Long imageId;
    private String imageUrl;
    private Boolean isPrincipal;
    private ProductSummaryDto product;
    
    // Constructor vacío
    public ProductImageResponse() {}
    
    // Constructor con parámetros
    public ProductImageResponse(Long imageId, String imageUrl, Boolean isPrincipal, ProductSummaryDto product) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isPrincipal = isPrincipal;
        this.product = product;
    }
}