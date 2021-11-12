package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.repository.OrderRepository;
import com.rokzasok.serveit.service.IOrderService;
import com.rokzasok.serveit.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OrderService implements IOrderService {
    final
    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findOne(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order entity) {
        return orderRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) {
        //TODO: Check if unfinished orders can be deleted
        Order toDelete = findOne(id);
        if (toDelete == null)
            throw new EntityNotFoundException("Order with given ID not found");
        orderRepository.delete(toDelete);
        return true;
    }
}
