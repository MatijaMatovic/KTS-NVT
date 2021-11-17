package com.rokzasok.serveit.service.impl;


import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.repository.OrderRepository;
import com.rokzasok.serveit.service.IOrderService;
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
        Order toDelete = findOne(id);

        if (toDelete == null)
            throw new EntityNotFoundException("Order with given ID not found");

        //TODO: Check if unfinished orders can be deleted
        //TODO: Check if OrderItems have to be JOIN FETCH-ed
        for (DishOrderItem dish : toDelete.getDishes()) {
            if (dish.getStatus() != ItemStatus.CREATED) {
                return false;
            }
        }
        for (DrinkOrderItem drink : toDelete.getDrinks()) {
            if (drink.getStatus() != ItemStatus.CREATED) {
                return false;
            }
        }
        orderRepository.delete(toDelete);
        return true;
    }

    @Override
    public List<Order> getNotFinishedOrders() {
        return orderRepository.findByIsDeletedAndStatus(false, OrderStatus.NOT_FINISHED);
    }
}
