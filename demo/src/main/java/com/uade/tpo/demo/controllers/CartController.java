package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Cart;
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
    public ResponseEntity<Cart> createCart(@RequestParam Long idUsuario) {
        Cart cart = cartService.createCart(idUsuario);
        return ResponseEntity.ok(cart);
    }

    // Agregar producto al carrito
    @PostMapping("/{idCart}/add-product")
    public ResponseEntity<Cart> addProduct(@PathVariable Long idCart, @RequestParam Long idProducto, @RequestParam int cantidad) {
        Cart cart = cartService.addProduct(idCart, idProducto, cantidad);
        return ResponseEntity.ok(cart);
    }

    // Eliminar producto del carrito
    @DeleteMapping("/{idCart}/delete-product")
    public ResponseEntity<Cart> deleteProduct(@PathVariable Long idCart, @RequestParam Long idProducto) {
        Cart cart = cartService.deleteProduct(idCart, idProducto);
        return ResponseEntity.ok(cart);
    }

    // Vaciar carrito
    @PostMapping("/{idCart}/clean")
    public ResponseEntity<Cart> cleanCart(@PathVariable Long idCart) {
        Cart cart = cartService.cleanCart(idCart);
        return ResponseEntity.ok(cart);
    }

    // Confirmar carrito
    @PostMapping("/{idCart}/confirm")
    public ResponseEntity<Cart> confirmCart(@PathVariable Long idCart) {
        Cart cart = cartService.confirmCart(idCart);
        return ResponseEntity.ok(cart);
    }
}
