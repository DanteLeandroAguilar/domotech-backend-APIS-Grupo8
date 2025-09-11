package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Order;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    // Buscar órdenes por usuario
    @Query("SELECT o FROM Order o WHERE o.user.userId = ?1")
    List<Order> findByUserId(Long userId);


    @Query("SELECT o FROM Order o WHERE \n" +
            "(?1 IS NULL OR o.user.userId = ?1) AND \n" +
            "(?2 IS NULL OR o.orderDate >= ?2) AND \n" +
            "(?3 IS NULL OR o.orderDate <= ?3)")
    List<Order> findOrdersBy(Long userId, Date startDate, Date endDate);
}
