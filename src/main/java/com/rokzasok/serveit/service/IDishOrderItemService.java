package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.ItemStatus;

import java.util.List;

public interface IDishOrderItemService extends IGenericService<DishOrderItem> {
    public Boolean changeStatusDishOrderItem(Integer id, ItemStatus itemStatus);
    public Boolean acceptDishOrderItem(Integer id, ItemStatus itemStatus, Integer cookId);
    List<DishOrderItem> findAllByOrderID(Integer orderID);
    List<DishOrderItem> findAllByCookID(Integer waiterID);
}
