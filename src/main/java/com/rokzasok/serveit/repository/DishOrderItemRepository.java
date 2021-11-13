package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DishOrderItemRepository extends JpaRepository<DishOrderItem, Integer> {
    @Modifying
    @Transactional
    @Query(
            value="update dish_order_item set status = ?2 where id = ?1",
            nativeQuery = true
    )
    public void changeStatusDishOrderItem(Integer id, String itemStatus);

    @Modifying
    @Transactional
    @Query(
            value="update dish_order_item set status = ?2, cook_id = ?3 where id = ?1",
            nativeQuery = true
    )
    public void acceptDishOrderItem(Integer id, String itemStatus, Integer cookId);
}
