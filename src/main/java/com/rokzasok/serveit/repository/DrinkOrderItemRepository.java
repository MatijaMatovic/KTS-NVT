package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.DrinkOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkOrderItemRepository extends JpaRepository<DrinkOrderItem, Integer> {
}
