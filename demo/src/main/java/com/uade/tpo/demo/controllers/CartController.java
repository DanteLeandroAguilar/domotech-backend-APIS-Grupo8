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

    /**
     * Obtiene el carrito activo del usuario logeado
     * GET /carts
     */
    @GetMapping
    public ResponseEntity<CartResponseDTO> getLoggedUserCart() {
        CartResponseDTO cart = cartService.getActiveCartDTOByLoggedUser();
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/update-product")
    public ResponseEntity<CartResponseDTO> updateProductAmount(@RequestParam Long idProduct, @RequestParam int amount) {
        return ResponseEntity.ok(cartService.updateProductAmount(idProduct, amount));
    }

}
