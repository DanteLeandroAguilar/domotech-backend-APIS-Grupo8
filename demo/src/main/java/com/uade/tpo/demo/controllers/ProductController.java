package com.uade.tpo.demo.controllers;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.dto.*;
import com.uade.tpo.demo.mapper.ProductMapper;
import com.uade.tpo.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductMapper productMapper;
    
    // ==================== ENDPOINTS PÚBLICOS (CATÁLOGO) ====================
    
    /**
     * Get products with stock (public catalog)
     * GET /api/products/catalog
     * ACCESO: PÚBLICO (cualquiera puede ver el catálogo)
     */
    @GetMapping("/catalog")
    public ResponseEntity<Page<ProductDto>> getProductCatalog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductDto> response = productService.getProductsWithStock(pageable);

        return ResponseEntity.ok(response);
    }
    
    /**
     * Get product by ID (public access for product details)
     * GET /api/products/{id}
     * ACCESO: PÚBLICO (cualquiera puede ver detalles del producto)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        try {
            ProductDto response = productService.findProductById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Search products (mantenido por compatibilidad)
     * GET /api/products/search
     * ACCESO: PÚBLICO (cualquiera puede buscar productos)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductDto> response = productService.searchProducts(term, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDto>> getFilteredProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String compatibility,
            @RequestParam(required = false) String connectionType,
            @RequestParam(required = false) Boolean withStock,
            @RequestParam(required = false) Boolean withDiscount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        try {
            ProductFilterRequest filters = new ProductFilterRequest(
                categoryId, brand, minPrice, maxPrice, searchTerm,
                compatibility, connectionType, withStock, withDiscount
            );

            Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<ProductDto> response = productService.getFilteredProducts(filters, pageable);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // ==================== ENDPOINTS SOLO VENDEDOR ====================
    
    /**
     * Create new product (only SELLER users)
     * POST /api/products
     * ACCESO: SOLO VENDEDOR (requiere JWT con rol SELLER)
     */
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        try {
            Product product = productMapper.toEntity(request);
            Product savedProduct = productService.createProduct(product);
            ProductResponse response = productMapper.toResponse(savedProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Get all products with pagination (admin view)
     * GET /api/products
     * ACCESO: SOLO VENDEDOR (vista completa para gestión)
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productService.getAllProducts(pageable);
        Page<ProductResponse> response = products.map(productMapper::toResponse);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update product (only SELLER users)
     * PUT /api/products/{id}
     * ACCESO: SOLO VENDEDOR (requiere JWT con rol SELLER)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        
        try {
            Product productUpdates = productMapper.toEntity(request);
            Product updatedProduct = productService.updateProduct(id, productUpdates);
            ProductResponse response = productMapper.toResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        
        try {
            Product updatedProduct = productService.updateStock(id, request.getStock());
            ProductResponse response = productMapper.toResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    

    @PatchMapping("/{id}/discount")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> applyDiscount(
            @PathVariable Long id,
            @Valid @RequestBody DiscountRequest request) {
        
        try {
            Product updatedProduct = productService.applyDiscount(id, request.getDiscountPercentage());
            ProductResponse response = productMapper.toResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}/discount")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> removeDiscount(@PathVariable Long id) {
        try {
            Product updatedProduct = productService.removeDiscount(id);
            ProductResponse response = productMapper.toResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            
            if (!product.getActive()) {
                Map<String, String> warningResponse = new HashMap<>();
                warningResponse.put("warning", "Producto ya eliminado");
                warningResponse.put("message", String.format("El producto '%s' ya estaba marcado como eliminado", product.getName()));
                warningResponse.put("status", "ALREADY_DELETED");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(warningResponse);
            }
            
            productService.deleteProduct(id);
            
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("success", "Producto eliminado exitosamente");
            successResponse.put("message", String.format("El producto '%s' (ID: %d) ha sido eliminado correctamente", product.getName(), id));
            successResponse.put("productName", product.getName());
            successResponse.put("status", "DELETED");
            
            return ResponseEntity.ok(successResponse);
            
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("message", "Ha ocurrido un error inesperado al eliminar el producto");
            errorResponse.put("details", e.getMessage());
            errorResponse.put("status", "ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
