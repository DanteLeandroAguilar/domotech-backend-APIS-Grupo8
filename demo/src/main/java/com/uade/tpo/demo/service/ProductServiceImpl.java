package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.dto.ProductDto;
import com.uade.tpo.demo.entity.dto.ProductFilterRequest;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.InsufficientStockException;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.mapper.ProductMapper;
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
@Transactional // es atómica cada operación del servicio
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductoRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;
    
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
    public ProductDto findProductById(Long id){
        return this.productMapper.toDto(getProductById(id));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    

    @Override
    public Page<ProductDto> getProductsWithStock(Pageable pageable) {
        return productRepository.findProductsWithFilters(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                true,
                null,
                pageable
        ).map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveTrue(
                searchTerm, searchTerm, pageable).map(productMapper::toDto);
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
        if(updatedProduct.getDiscount() != null){
            if (updatedProduct.getDiscount() < 0 || updatedProduct.getDiscount() > 100) {
                throw new RuntimeException("Discount must be between 0 and 100");
            }
            existingProduct.setDiscount(updatedProduct.getDiscount());
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
        if(updatedProduct.getActive() != null){
            existingProduct.setActive(updatedProduct.getActive());
        }

        if(updatedProduct.getStock() != null){
            existingProduct.setStock(updatedProduct.getStock());
            if(updatedProduct.getStock() == 0){
                existingProduct.setActive(false);
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
    public void decreaseStock(Long productId, int cantidad) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (!product.hasSufficientStock(cantidad)) {
                throw new InsufficientStockException("Stock insuficiente para el producto: " + product.getName());
            }
            product.setStock(product.getStock() - cantidad);
            productRepository.save(product);
        } else {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + productId);
        }
    }

    @Override
    public void increaseStock(Long productId, int cantidad) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStock(product.getStock() + cantidad);
            productRepository.save(product);
        } else {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + productId);
        }
    }

    @Override
    public Page<ProductDto> getFilteredProducts(ProductFilterRequest filters, Pageable pageable) {
        return productRepository.findProductsWithFilters(
                filters.getCategoryId(),
                filters.getBrand(),
                filters.getMinPrice(),
                filters.getMaxPrice(),
                filters.getSearchTerm(),
                filters.getCompatibility(),
                filters.getConnectionType(),
                true,
                filters.getWithDiscount(),
                pageable
        ).map(productMapper::toDto);
    }
}