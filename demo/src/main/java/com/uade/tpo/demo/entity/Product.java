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
@Table(name = "productos")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long idProducto;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Category categoria;

    private String nombre;

    private String descripcion;

    private Double precio;

    private Integer stock;

    private Double descuentoPorcentaje;

    @Temporal(TemporalType.TIMESTAMP) 
    private Date fechaCreacion = new Date();

    private Boolean activo;

    private String marca;

    private String compatibilidad;

    @Enumerated(EnumType.STRING)
    private TipoConexion tipoConexion;

    @OneToMany(mappedBy = "producto")
    private List<ImagenProducto> imagenes;

    @OneToMany(mappedBy = "producto")
    private List<CarritoItem> carritoItems;

    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallePedidos;
}
