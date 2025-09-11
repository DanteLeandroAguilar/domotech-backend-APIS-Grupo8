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
import com.uade.tpo.demo.exceptions.CartNotFoundException;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;

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

    @Override
    @Transactional
    public CartResponseDTO updateProductAmount(Long idProducto, int cantidad) {
        User usuario = userService.getLoggedUser();
        Cart cart = getOrCreateActiveCart(usuario.getUserId());

        if (!cart.getActive()) throw new CartNotFoundException("El carrito no está activo");
        Product product = productoRepository.findById(idProducto).orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + idProducto));

        List<CartItem> items = cart.getItems();
        CartItem found = null;
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(idProducto)) {
                found = item;
                break;
            }
        }

        if (cantidad <= 0) {
            if (found != null) {
                items.remove(found);
                cartItemRepository.delete(found);
            }
        } else if (found != null) {
            found.setAmount(cantidad);
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

    @Override
    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException("Carrito no encontrado con ID: " + cartId));
    }

    @Override
    public Cart getActiveCartByLoggedUser() {
        User usuario = userService.getLoggedUser();
        return getOrCreateActiveCart(usuario.getUserId());
    }

    @Override
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public void deactivateCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException("Carrito no encontrado con ID: " + cartId));
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
