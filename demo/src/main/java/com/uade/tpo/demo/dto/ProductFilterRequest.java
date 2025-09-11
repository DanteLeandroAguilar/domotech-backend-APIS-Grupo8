package com.uade.tpo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductFilterRequest {
    // Getters and Setters
    private Long categoryId;
    private String brand;
    private Double minPrice;
    private Double maxPrice;
    private String searchTerm;
    private String compatibility;
    private String connectionType;
    private Boolean withStock;
    private Boolean withDiscount;

    // Constructors
    public ProductFilterRequest() {}

    public ProductFilterRequest(Long categoryId, String brand, Double minPrice, Double maxPrice, 
                               String searchTerm, String compatibility, String connectionType, 
                               Boolean withStock, Boolean withDiscount) {
        this.categoryId = categoryId;
        this.brand = brand;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.searchTerm = searchTerm;
        this.compatibility = compatibility;
        this.connectionType = connectionType;
        this.withStock = withStock;
        this.withDiscount = withDiscount;
    }

    @Override
    public String toString() {
        return "ProductFilterRequest{" +
                "categoryId=" + categoryId +
                ", brand='" + brand + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", searchTerm='" + searchTerm + '\'' +
                ", compatibility='" + compatibility + '\'' +
                ", connectionType='" + connectionType + '\'' +
                ", withStock=" + withStock +
                ", withDiscount=" + withDiscount +
                '}';
    }
}
