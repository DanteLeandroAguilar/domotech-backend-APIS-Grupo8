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
import lombok.Data;

import java.sql.Blob;

@Entity
@Data
@Table(name = "productImages")
public class ProductImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column
    private String imageUrl;

    @Column
    private Boolean isPrincipal;

    @Lob
    @Column(name = "imageBlob")
    private Blob imageBlob; // Blob es un tipo de dato para almacenar datos binarios grandes

    // Constructor vacío
    public ProductImage() {}

}