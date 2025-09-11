package com.uade.tpo.demo.mapper;

import com.uade.tpo.demo.dto.CategorySummaryDto;
import com.uade.tpo.demo.dto.ProductImageResponse;
import com.uade.tpo.demo.dto.ProductSummaryDto;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductImageMapper {
    
    /**
     * Convierte ProductImage entity a ProductImageResponse DTO
     */
    public ProductImageResponse toResponse(ProductImage productImage) {
        if (productImage == null) {
            return null;
        }
        
        ProductImageResponse response = new ProductImageResponse();
        response.setImageId(productImage.getImageId());
        response.setImageUrl(productImage.getImageUrl());
        response.setIsPrincipal(productImage.getIsPrincipal());
        
        // Mapear producto (sin las relaciones circulares)
        if (productImage.getProduct() != null) {
            response.setProduct(toProductSummary(productImage.getProduct()));
        }
        
        return response;
    }
    
    /**
     * Convierte lista de ProductImage entities a lista de DTOs
     */
    public List<ProductImageResponse> toResponseList(List<ProductImage> productImages) {
        return productImages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Convierte Product entity a ProductSummaryDto
     */
    private ProductSummaryDto toProductSummary(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductSummaryDto dto = new ProductSummaryDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setDiscount(product.getDiscount());
        dto.setActive(product.getActive());
        dto.setBrand(product.getBrand());
        dto.setCompatibility(product.getCompatibility());
        dto.setConectionType(product.getConectionType() != null ? 
            product.getConectionType().toString() : null);
        
        // Mapear categoría
        if (product.getCategory() != null) {
            dto.setCategory(toCategorySummary(product.getCategory()));
        }
        
        return dto;
    }
    
    /**
     * Convierte Category entity a CategorySummaryDto
     */
    private CategorySummaryDto toCategorySummary(Category category) {
        if (category == null) {
            return null;
        }
        
        return new CategorySummaryDto(
            category.getCategoryId(),
            category.getName(),
            category.getDescription()
        );
    }
}