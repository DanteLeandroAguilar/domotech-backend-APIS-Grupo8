package com.uade.tpo.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orderDetail")
public class OrderDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column
    private Integer quantity;

    @Column
    private Double unitPrice;

    @Column
    private Double appliedDiscount;

    @Column
    private Double subtotal; // (precioUnitario - descuento) * cantidad

    public OrderDetail() {}

    public OrderDetail(Product product, Integer quantity, Double unitPrice, Double appliedDiscount, Double subtotal) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.appliedDiscount = appliedDiscount;
        this.subtotal = subtotal;
    }
}
