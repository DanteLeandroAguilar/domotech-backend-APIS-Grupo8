package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    // Busca el carrito activo de un usuario
    @Query("SELECT c FROM Cart c WHERE c.user = ?1 AND c.active = true")
    Optional<Cart> findByUserAndActiveTrue(User user);

    // Busca carritos activos que no han sido modificados en las últimas 24 horas
    @Query("SELECT c FROM Cart c WHERE c.active = true AND c.lastModifiedDate < ?1")
    List<Cart> findActiveCartsOlderThan(Date cutoffDate);

    // Busca carritos inactivos que no han sido modificados en los últimos 30 días
    @Query("SELECT c FROM Cart c WHERE c.active = false AND c.lastModifiedDate < ?1")
    List<Cart> findInactiveCartsOlderThan(Date cutoffDate);

    // Actualiza carritos a inactivos por sus IDs
    @Modifying
    @Query("UPDATE Cart c SET c.active = false WHERE c.cartId IN ?1")
    int deactivateCartsByIds(List<Long> cartIds);

    // Buscar carritos con filtros opcionales
    @Query("SELECT c FROM Cart c WHERE " +
           "(:userId IS NULL OR c.user.userId = :userId) AND " +
           "(:active IS NULL OR c.active = :active) AND " +
           "(:startDate IS NULL OR c.createdDate >= :startDate) AND " +
           "(:endDate IS NULL OR c.createdDate <= :endDate)")
    List<Cart> findCartsWithFilters(
            @Param("userId") Long userId,
            @Param("active") Boolean active,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
