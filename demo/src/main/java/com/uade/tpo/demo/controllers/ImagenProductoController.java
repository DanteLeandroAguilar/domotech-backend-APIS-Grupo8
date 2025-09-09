package com.uade.tpo.demo.controllers;

import com.uade.tpo.demo.entity.ProductImage;
import com.uade.tpo.demo.service.ImagenProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImagenProductoController {
    
    @Autowired
    private ImagenProductoService imagenProductoService;
    
    /**
     * Upload an image for a product
     * POST /api/productos/{productId}/images
     */
    @PostMapping("/productos/{productId}/images")
    public ResponseEntity<ProductImage> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrincipal", defaultValue = "false") Boolean isPrincipal) {
        
        try {
            ProductImage savedImage = imagenProductoService.uploadImage(file, productId, isPrincipal);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get all images for a product
     * GET /api/productos/{productId}/images
     */
    @GetMapping("/productos/{productId}/images")
    public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Long productId) {
        try {
            List<ProductImage> images = imagenProductoService.getImagesByProduct(productId);
            return ResponseEntity.ok(images);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get principal image for a product
     * GET /api/productos/{productId}/images/principal
     */
    @GetMapping("/productos/{productId}/images/principal")
    public ResponseEntity<ProductImage> getPrincipalImage(@PathVariable Long productId) {
        try {
            ProductImage principalImage = imagenProductoService.getPrincipalImage(productId);
            if (principalImage != null) {
                return ResponseEntity.ok(principalImage);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Download/Display an image
     * GET /api/images/{imageId}
     */
    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        try {
            ProductImage image = imagenProductoService.getImageById(imageId);
            
            if (image.getImageBlob() != null) {
                Blob blob = image.getImageBlob();
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); 
                headers.setContentLength(imageBytes.length);
                
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(imageBytes);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Mark an image as principal
     * PUT /api/images/{imageId}/principal
     */
    @PutMapping("/images/{imageId}/principal")
    public ResponseEntity<ProductImage> markAsPrincipal(@PathVariable Long imageId) {
        try {
            ProductImage updatedImage = imagenProductoService.markAsPrincipal(imageId);
            return ResponseEntity.ok(updatedImage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Delete an image
     * DELETE /api/images/{imageId}
     */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        try {
            imagenProductoService.deleteImage(imageId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}