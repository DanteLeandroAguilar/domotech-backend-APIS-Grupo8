package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    /**
     * Create a new product (only SELLER users)
     * @param product Product to create
     * @return Created product
     */
    Product createProduct(Product product);
    
    /**
     * Get product by ID
     * @param id Product ID
     * @return Product if found
     */
    Product getProductById(Long id);
    
    /**
     * Find product by ID (returns Optional)
     * @param id Product ID
     * @return Optional containing product if found
     */
    Optional<Product> findById(Long id);
    
    /**
     * Get all products with pagination
     * @param pageable Pagination parameters
     * @return Page of products
     */
    Page<Product> getAllProducts(Pageable pageable);
    
    /**
     * Get products by category
     * @param categoryId Category ID
     * @param pageable Pagination parameters
     * @return Page of products in category
     */
    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);
    
    /**
     * Get products with stock > 0 (for public catalog)
     * @param pageable Pagination parameters
     * @return Page of products with stock
     */
    Page<Product> getProductsWithStock(Pageable pageable);
    
    /**
     * Search products by name or description
     * @param searchTerm Search term
     * @param pageable Pagination parameters
     * @return Page of matching products
     */
    Page<Product> searchProducts(String searchTerm, Pageable pageable);
    
    /**
     * Filter products by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param pageable Pagination parameters
     * @return Page of products in price range
     */
    Page<Product> getProductsByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);
    
    /**
     * Filter products by brand
     * @param brand Brand name
     * @param pageable Pagination parameters
     * @return Page of products by brand
     */
    Page<Product> getProductsByBrand(String brand, Pageable pageable);
    
    /**
     * Update product (only owner or admin)
     * @param id Product ID
     * @param updatedProduct Updated product data
     * @return Updated product
     */
    Product updateProduct(Long id, Product updatedProduct);
    
    /**
     * Update product stock
     * @param id Product ID
     * @param newStock New stock quantity
     * @return Updated product
     */
    Product updateStock(Long id, Integer newStock);
    
    /**
     * Apply discount to product
     * @param id Product ID
     * @param discountPercentage Discount percentage (0-100)
     * @return Updated product
     */
    Product applyDiscount(Long id, Double discountPercentage);
    
    /**
     * Remove discount from product
     * @param id Product ID
     * @return Updated product
     */
    Product removeDiscount(Long id);
    
    /**
     * Delete product (only owner or admin)
     * @param id Product ID
     */
    void deleteProduct(Long id);
    
    /**
     * Check if product has enough stock
     * @param id Product ID
     * @param quantity Required quantity
     * @return true if enough stock available
     */
    boolean hasEnoughStock(Long id, Integer quantity);
    
    /**
     * Reserve stock for checkout
     * @param id Product ID
     * @param quantity Quantity to reserve
     * @return true if reservation successful
     */
    boolean reserveStock(Long id, Integer quantity);
    
    /**
     * Get products by seller (for seller dashboard)
     * @param sellerId Seller user ID
     * @param pageable Pagination parameters
     * @return Page of seller's products
     */
    Page<Product> getProductsBySeller(Long sellerId, Pageable pageable);
}