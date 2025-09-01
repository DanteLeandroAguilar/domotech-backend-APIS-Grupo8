package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.ImagenProducto;
import com.uade.tpo.demo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenProductoRepository extends JpaRepository<ImagenProducto, Long> {
    
    /**
     * Find all images for a specific product
     */
    List<ImagenProducto> findByProduct(Producto product);
    
    /**
     * Find the principal image of a product
     */
    ImagenProducto findByProductAndIsPrincipalTrue(Producto product);
    
    /**
     * Count how many images a product has
     */
    long countByProduct(Producto product);
}