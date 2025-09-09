package com.uade.tpo.demo.controllers;

import com.uade.tpo.demo.dto.ProductImageResponse;
import com.uade.tpo.demo.entity.ProductImage;
import com.uade.tpo.demo.mapper.ProductImageMapper;
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
    
    @Autowired
    private ProductImageMapper productImageMapper;
    
    /**
     * Upload an image for a product
     * POST /api/productos/{productId}/images
     */
    @PostMapping("/productos/{productId}/images")
    public ResponseEntity<ProductImageResponse> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrincipal", defaultValue = "false") Boolean isPrincipal) {
        
        try {
            ProductImage savedImage = imagenProductoService.uploadImage(file, productId, isPrincipal);
            
            // Generar URL para acceder a la imagen
            String imageUrl = "/api/images/" + savedImage.getImageId() + "/download";
            
            // Actualizar en base de datos con la URL
            savedImage = imagenProductoService.updateImageUrl(savedImage.getImageId(), imageUrl);
            
            // Convertir a DTO antes de devolver
            ProductImageResponse response = productImageMapper.toResponse(savedImage);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public ResponseEntity<List<ProductImageResponse>> getProductImages(@PathVariable Long productId) {
        try {
            List<ProductImage> images = imagenProductoService.getImagesByProduct(productId);
            List<ProductImageResponse> response = productImageMapper.toResponseList(images);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Get principal image for a product
     * GET /api/productos/{productId}/images/principal
     */
    @GetMapping("/productos/{productId}/images/principal")
    public ResponseEntity<ProductImageResponse> getPrincipalImage(@PathVariable Long productId) {
        try {
            ProductImage principalImage = imagenProductoService.getPrincipalImage(productId);
            if (principalImage != null) {
                ProductImageResponse response = productImageMapper.toResponse(principalImage);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Download/Display an image by ID
     * GET /api/images/{imageId}/download
     */
    @GetMapping("/images/{imageId}/download")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long imageId) {
        try {
            ProductImage image = imagenProductoService.getImageById(imageId);
            
            if (image.getImageBlob() != null) {
                Blob blob = image.getImageBlob();
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                
                HttpHeaders headers = new HttpHeaders();
                // Detectar tipo de imagen por el contenido
                String contentType = detectImageType(imageBytes);
                headers.setContentType(MediaType.parseMediaType(contentType));
                headers.setContentLength(imageBytes.length);
                
                // Headers para permitir visualización en browser
                headers.set("Content-Disposition", "inline; filename=\"image_" + imageId + getFileExtension(contentType) + "\"");
                
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
     * Get image info (without downloading the blob)
     * GET /api/images/{imageId}
     */
    @GetMapping("/images/{imageId}")
    public ResponseEntity<ProductImageResponse> getImageInfo(@PathVariable Long imageId) {
        try {
            ProductImage image = imagenProductoService.getImageById(imageId);
            // Convertir a DTO (sin incluir BLOB)
            ProductImageResponse response = productImageMapper.toResponse(image);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Mark an image as principal
     * PUT /api/images/{imageId}/principal
     */
    @PutMapping("/images/{imageId}/principal")
    public ResponseEntity<ProductImageResponse> markAsPrincipal(@PathVariable Long imageId) {
        try {
            ProductImage updatedImage = imagenProductoService.markAsPrincipal(imageId);
            ProductImageResponse response = productImageMapper.toResponse(updatedImage);
            return ResponseEntity.ok(response);
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
    
    /**
     * Detectar el tipo de imagen por los primeros bytes
     */
    private String detectImageType(byte[] imageBytes) {
        if (imageBytes.length >= 3) {
            // JPEG
            if (imageBytes[0] == (byte) 0xFF && imageBytes[1] == (byte) 0xD8 && imageBytes[2] == (byte) 0xFF) {
                return "image/jpeg";
            }
            // PNG
            if (imageBytes.length >= 4 && 
                imageBytes[0] == (byte) 0x89 && imageBytes[1] == 0x50 && 
                imageBytes[2] == 0x4E && imageBytes[3] == 0x47) {
                return "image/png";
            }
            // GIF
            if (imageBytes.length >= 6 &&
                imageBytes[0] == 0x47 && imageBytes[1] == 0x49 && imageBytes[2] == 0x46) {
                return "image/gif";
            }
        }
        // Default
        return "image/jpeg";
    }
    
    /**
     * Obtener extensión de archivo según el tipo de contenido
     */
    private String getFileExtension(String contentType) {
        switch (contentType) {
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/jpeg":
            default:
                return ".jpg";
        }
    }
}