package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishOrderItemRepository extends JpaRepository<DishOrderItem, Integer> {

    List<DishOrderItem> findByCookId(Integer cookId);
}
