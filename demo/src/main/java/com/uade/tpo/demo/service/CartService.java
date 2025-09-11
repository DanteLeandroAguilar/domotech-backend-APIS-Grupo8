package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.dto.CartResponseDTO;

import java.util.List;

public interface CartService {

    CartResponseDTO updateProductAmount(Long idProduct, int amount);

    Cart getActiveCartByLoggedUser();

    CartResponseDTO getActiveCartDTOByLoggedUser();

    List<CartItem> getCartItems(Long cartId);

    void deactivateCart(Long cartId);

    int deactivateExpiredCarts();
}
