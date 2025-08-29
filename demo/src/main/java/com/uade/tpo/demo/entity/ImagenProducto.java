package com.uade.tpo.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "imagenes_productos")
public class ImagenProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long idImagen;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column
    private String urlImagen;

    @Column
    private Boolean esPrincipal;
}
