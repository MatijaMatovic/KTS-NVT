package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.repository.OrderRepository;
import com.rokzasok.serveit.service.IOrderService;
import com.rokzasok.serveit.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order findOne(Integer id) {
        return null;
    }

    @Override
    public Order save(Order entity) {
        return null;
    }

    @Override
    public Boolean deleteOne(Integer id) {
        return null;
    }
}
