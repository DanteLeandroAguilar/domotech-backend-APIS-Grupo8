package com.uade.tpo.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Carrito;
import com.uade.tpo.demo.entity.Categoria;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.CarritoRepository;
import com.uade.tpo.demo.repository.CategoryRepository;
import com.uade.tpo.demo.repository.UserRepository;

@Service
public class CarritoServiceImpl implements CarritoService {


    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UserRepository usuarioRepository;

    
    public Carrito crearCarrito(Long idUsuario) {
        User usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = new Carrito();
        carrito.setUser(usuario);
        return carritoRepository.save(carrito);
    }

    
    public Carrito agregarProducto(Long idCarrito, Long idProducto, int cantidad) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'agregarProducto'");
    }

    
    public Carrito eliminarProducto(Long idCarrito, Long idProducto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarProducto'");
    }

    
    public Carrito vaciarCarrito(Long idCarrito) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'vaciarCarrito'");
    }

   
    public Carrito confirmarCarrito(Long idCarrito) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'confirmarCarrito'");
    }
    
}
