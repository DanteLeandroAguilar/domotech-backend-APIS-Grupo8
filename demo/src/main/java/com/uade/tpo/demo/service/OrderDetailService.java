package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.OrderDetail;
import java.util.List;

public interface OrderDetailService {
    OrderDetail saveOrderDetail(OrderDetail detail);
    List<OrderDetail> saveAllOrderDetails(List<OrderDetail> details);
}

