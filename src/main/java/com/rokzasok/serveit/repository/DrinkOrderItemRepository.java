package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.DrinkOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrinkOrderItemRepository extends JpaRepository<DrinkOrderItem, Integer> {

    List<DrinkOrderItem> findByBartenderId(Integer bartenderID);
}
