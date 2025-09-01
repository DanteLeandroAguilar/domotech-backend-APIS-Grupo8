package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.ImagenProducto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImagenProductoService {
    
    /**
     * Upload an image for a specific product
     * @param file Image file
     * @param productId ID of the product that owns the image
     * @param isPrincipal If this is the main product image
     * @return Created ImagenProducto
     */
    public ImagenProducto uploadImage(MultipartFile file, Long productId, Boolean isPrincipal) throws IOException;
    
    /**
     * Get an image by its ID
     * @param id Image ID
     * @return Found ImagenProducto
     */
    public ImagenProducto getImageById(Long id);
    
    /**
     * Get all images for a product
     * @param productId Product ID
     * @return List of product images
     */
    public List<ImagenProducto> getImagesByProduct(Long productId);
    
    /**
     * Get the main image of a product
     * @param productId Product ID
     * @return Main ImagenProducto or null if doesn't exist
     */
    public ImagenProducto getPrincipalImage(Long productId);
    
    /**
     * Mark an image as principal (unmarks others from same product)
     * @param imageId ID of the image to mark as principal
     * @return Updated ImagenProducto
     */
    public ImagenProducto markAsPrincipal(Long imageId);
    
    /**
     * Delete an image
     * @param id ID of the image to delete
     */
    public void deleteImage(Long id);
}