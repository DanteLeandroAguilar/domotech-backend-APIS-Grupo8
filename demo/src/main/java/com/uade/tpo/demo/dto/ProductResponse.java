package com.uade.tpo.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Double discount;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate;
    
    private Boolean active;
    private String brand;
    private String compatibility;
    private String conectionType;
    private CategorySummaryDto category;
    
    // Lista completa de imágenes del producto
    private List<ProductImageResponse> images;
    
    // Imagen principal del producto (para fácil acceso)
    private ProductImageResponse principalImage;
    
    // Constructor vacío
    public ProductResponse() {}
    
    // Constructor básico para listados simples
    public ProductResponse(Long productId, String name, String description, Double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Constructor completo para responses detalladas
    public ProductResponse(Long productId, String name, String description, Double price, 
                          Integer stock, Double discount, Boolean active, String brand, 
                          String compatibility, String conectionType, CategorySummaryDto category) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.discount = discount;
        this.active = active;
        this.brand = brand;
        this.compatibility = compatibility;
        this.conectionType = conectionType;
        this.category = category;
        this.creationDate = LocalDateTime.now();
    }
    
    /**
     * Helper method to check if product has images
     */
    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }
    
    /**
     * Helper method to check if product has principal image
     */
    public boolean hasPrincipalImage() {
        return principalImage != null;
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
     * Helper method to check if product has stock available
     */
    public boolean hasStock() {
        return stock != null && stock > 0;
    }
}