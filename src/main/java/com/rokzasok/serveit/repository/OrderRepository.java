package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.Order;
import com.rokzasok.serveit.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findByIsDeletedAndStatus(boolean isDeleted, OrderStatus status);
}
