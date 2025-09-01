package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.ImagenProducto;
import com.uade.tpo.demo.entity.Producto;
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
    public ImagenProducto uploadImage(MultipartFile file, Long productId, Boolean isPrincipal) throws IOException {
        // Find the product
        Optional<Producto> productOpt = productoRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Producto product = productOpt.get();
        
        // If this image will be principal, unmark others as principal
        if (isPrincipal != null && isPrincipal) {
            List<ImagenProducto> existingImages = imagenProductoRepository.findByProduct(product);
            for (ImagenProducto img : existingImages) {
                img.setIsPrincipal(false);
                imagenProductoRepository.save(img);
            }
        }
        
        // Create new image
        ImagenProducto newImage = new ImagenProducto();
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
    public ImagenProducto getImageById(Long id) {
        return imagenProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + id));
    }
    
    @Override
    public List<ImagenProducto> getImagesByProduct(Long productId) {
        Optional<Producto> productOpt = productoRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        return imagenProductoRepository.findByProduct(productOpt.get());
    }
    
    @Override
    public ImagenProducto getPrincipalImage(Long productId) {
        Optional<Producto> productOpt = productoRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        return imagenProductoRepository.findByProductAndIsPrincipalTrue(productOpt.get());
    }
    
    @Override
    public ImagenProducto markAsPrincipal(Long imageId) {
        ImagenProducto image = getImageById(imageId);
        
        // Unmark all images from same product as principal
        List<ImagenProducto> productImages = imagenProductoRepository.findByProduct(image.getProduct());
        for (ImagenProducto img : productImages) {
            img.setIsPrincipal(false);
            imagenProductoRepository.save(img);
        }
        
        // Mark this image as principal
        image.setIsPrincipal(true);
        return imagenProductoRepository.save(image);
    }
    
    @Override
    public void deleteImage(Long id) {
        ImagenProducto image = getImageById(id);
        imagenProductoRepository.delete(image);
    }
}