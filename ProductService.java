package com.uade.tpo.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.uade.tpo.demo.entity.Product;

public interface ProductService {
    public Product createProduct(Product product);

    public Optional<Product> getProductById(Long productId);

    public Page<Product> getProducts();
    
    public Page<Product> getProductByCategoryId(Long categoryId);
    
}
