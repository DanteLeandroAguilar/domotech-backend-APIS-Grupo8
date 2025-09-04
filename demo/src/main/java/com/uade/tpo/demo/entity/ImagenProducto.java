package com.uade.tpo.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Blob;

@Entity
@Table(name = "imagenes_productos")
public class ImagenProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Product product;

    @Column
    private String imageUrl;

    @Column
    private Boolean isPrincipal;

    @Lob
    @Column(name = "image_blob")
    private Blob imageBlob;

    // Constructor vacío
    public ImagenProducto() {}

    // Getters y Setters
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(Boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public Blob getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Blob imageBlob) {
        this.imageBlob = imageBlob;
    }
}