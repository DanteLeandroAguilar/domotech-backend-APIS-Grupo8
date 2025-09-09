package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProductById(Long productId);
    void updateStock(Long productId, int newStock);
}

