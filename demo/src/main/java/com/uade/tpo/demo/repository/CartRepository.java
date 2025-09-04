package com.uade.tpo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.User;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    // Busca el carrito activo de un usuario
    @Query("SELECT c FROM Cart c WHERE c.user = ?1 AND c.active = true")
    Optional<Cart> findByUserAndActiveTrue(User user);
}
