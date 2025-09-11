package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.CategoryRepository;
import com.uade.tpo.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductoRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public Product createProduct(Product product) {
        // Validar que la categoría existe
        if (product.getCategory() == null || product.getCategory().getCategoryId() == null) {
            throw new RuntimeException("Category is required");
        }
        
        Optional<Category> categoryOpt = categoryRepository.findById(product.getCategory().getCategoryId());
        if (!categoryOpt.isPresent()) {
            throw new RuntimeException("Category not found");
        }
        
        // Configurar valores por defecto
        product.setCategory(categoryOpt.get());
        product.setCreationDate(LocalDateTime.now());
        product.setActive(product.getActive() != null ? product.getActive() : true);
        product.setDiscount(product.getDiscount() != null ? product.getDiscount() : 0.0);
        
        // Validaciones de negocio
        if (product.getPrice() <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }
        
        if (product.getStock() < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }
        
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            throw new RuntimeException("Discount must be between 0 and 100");
        }
        
        return productRepository.save(product);
    }
    
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent()) {
            throw new RuntimeException("Category not found");
        }
        return productRepository.findByCategory(categoryOpt.get(), pageable);
    }
    
    @Override
    public Page<Product> getProductsWithStock(Pageable pageable) {
        return productRepository.findByStockGreaterThanAndActiveTrue(0, pageable);
    }
    
    @Override
    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveTrue(
                searchTerm, searchTerm, pageable);
    }
    
    @Override
    public Page<Product> getProductsByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetweenAndActiveTrue(minPrice, maxPrice, pageable);
    }
    
    @Override
    public Page<Product> getProductsByBrand(String brand, Pageable pageable) {
        return productRepository.findByBrandIgnoreCaseAndActiveTrue(brand, pageable);
    }
    
    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        
        // Actualizar solo los campos permitidos
        if (updatedProduct.getName() != null) {
            existingProduct.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            if (updatedProduct.getPrice() <= 0) {
                throw new RuntimeException("Price must be greater than 0");
            }
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getBrand() != null) {
            existingProduct.setBrand(updatedProduct.getBrand());
        }
        if (updatedProduct.getCompatibility() != null) {
            existingProduct.setCompatibility(updatedProduct.getCompatibility());
        }
        if (updatedProduct.getConectionType() != null) {
            existingProduct.setConectionType(updatedProduct.getConectionType());
        }
        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(updatedProduct.getCategory().getCategoryId());
            if (categoryOpt.isPresent()) {
                existingProduct.setCategory(categoryOpt.get());
            }
        }
        
        return productRepository.save(existingProduct);
    }
    
    @Override
    public Product updateStock(Long id, Integer newStock) {
        Product product = getProductById(id);
        
        if (newStock < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }
        
        product.setStock(newStock);
        return productRepository.save(product);
    }
    
    @Override
    public Product applyDiscount(Long id, Double discountPercentage) {
        Product product = getProductById(id);
        
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new RuntimeException("Discount must be between 0 and 100");
        }
        
        product.setDiscount(discountPercentage);
        return productRepository.save(product);
    }
    
    @Override
    public Product removeDiscount(Long id) {
        Product product = getProductById(id);
        product.setDiscount(0.0);
        return productRepository.save(product);
    }
    
    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        // Soft delete - marcar como inactivo en lugar de eliminar
        product.setActive(false);
        productRepository.save(product);
    }
    
    @Override
    public boolean hasEnoughStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        return product.getStock() >= quantity && product.getActive();
    }
    
    @Override
    public boolean reserveStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        
        if (!hasEnoughStock(id, quantity)) {
            return false;
        }
        
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return true;
    }
    
    @Override
    public Page<Product> getProductsBySeller(Long sellerId, Pageable pageable) {
        // Si implementas seller_id en Product, usar este método
        // return productRepository.findBySellerId(sellerId, pageable);
        
        // Por ahora devuelve todos los productos (hasta implementar seller_id)
        return productRepository.findAll(pageable);
    }

    @Override
    public void decreaseStock(Long productId, int cantidad) {
        Optional<Product> productOpt = productoRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (!product.hasSufficientStock(cantidad)) {
                throw new InsufficientStockException("Stock insuficiente para el producto: " + product.getName());
            }
            product.setStock(product.getStock() - cantidad);
            productoRepository.save(product);
        } else {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + productId);
        }
    }
}