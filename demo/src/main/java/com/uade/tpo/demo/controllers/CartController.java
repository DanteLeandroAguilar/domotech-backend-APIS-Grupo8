package com.uade.tpo.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import com.uade.tpo.demo.entity.dto.CartResponseDTO;
import com.uade.tpo.demo.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> getCarts(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        List<CartResponseDTO> carts = cartService.getCarts(userId, active, startDate, endDate);
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/me")
    public ResponseEntity<CartResponseDTO> getLoggedUserCart() {
        CartResponseDTO cart = cartService.getActiveCartDTOByLoggedUser();
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/update-product")
    public ResponseEntity<CartResponseDTO> updateProductAmount(@RequestParam Long idProduct, @RequestParam int amount) {
        return ResponseEntity.ok(cartService.updateProductAmount(idProduct, amount));
    }

}
