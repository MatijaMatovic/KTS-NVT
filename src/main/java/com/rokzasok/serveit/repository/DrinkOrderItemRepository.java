package com.rokzasok.serveit.repository;

import com.rokzasok.serveit.model.DrinkOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DrinkOrderItemRepository extends JpaRepository<DrinkOrderItem, Integer> {
    @Modifying
    @Transactional
    @Query(
            value="update drink_order_item set status = ?2 where id = ?1",
            nativeQuery = true
    )
    public void changeStatusDrinkOrderItem(Integer id, String itemStatus);

    @Modifying
    @Transactional
    @Query(
            value="update drink_order_item set status = ?2, bartender_id = ?3 where id = ?1",
            nativeQuery = true
    )
    public void acceptDrinkOrderItem(Integer id, String itemStatus, Integer bartenderId);

    List<DrinkOrderItem> findByBartenderId(Integer bartenderID);
}
