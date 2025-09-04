package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Cart;

public interface CartService {
    
    public Cart createCart(Long idUsuario);

    public Cart addProduct(Long idCart, Long idProducto, int cantidad);

    public Cart deleteProduct(Long idCart, Long idProducto);

    public Cart cleanCart(Long idCart);

    public Cart confirmCart(Long idCart);

}
