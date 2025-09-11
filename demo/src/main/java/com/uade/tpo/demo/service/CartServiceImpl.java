package com.uade.tpo.demo.service;

import java.util.ArrayList;
import java.util.Date;
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
import com.uade.tpo.demo.mapper.CartMapper;
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
        cart.setLastModifiedDate(new Date());
        cart = cartRepository.save(cart);
        return CartMapper.toCartResponseDTO(cart);
    }

    @Override
    public Cart getActiveCartByLoggedUser() {
        User usuario = userService.getLoggedUser();
        return getOrCreateActiveCart(usuario.getUserId());
    }

    @Override
    public CartResponseDTO getActiveCartDTOByLoggedUser() {
        Cart cart = getActiveCartByLoggedUser();
        return CartMapper.toCartResponseDTO(cart);
    }

    @Override
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public void deactivateCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException("Carrito no encontrado con ID: " + cartId));
        cart.setActive(false);
        cart.setLastModifiedDate(new Date());
        cartRepository.save(cart);
    }
    
    @Override
    @Transactional
    public int deactivateExpiredCarts() {
        long twentyFourHoursAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        Date cutoffDate = new Date(twentyFourHoursAgo);

        List<Cart> expiredCarts = cartRepository.findActiveCartsOlderThan(cutoffDate);

        if (expiredCarts.isEmpty()) {
            return 0;
        }

        List<Long> cartIds = expiredCarts.stream()
                .map(Cart::getCartId)
                .toList();

        int deactivatedCount = cartRepository.deactivateCartsByIds(cartIds);

        return deactivatedCount;
    }

    @Override
    @Transactional
    public int deleteOldInactiveCarts() {
        long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
        Date cutoffDate = new Date(thirtyDaysAgo);

        List<Cart> oldInactiveCarts = cartRepository.findInactiveCartsOlderThan(cutoffDate);

        if (oldInactiveCarts.isEmpty()) {
            return 0;
        }

        cartRepository.deleteAll(oldInactiveCarts);

        int deletedCount = oldInactiveCarts.size();
        System.out.println("Carritos eliminados permanentemente (con cascading JPA): " + deletedCount);
        return deletedCount;
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
