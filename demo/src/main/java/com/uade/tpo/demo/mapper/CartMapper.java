package com.uade.tpo.demo.mapper;

import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.CartItem;
import com.uade.tpo.demo.entity.dto.CartResponseDTO;
import com.uade.tpo.demo.entity.dto.CartItemResponseDTO;
import java.util.ArrayList;
import java.util.List;

public class CartMapper {
    public static CartResponseDTO toCartResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUser().getUserId());
        dto.setActive(cart.getActive());
        List<CartItemResponseDTO> itemsDto = new ArrayList<>();
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                itemsDto.add(toCartItemResponseDTO(item));
            }
        }
        dto.setItems(itemsDto);
        return dto;
    }

    public static CartItemResponseDTO toCartItemResponseDTO(CartItem item) {
        CartItemResponseDTO itemDto = new CartItemResponseDTO();
        itemDto.setId(item.getId());
        itemDto.setProductId(item.getProduct().getProductId());
        itemDto.setProductName(item.getProduct().getName());
        itemDto.setAmount(item.getAmount());
        itemDto.setPrice(item.getProduct().getPrice());
        itemDto.setDiscount(item.getProduct().getDiscount() != null ? item.getProduct().getDiscount() : 0.0);
        itemDto.setRoom(item.getRoom() != null ? item.getRoom() : "general");
        return itemDto;
    }
}
