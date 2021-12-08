package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.ItemStatus;

import java.util.List;

public interface IDishOrderItemService extends IGenericService<DishOrderItem> {
    public DishOrderItem changeStatusDishOrderItem(Integer id, ItemStatus itemStatus) throws Exception;
    public DishOrderItem acceptDishOrderItem(Integer id, ItemStatus itemStatus, Integer cookId, IUserService userService) throws Exception;
    List<DishOrderItem> findAllByOrderID(Integer orderID);
    List<DishOrderItem> findAllByCookID(Integer waiterID);
}
