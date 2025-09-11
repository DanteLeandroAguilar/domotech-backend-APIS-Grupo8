package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find products by category
     */
    Page<Product> findByCategory(Category category, Pageable pageable);
    
    /**
     * Find products with stock and active
     */
    Page<Product> findByStockGreaterThanAndActiveTrue(Integer stock, Pageable pageable);
    
    /**
     * Search products by name or description (case insensitive)
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.active = true")
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveTrue(
            @Param("searchTerm") String searchTerm1, 
            @Param("searchTerm") String searchTerm2, 
            Pageable pageable);
    
    /**
     * Find products by price range
     */
    Page<Product> findByPriceBetweenAndActiveTrue(Double minPrice, Double maxPrice, Pageable pageable);
    
    /**
     * Find products by brand (case insensitive)
     */
    Page<Product> findByBrandIgnoreCaseAndActiveTrue(String brand, Pageable pageable);
    
    /**
     * Find products by seller (if you implement seller_id field)
     */
    // Page<Product> findBySellerIdAndActiveTrue(Long sellerId, Pageable pageable);
    
    /**
     * Find products by compatibility containing text
     */
    Page<Product> findByCompatibilityContainingIgnoreCaseAndActiveTrue(String compatibility, Pageable pageable);
    
    /**
     * Find products by connection type
     */
    Page<Product> findByConectionTypeAndActiveTrue(String conectionType, Pageable pageable);
    
    /**
     * Find products with discount > 0
     */
    Page<Product> findByDiscountGreaterThanAndActiveTrue(Double discount, Pageable pageable);
    
    /**
     * Count total active products
     */
    long countByActiveTrue();
    
    /**
     * Count products by category
     */
    long countByCategoryAndActiveTrue(Category category);

    /**
     * Dynamic filter for products with multiple optional criteria
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
           "(:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:searchTerm IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:compatibility IS NULL OR LOWER(p.compatibility) LIKE LOWER(CONCAT('%', :compatibility, '%'))) AND " +
           "(:connectionType IS NULL OR LOWER(p.conectionType) LIKE LOWER(CONCAT('%', :connectionType, '%'))) AND " +
           "(:withStock IS NULL OR (:withStock = true AND p.stock > 0) OR (:withStock = false)) AND " +
           "(:withDiscount IS NULL OR (:withDiscount = true AND p.discount > 0) OR (:withDiscount = false)) AND " +
           "p.active = true")
    Page<Product> findProductsWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("brand") String brand,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("searchTerm") String searchTerm,
            @Param("compatibility") String compatibility,
            @Param("connectionType") String connectionType,
            @Param("withStock") Boolean withStock,
            @Param("withDiscount") Boolean withDiscount,
            Pageable pageable);
}