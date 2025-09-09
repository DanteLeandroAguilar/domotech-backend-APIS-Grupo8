package com.uade.tpo.demo.entity.mapper;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.OrderDetail;
import com.uade.tpo.demo.entity.dto.OrderResponseDTO;
import com.uade.tpo.demo.entity.dto.OrderDetailResponseDTO;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static OrderResponseDTO toOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getUserId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotal(order.getTotal());
        List<OrderDetailResponseDTO> detailsDto = new ArrayList<>();
        if (order.getOrderDetail() != null) {
            for (OrderDetail detail : order.getOrderDetail()) {
                detailsDto.add(toOrderDetailResponseDTO(detail));
            }
        }
        dto.setDetails(detailsDto);
        return dto;
    }

    public static OrderDetailResponseDTO toOrderDetailResponseDTO(OrderDetail detail) {
        OrderDetailResponseDTO detailDto = new OrderDetailResponseDTO();
        detailDto.setId(detail.getOrderItemId());
        detailDto.setProductId(detail.getProduct().getProductId());
        detailDto.setProductName(detail.getProduct().getName());
        detailDto.setQuantity(detail.getQuantity());
        detailDto.setUnitPrice(detail.getUnitPrice());
        detailDto.setAppliedDiscount(detail.getAppliedDiscount());
        detailDto.setSubtotal(detail.getSubtotal());
        return detailDto;
    }
}

