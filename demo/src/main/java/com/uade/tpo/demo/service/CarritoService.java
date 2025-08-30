package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Carrito;

public interface CarritoService {
    
    public Carrito crearCarrito(Long idUsuario);

    public Carrito agregarProducto(Long idCarrito, Long idProducto, int cantidad);

    public Carrito eliminarProducto(Long idCarrito, Long idProducto);

    public Carrito vaciarCarrito(Long idCarrito);

    public Carrito confirmarCarrito(Long idCarrito);

}
