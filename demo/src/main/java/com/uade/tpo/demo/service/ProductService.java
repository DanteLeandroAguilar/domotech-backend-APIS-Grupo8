package com.uade.tpo.demo.service;

import com.uade.tpo.demo.dto.ProductDto;
import com.uade.tpo.demo.dto.ProductFilterRequest;
import com.uade.tpo.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(Long id);

    ProductDto findProductById(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    Page<ProductDto> getProductsWithStock(Pageable pageable);

    Page<ProductDto> searchProducts(String searchTerm, Pageable pageable);

    Page<ProductDto> getFilteredProducts(ProductFilterRequest filters, Pageable pageable);

    Product updateProduct(Long id, Product updatedProduct);

    Product updateStock(Long id, Integer newStock);

    Product applyDiscount(Long id, Double discountPercentage);

    Product removeDiscount(Long id);

    void deleteProduct(Long id);

    void decreaseStock(Long productId, int cantidad);
}
