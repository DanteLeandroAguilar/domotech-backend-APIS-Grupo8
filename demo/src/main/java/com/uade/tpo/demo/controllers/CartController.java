package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.dto.CartResponseDTO;
import com.uade.tpo.demo.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Crear un carrito para un usuario
    @PostMapping("/create")
    public ResponseEntity<CartResponseDTO> createCart(@RequestParam Long idUsuario) {
        return ResponseEntity.ok(cartService.createCart(idUsuario));
    }

    // Agregar producto al carrito
    @PostMapping("/{idCart}/add-product")
    public ResponseEntity<CartResponseDTO> addProduct(@PathVariable Long idCart, @RequestParam Long idProducto, @RequestParam int cantidad) {
        return ResponseEntity.ok(cartService.addProduct(idCart, idProducto, cantidad));
    }

    // Eliminar producto del carrito
    @DeleteMapping("/{idCart}/delete-product")
    public ResponseEntity<CartResponseDTO> deleteProduct(@PathVariable Long idCart, @RequestParam Long idProducto) {
        return ResponseEntity.ok(cartService.deleteProduct(idCart, idProducto));
    }

    // Vaciar carrito
    @PostMapping("/{idCart}/clean")
    public ResponseEntity<CartResponseDTO> cleanCart(@PathVariable Long idCart) {
        return ResponseEntity.ok(cartService.cleanCart(idCart));
    }

    // Confirmar carrito
    @PostMapping("/{idCart}/confirm")
    public ResponseEntity<CartResponseDTO> confirmCart(@PathVariable Long idCart) {
        return ResponseEntity.ok(cartService.confirmCart(idCart));
    }
}
