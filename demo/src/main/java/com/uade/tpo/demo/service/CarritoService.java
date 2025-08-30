package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Carrito;

public interface CarritoService {
    
    public Carrito createCart(Long idUsuario);

    public Carrito addProduct(Long idCarrito, Long idProducto, int cantidad);

    public Carrito deleteProduct(Long idCarrito, Long idProducto);

    public Carrito cleanCart(Long idCarrito);

    public Carrito confirmCart(Long idCarrito);

}
