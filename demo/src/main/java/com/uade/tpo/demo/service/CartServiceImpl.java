package com.uade.tpo.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.CartItemRepository;
import com.uade.tpo.demo.repository.CartRepository;
import com.uade.tpo.demo.repository.ProductoRepository;
import com.uade.tpo.demo.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart createCart(Long idUsuario) {
        Optional<User> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        User usuario = usuarioOpt.get();
        Optional<Cart> cartOpt = cartRepository.findByUserAndActiveTrue(usuario);
        if (cartOpt.isPresent()) {
            return cartOpt.get(); // Ya tiene un carrito activo
        }
        Cart cart = new Cart();
        cart.setUser(usuario);
        cart.setActive(true);
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    public Cart addProduct(Long idCart, Long idProducto, int cantidad) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        Product product = productoRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (!cart.getActive()) throw new RuntimeException("El carrito no está activo");
        List<CartItem> items = cart.getItems();
        CartItem found = null;
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(idProducto)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            found.setAmount(found.getAmount() + cantidad);
            cartItemRepository.save(found);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setAmount(cantidad);
            cartItemRepository.save(newItem);
            items.add(newItem);
        }
        cart.setItems(items);
        return cartRepository.save(cart);
    }

    public Cart deleteProduct(Long idCart, Long idProducto) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        if (!cart.getActive()) throw new RuntimeException("El carrito no está activo");
        List<CartItem> items = cart.getItems();
        CartItem toRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(idProducto)) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) {
            items.remove(toRemove);
            cartItemRepository.delete(toRemove);
        }
        cart.setItems(items);
        return cartRepository.save(cart);
    }

    public Cart cleanCart(Long idCart) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        if (!cart.getActive()) throw new RuntimeException("El carrito no está activo");
        List<CartItem> items = new ArrayList<>(cart.getItems());
        for (CartItem item : items) {
            cartItemRepository.delete(item);
        }
        cart.getItems().clear();
        return cartRepository.save(cart);
    }

    public Cart confirmCart(Long idCart) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        if (!cart.getActive()) throw new RuntimeException("El carrito ya fue confirmado");
        cart.setActive(false);
        return cartRepository.save(cart);
    }
    
}
