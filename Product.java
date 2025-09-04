package com.uade.tpo.demo.entity;

import java.util.Date;
import java.util.List;

import com.uade.tpo.demo.entity.enums.TipoConexion;

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
    @JoinColumn(name = "category_id", nullable = false)
    private Categoria category;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private Double discount;

    @Temporal(TemporalType.TIMESTAMP) 
    private Date creationDate = new Date();

    private Boolean active;

    private String brand;

    private String compatibility;

    @Enumerated(EnumType.STRING)
    private TipoConexion conectionType;

    @OneToMany(mappedBy = "product")
    private List<ProductImages> images;

    @OneToMany(mappedBy = "product")
    private List<CartItem> carritoItems;

    @OneToMany(mappedBy = "product")
    private List<DetallePedido> detallePedidos;
}
