package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.OrderDetail;
import com.uade.tpo.demo.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail saveOrderDetail(OrderDetail detail) {
        return orderDetailRepository.save(detail);
    }

    @Override
    public List<OrderDetail> saveAllOrderDetails(List<OrderDetail> details) {
        return orderDetailRepository.saveAll(details);
    }
}

