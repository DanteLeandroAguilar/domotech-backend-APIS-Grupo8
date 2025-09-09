package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    // Buscar órdenes por usuario
    @Query("SELECT o FROM Order o WHERE o.user.userId = ?1")
    List<Order> findByUserId(Long userId);

    // Buscar órdenes por fecha exacta
    @Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = ?1")
    List<Order> findByOrderDate(java.sql.Date date);

    // Buscar órdenes entre fechas
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN ?1 AND ?2")
    List<Order> findByOrderDateBetween(java.util.Date startDate, java.util.Date endDate);
}
