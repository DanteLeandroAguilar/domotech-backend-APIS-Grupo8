package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.ProductImage;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.ImagenProductoRepository;
import com.uade.tpo.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class ImagenProductoServiceImpl implements ImagenProductoService {
    
    @Autowired
    private ImagenProductoRepository imagenProductoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Override
    public ProductImage uploadImage(MultipartFile file, Long productId, Boolean isPrincipal) throws IOException {
        // Find the product
        Optional<Product> productOpt = productoRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product product = productOpt.get();
        
        // If this image will be principal, unmark others as principal
        if (isPrincipal != null && isPrincipal) {
            List<ProductImage> existingImages = imagenProductoRepository.findByProduct(product);
            for (ProductImage img : existingImages) {
                img.setIsPrincipal(false);
                imagenProductoRepository.save(img);
            }
        }
        
        // Create new image
        ProductImage newImage = new ProductImage();
        newImage.setProduct(product);
        newImage.setIsPrincipal(isPrincipal != null ? isPrincipal : false);
        
        try {
            // Convert file to BLOB
            Blob blob = new SerialBlob(file.getBytes());
            newImage.setImageBlob(blob);
        } catch (SQLException e) {
            throw new IOException("Error processing image", e);
        }
        
        return imagenProductoRepository.save(newImage);
    }
    
    @Override
    public ProductImage getImageById(Long id) {
        return imagenProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + id));
    }
    
    @Override
    public List<ProductImage> getImagesByProduct(Long productId) {
        Optional<Product> productOpt = productoRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        return imagenProductoRepository.findByProduct(productOpt.get());
    }
    
    @Override
    public ProductImage getPrincipalImage(Long productId) {
        Optional<Product> productOpt = productoRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        return imagenProductoRepository.findByProductAndIsPrincipalTrue(productOpt.get());
    }
    
    @Override
    public ProductImage markAsPrincipal(Long imageId) {
        ProductImage image = getImageById(imageId);
        
        // Unmark all images from same product as principal
        List<ProductImage> productImages = imagenProductoRepository.findByProduct(image.getProduct());
        for (ProductImage img : productImages) {
            img.setIsPrincipal(false);
            imagenProductoRepository.save(img);
        }
        
        // Mark this image as principal
        image.setIsPrincipal(true);
        return imagenProductoRepository.save(image);
    }
    
    @Override
    public void deleteImage(Long id) {
        ProductImage image = getImageById(id);
        imagenProductoRepository.delete(image);
    }
    
    @Override
    public ProductImage updateImageUrl(Long imageId, String imageUrl) {
        Optional<ProductImage> imageOpt = imagenProductoRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            ProductImage image = imageOpt.get();
            image.setImageUrl(imageUrl);
            return imagenProductoRepository.save(image);
        }
        throw new RuntimeException("Image not found with ID: " + imageId);
    }
}