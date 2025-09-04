package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.ProductImage;
import com.uade.tpo.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenProductoRepository extends JpaRepository<ProductImage, Long> {
    
    /**
     * Find all images for a specific product
     */
    List<ProductImage> findByProduct(Product product);
    
    /**
     * Find the principal image of a product
     */
    ProductImage findByProductAndIsPrincipalTrue(Product product);
    
    /**
     * Count how many images a product has
     */
    long countByProduct(Product product);
}