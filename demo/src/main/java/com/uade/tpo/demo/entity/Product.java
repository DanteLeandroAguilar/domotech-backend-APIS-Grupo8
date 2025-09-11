package com.uade.tpo.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uade.tpo.demo.entity.enums.ConectionType;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    // Mapeo directo a la columna double de MySQL
    @Column(name = "discount")
    private Double discount = 0.0;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean active = true;

    @Column(length = 255)
    private String brand;

    @Column(length = 255)
    private String compatibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "conection_type")
    private ConectionType conectionType;

    // CORRECTED: Using consistent naming - "images" instead of "imagenes"
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductImage> images;

    // CORRECTED: Using consistent naming - "cartItems" instead of "carritoItems"
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItem> cartItems;

    // CORRECTED: Using consistent naming - "orderDetails" instead of "detallePedidos"
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    // Constructor vacío para JPA
    public Product() {}

    // Constructor con parámetros principales
    public Product(String name, String description, Double price, Integer stock, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.discount = 0.0;
        this.active = true;
        this.creationDate = LocalDateTime.now();
    }
}