package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.dto.CartResponseDTO;

import java.util.Date;
import java.util.List;

public interface CartService {

    CartResponseDTO updateProductAmount(Long idProduct, int amount, String room);

    Cart getActiveCartByLoggedUser();

    CartResponseDTO getActiveCartDTOByLoggedUser();

    List<CartItem> getCartItems(Long cartId);

    void deactivateCart(Long cartId);

    int deactivateExpiredCarts();

    int deleteOldInactiveCarts();

    List<CartResponseDTO> getCarts(Long userId, Boolean active, Date startDate, Date endDate);
}
