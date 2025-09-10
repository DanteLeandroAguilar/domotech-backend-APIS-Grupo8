package com.uade.tpo.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductSearchRequest {
    
    @Size(min = 2, max = 100, message = "Search term must be between 2 and 100 characters")
    private String searchTerm;
    
    private Long categoryId;
    
    @DecimalMin(value = "0.0", message = "Minimum price cannot be negative")
    @DecimalMax(value = "999999.99", message = "Minimum price cannot exceed 999,999.99")
    private Double minPrice;
    
    @DecimalMin(value = "0.01", message = "Maximum price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Maximum price cannot exceed 999,999.99")
    private Double maxPrice;
    
    @Size(max = 50, message = "Brand name cannot exceed 50 characters")
    private String brand;
    
    @Size(max = 200, message = "Compatibility cannot exceed 200 characters")
    private String compatibility;
    
    @Pattern(regexp = "^(WIFI|BLUETOOTH|ZIGBEE|ETHERNET|USB)$", 
             message = "Connection type must be one of: WIFI, BLUETOOTH, ZIGBEE, ETHERNET, USB")
    private String conectionType;
    
    private Boolean inStock = true; // Solo productos con stock por defecto
    
    private Boolean onSale; // Filtrar solo productos con descuento
    
    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;
    
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private Integer size = 20;
    
    @Pattern(regexp = "^(name|price|creationDate|discount|stock|brand)$", 
             message = "Sort field must be one of: name, price, creationDate, discount, stock, brand")
    private String sortBy = "name";
    
    @Pattern(regexp = "^(asc|desc)$", 
             message = "Sort direction must be 'asc' or 'desc'")
    private String sortDirection = "asc";
    
    // Constructor vacío
    public ProductSearchRequest() {}
    
    // Constructor para búsqueda básica
    public ProductSearchRequest(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    // Constructor para filtro por categoría
    public ProductSearchRequest(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    // Constructor para filtro por rango de precios
    public ProductSearchRequest(Double minPrice, Double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
    
    /**
     * Validar que minPrice sea menor que maxPrice si ambos están presentes
     */
    @AssertTrue(message = "Minimum price must be less than maximum price")
    public boolean isPriceRangeValid() {
        if (minPrice == null || maxPrice == null) {
            return true; // Si uno de los dos es null, no validamos
        }
        return minPrice <= maxPrice;
    }
    
    /**
     * Helper method to check if any filters are applied
     */
    public boolean hasFilters() {
        return searchTerm != null || categoryId != null || 
               minPrice != null || maxPrice != null || 
               brand != null || compatibility != null || 
               conectionType != null || onSale != null;
    }
    
    /**
     * Helper method to check if price range filter is applied
     */
    public boolean hasPriceRange() {
        return minPrice != null || maxPrice != null;
    }
    
    /**
     * Helper method to reset all filters
     */
    public void resetFilters() {
        this.searchTerm = null;
        this.categoryId = null;
        this.minPrice = null;
        this.maxPrice = null;
        this.brand = null;
        this.compatibility = null;
        this.conectionType = null;
        this.onSale = null;
        this.inStock = true; // Mantener filtro de stock por defecto
    }
}