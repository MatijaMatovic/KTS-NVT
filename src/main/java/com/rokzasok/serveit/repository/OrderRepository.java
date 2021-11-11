package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
