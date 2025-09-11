package com.uade.tpo.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Blob;

@Entity
@Data
@Table(name = "product_images")
public class ProductImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_principal")
    private Boolean isPrincipal;

    @Lob
    @Column(name = "image_blob")
    @JsonIgnore  // No serializar el BLOB en JSON para evitar respuestas enormes
    private Blob imageBlob;

    // Constructor vacío
    public ProductImage() {}

    // Constructor con parámetros principales
    public ProductImage(Product product, String imageUrl, Boolean isPrincipal) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.isPrincipal = isPrincipal != null ? isPrincipal : false;
    }
}