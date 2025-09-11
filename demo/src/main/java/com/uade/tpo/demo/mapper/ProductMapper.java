package com.uade.tpo.demo.mapper;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.dto.*;
import com.uade.tpo.demo.entity.enums.ConectionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    
    @Autowired
    private ProductImageMapper productImageMapper;
    
    /**
     * Convert ProductRequest DTO to Product entity
     */
    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }
        
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDiscount(request.getDiscount());
        product.setActive(request.getActive());
        product.setBrand(request.getBrand());
        product.setCompatibility(request.getCompatibility());
        
        // Convert connection type string to enum
        if (request.getConectionType() != null) {
            try {
                product.setConectionType(ConectionType.valueOf(request.getConectionType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Si el valor no es válido, dejar null o valor por defecto
                product.setConectionType(null);
            }
        }
        
        // Set category (only ID, will be resolved in service)
        if (request.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(request.getCategoryId());
            product.setCategory(category);
        }
        
        return product;
    }
    
    /**
     * Convert Product entity to ProductResponse DTO
     */
    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setDiscount(product.getDiscount());
        response.setCreationDate(product.getCreationDate());
        response.setActive(product.getActive());
        response.setBrand(product.getBrand());
        response.setCompatibility(product.getCompatibility());
        response.setConectionType(product.getConectionType() != null ? 
            product.getConectionType().toString() : null);
        
        // Convert category
        if (product.getCategory() != null) {
            response.setCategory(toCategorySummary(product.getCategory()));
        }
        
        // Convert images (if loaded) - Using correct field name
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            List<ProductImageResponse> imageResponses = product.getImages().stream()
                .map(productImageMapper::toResponse)
                .collect(Collectors.toList());
            response.setImages(imageResponses);
            
            
            // Find principal image
            product.getImages().stream()
                .filter(img -> img.getIsPrincipal() != null && img.getIsPrincipal())
                .findFirst()
                .ifPresent(principalImg -> 
                    response.setPrincipalImage(productImageMapper.toResponse(principalImg)));
        }
        
        return response;
    }
    
    /**
     * Convert list of Product entities to list of ProductResponse DTOs
     */
    public List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Category entity to CategorySummaryDto
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
    
    /**
     * Update existing Product entity with data from ProductRequest
     */
    public void updateEntityFromRequest(Product product, ProductRequest request) {
        if (product == null || request == null) {
            return;
        }
        
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        if (request.getDiscount() != null) {
            product.setDiscount(request.getDiscount());
        }
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        if (request.getCompatibility() != null) {
            product.setCompatibility(request.getCompatibility());
        }
        if (request.getConectionType() != null) {
            try {
                product.setConectionType(ConectionType.valueOf(request.getConectionType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                product.setConectionType(null);
            }
        }
        if (request.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(request.getCategoryId());
            product.setCategory(category);
        }
    }

    /**
     * Convert Product entity to ProductListDto (optimized for lists, no circular references)
     */
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscount(product.getDiscount());
        dto.setActive(product.getActive());
        dto.setBrand(product.getBrand());
        dto.setCompatibility(product.getCompatibility());
        dto.setConnectionType(product.getConectionType() != null ?
            product.getConectionType().toString() : null);

        if (product.getCategory() != null) {
            dto.setCategory(new CategorySummaryDto(
                    product.getCategory().getCategoryId(),
                    product.getCategory().getName(),
                    product.getCategory().getDescription()
            ));
        }

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            List<ProductImageDto> imageDtos = product.getImages().stream()
                .map(img -> ProductImageDto.from(
                    img.getImageId(),
                    img.getImageUrl(),
                    img.getIsPrincipal()
                ))
                .collect(Collectors.toList());
            dto.setImages(imageDtos);

            product.getImages().stream()
                .filter(img -> img.getIsPrincipal() != null && img.getIsPrincipal())
                .findFirst()
                .ifPresent(principalImg ->
                    dto.setPrincipalImage(ProductImageDto.from(
                        principalImg.getImageId(),
                        principalImg.getImageUrl(),
                        principalImg.getIsPrincipal()
                    )));
        }

        dto.calculateFields(product.getStock());

        return dto;
    }
}