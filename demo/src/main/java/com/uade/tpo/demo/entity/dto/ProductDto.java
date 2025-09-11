package com.uade.tpo.demo.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    
    // Información básica del producto
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Boolean active;
    private String brand;
    private String compatibility;
    private String connectionType;
    private CategorySummaryDto category;
    private ProductImageDto principalImage;
    private List<ProductImageDto> images;
    private Boolean onSale;
    private Double finalPrice;
    private Double discountAmount;
    private Boolean available;

    public void calculateFields(Integer stock) {
        this.onSale = discount != null && discount > 0;
        this.available = stock != null && stock > 0;
        
        if (price != null) {
            if (onSale && discount != null) {
                this.discountAmount = roundToTwoDecimals(price * (discount / 100));
                this.finalPrice = roundToTwoDecimals(price - discountAmount);
            } else {
                this.finalPrice = price;
                this.discountAmount = 0.0;
            }
        }
    }
    
    private Double roundToTwoDecimals(Double value) {
        if (value == null) return null;
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
