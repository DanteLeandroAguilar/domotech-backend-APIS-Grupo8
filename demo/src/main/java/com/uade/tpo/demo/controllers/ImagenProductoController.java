package com.uade.tpo.demo.controllers;

import com.uade.tpo.demo.entity.dto.ProductImageResponse;
import com.uade.tpo.demo.entity.ProductImage;
import com.uade.tpo.demo.mapper.ProductImageMapper;
import com.uade.tpo.demo.service.ImagenProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImagenProductoController {
    
    @Autowired
    private ImagenProductoService imagenProductoService;
    
    @Autowired
    private ProductImageMapper productImageMapper;
    
    // ==================== ENDPOINTS PÚBLICOS (VER IMÁGENES) ====================
    
    /**
     * Get all images for a product
     * GET /api/productos/{productId}/images
     * ACCESO: PÚBLICO (cualquiera puede ver las imágenes)
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
     * ACCESO: PÚBLICO (cualquiera puede ver la imagen principal)
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
     * ACCESO: PÚBLICO (cualquiera puede descargar/ver imágenes)
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
     * ACCESO: PÚBLICO (cualquiera puede ver información de imágenes)
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
    
    // ==================== ENDPOINTS SOLO VENDEDOR ====================
    
    /**
     * Upload an image for a product
     * POST /api/productos/{productId}/images
     * ACCESO: SOLO VENDEDOR (requiere JWT con rol SELLER)
     */
    @PostMapping("/productos/{productId}/images")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isPrincipal", defaultValue = "false") Boolean isPrincipal) {
        
        // Validaciones de archivo
        if (file.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Archivo vacío");
            error.put("message", "Debe seleccionar un archivo de imagen válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        // Validar tipo de archivo
        String contentType = file.getContentType();
        if (!isValidImageType(contentType)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Tipo de archivo inválido");
            error.put("message", "Solo se permiten imágenes (JPG, JPEG, PNG, GIF)");
            error.put("receivedType", contentType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        // Validar extensión
        if (!isValidImageExtension(file.getOriginalFilename())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Extensión de archivo inválida");
            error.put("message", "El archivo debe tener extensión .jpg, .jpeg, .png o .gif");
            error.put("filename", file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        // Validar tamaño (máximo 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Archivo demasiado grande");
            error.put("message", "El tamaño máximo permitido es 5MB");
            error.put("fileSize", String.valueOf(file.getSize()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        
        try {
            // Validar contenido del archivo
            byte[] fileBytes = file.getBytes();
            if (!isValidImageByBytes(fileBytes)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Contenido de archivo inválido");
                error.put("message", "El archivo no es una imagen válida");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            ProductImage savedImage = imagenProductoService.uploadImage(file, productId, isPrincipal);
            
            // Generar URL para acceder a la imagen
            String imageUrl = "/api/images/" + savedImage.getImageId() + "/download";
            
            // Actualizar en base de datos con la URL
            savedImage = imagenProductoService.updateImageUrl(savedImage.getImageId(), imageUrl);
            
            // Convertir a DTO antes de devolver
            ProductImageResponse response = productImageMapper.toResponse(savedImage);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la imagen");
            error.put("message", "No se pudo guardar la imagen. Verifique que el archivo sea una imagen válida.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Producto no encontrado");
            error.put("message", "No existe un producto con ID: " + productId);
            error.put("productId", productId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Mark an image as principal
     * PUT /api/images/{imageId}/principal
     * ACCESO: SOLO VENDEDOR (requiere JWT con rol SELLER)
     */
    @PutMapping("/images/{imageId}/principal")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> markAsPrincipal(@PathVariable Long imageId) {
        try {
            ProductImage updatedImage = imagenProductoService.markAsPrincipal(imageId);
            ProductImageResponse response = productImageMapper.toResponse(updatedImage);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", "Imagen marcada como principal exitosamente");
            successResponse.put("message", "La imagen " + imageId + " ahora es la imagen principal del producto");
            successResponse.put("image", response);
            
            return ResponseEntity.ok(successResponse);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Imagen no encontrada");
            error.put("message", "No existe una imagen con ID: " + imageId);
            error.put("imageId", imageId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Delete an image
     * DELETE /api/images/{imageId}
     * ACCESO: SOLO VENDEDOR (requiere JWT con rol SELLER)
     */
    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable Long imageId) {
        try {
            // Verificar si la imagen existe antes de eliminar
            ProductImage image = imagenProductoService.getImageById(imageId);
            String productName = image.getProduct() != null ? image.getProduct().getName() : "Unknown";
            
            imagenProductoService.deleteImage(imageId);
            
            Map<String, String> response = new HashMap<>();
            response.put("success", "Imagen eliminada exitosamente");
            response.put("message", "La imagen del producto '" + productName + "' ha sido eliminada correctamente");
            response.put("imageId", imageId.toString());
            response.put("status", "DELETED");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Imagen no encontrada");
            error.put("message", "No existe una imagen con ID: " + imageId);
            error.put("imageId", imageId.toString());
            error.put("status", "NOT_FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    // ==================== MÉTODOS UTILITARIOS PRIVADOS ====================
    
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
     * Validar que el archivo sea una imagen válida por content type
     */
    private boolean isValidImageType(String contentType) {
        return contentType != null && (
            contentType.equals("image/jpeg") || 
            contentType.equals("image/jpg") || 
            contentType.equals("image/png") || 
            contentType.equals("image/gif")
        );
    }

    /**
     * Validar que el archivo sea una imagen válida por extensión
     */
    private boolean isValidImageExtension(String filename) {
        if (filename == null) return false;
        String lowerFilename = filename.toLowerCase();
        return lowerFilename.endsWith(".jpg") || 
               lowerFilename.endsWith(".jpeg") || 
               lowerFilename.endsWith(".png") || 
               lowerFilename.endsWith(".gif");
    }

    /**
     * Validar que el archivo sea realmente una imagen analizando sus bytes
     */
    private boolean isValidImageByBytes(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length < 4) {
            return false;
        }
        
        // JPEG
        if (fileBytes.length >= 3 && 
            fileBytes[0] == (byte) 0xFF && 
            fileBytes[1] == (byte) 0xD8 && 
            fileBytes[2] == (byte) 0xFF) {
            return true;
        }
        
        // PNG
        if (fileBytes.length >= 8 && 
            fileBytes[0] == (byte) 0x89 && 
            fileBytes[1] == 0x50 && 
            fileBytes[2] == 0x4E && 
            fileBytes[3] == 0x47 &&
            fileBytes[4] == 0x0D && 
            fileBytes[5] == 0x0A && 
            fileBytes[6] == 0x1A && 
            fileBytes[7] == 0x0A) {
            return true;
        }
        
        // GIF
        if (fileBytes.length >= 6 &&
            fileBytes[0] == 0x47 && 
            fileBytes[1] == 0x49 && 
            fileBytes[2] == 0x46) {
            return true;
        }
        
        return false;
    }

    /**
     * Obtener extensión de archivo para PNG y JPG
     */
    private String getFileExtension(String contentType) {
        switch (contentType) {
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/jpeg":
            case "image/jpg":
            default:
                return ".jpg";
        }
    }
}