package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.DishOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishOrderItemRepository extends JpaRepository<DishOrderItem, Integer> {
}
