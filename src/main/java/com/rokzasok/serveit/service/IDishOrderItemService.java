package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.FoodMenu;

import java.util.List;

public interface IDishOrderItemService extends IGenericService<DishOrderItem>{

    List<DishOrderItem> findAllByOrderID(Integer orderID);
}
