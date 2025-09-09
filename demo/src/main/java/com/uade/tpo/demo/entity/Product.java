package com.uade.tpo.demo.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uade.tpo.demo.entity.enums.ConectionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private Double discount; // Porcentaje de descuento

    @Temporal(TemporalType.TIMESTAMP) 
    private Date creationDate = new Date();

    private Boolean active;

    private String brand;

    private String compatibility;

    @Enumerated(EnumType.STRING)
    private ConectionType conectionType;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> imagenes;

    @OneToMany(mappedBy = "product")
    private List<CartItem> carritoItems;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> detallePedidos;
}
