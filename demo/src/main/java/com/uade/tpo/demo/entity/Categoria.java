package com.uade.tpo.demo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "categorias")
public class Categoria {
    
    public Categoria() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long idCategoria;

    @Column
    private String nombre;

    @Column
    private String descripcion;

    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos;

}
