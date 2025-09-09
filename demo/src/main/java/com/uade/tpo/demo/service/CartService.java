package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.dto.CartResponseDTO;

import java.util.List;

public interface CartService {
    
    public CartResponseDTO createCart(Long idUsuario);

    public CartResponseDTO addProduct(Long idCart, Long idProducto, int cantidad);

    public CartResponseDTO deleteProduct(Long idCart, Long idProducto);

    public CartResponseDTO cleanCart(Long idCart);

    public CartResponseDTO confirmCart(Long idCart);

    Cart getCartById(Long cartId);

    List<CartItem> getCartItems(Long cartId);

    void deactivateCart(Long cartId);
}
