package com.uade.tpo.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.dto.CartResponseDTO;
import com.uade.tpo.demo.entity.mapper.CartMapper;
import com.uade.tpo.demo.repository.CartItemRepository;
import com.uade.tpo.demo.repository.CartRepository;
import com.uade.tpo.demo.repository.ProductoRepository;
import com.uade.tpo.demo.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    public CartResponseDTO createCart(Long idUsuario) {
        Optional<User> usuarioOpt = userRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        User usuario = usuarioOpt.get();
        Optional<Cart> cartOpt = cartRepository.findByUserAndActiveTrue(usuario);
        Cart cart;
        if (cartOpt.isPresent()) {
            cart = cartOpt.get();
        } else {
            cart = new Cart();
            cart.setUser(usuario);
            cart.setActive(true);
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }
        return CartMapper.toCartResponseDTO(cart);
    }

    @Transactional
    public CartResponseDTO addProduct(Long idCart, Long idProducto, int cantidad) {
        Cart cart;
        if (idCart == null) {
            // Si no hay idCart, crear uno nuevo para el usuario actual
            User usuario = userService.getLoggedUser();
            cart = getOrCreateActiveCart(usuario.getUserId());
        } else {
            cart = cartRepository.findById(idCart).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        }
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
        cart = cartRepository.save(cart);
        return CartMapper.toCartResponseDTO(cart);
    }

    @Transactional
    public CartResponseDTO deleteProduct(Long idCart, Long idProducto) {
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
        cart = cartRepository.save(cart);
        return CartMapper.toCartResponseDTO(cart);
    }

    @Transactional
    public CartResponseDTO cleanCart(Long idCart) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        if (!cart.getActive()) throw new RuntimeException("El carrito no está activo");
        List<CartItem> items = new ArrayList<>(cart.getItems());
        cartItemRepository.deleteAll(items);
        cart.getItems().clear();
        cart = cartRepository.save(cart);
        return CartMapper.toCartResponseDTO(cart);
    }

    @Transactional
    public CartResponseDTO confirmCart(Long idCart) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        if (!cart.getActive()) throw new RuntimeException("El carrito ya fue confirmado");
        cart.setActive(false);
        cart = cartRepository.save(cart);
        return CartMapper.toCartResponseDTO(cart);
    }

    @Override
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    @Override
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public void deactivateCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        cart.setActive(false);
        cartRepository.save(cart);
    }
    
    private Cart getOrCreateActiveCart(Long userId) {
        User usuario = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<Cart> cartOpt = cartRepository.findByUserAndActiveTrue(usuario);
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        Cart cart = new Cart();
        cart.setUser(usuario);
        cart.setActive(true);
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }
}
