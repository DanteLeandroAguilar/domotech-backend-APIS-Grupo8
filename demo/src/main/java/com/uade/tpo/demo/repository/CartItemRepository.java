package com.uade.tpo.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    @Query("SELECT c FROM CartItem c WHERE c.cart.cartId = ?1")
    List<CartItem> findByCartId(Long cartId);
}
