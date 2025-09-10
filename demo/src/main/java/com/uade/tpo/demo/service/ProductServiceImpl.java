package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productoRepository.findById(productId);
    }

    @Override
    public void decreaseStock(Long productId, int cantidad) {
        Optional<Product> productOpt = productoRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (!product.hasSufficientStock(cantidad)) {
                throw new RuntimeException("Stock insuficiente para la operación");
            }
            product.setStock(product.getStock() - cantidad);
            productoRepository.save(product);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }
}
