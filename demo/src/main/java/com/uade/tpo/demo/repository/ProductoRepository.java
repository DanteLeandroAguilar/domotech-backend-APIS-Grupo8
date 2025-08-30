package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
}
