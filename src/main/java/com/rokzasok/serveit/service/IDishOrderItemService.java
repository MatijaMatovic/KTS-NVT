package com.rokzasok.serveit.service;

import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;

import java.util.List;

public interface IDishOrderItemService extends IGenericService<DishOrderItem> {
    List<DishOrderItem> findAllByCookID(Integer cookId);
    DishOrderItem acceptDishOrderItem(Integer id, Integer cookId, IUserService userService) throws DishOrderItemNotFoundException, UserNotFoundException;
    DishOrderItem completeDishOrderItem(Integer id, Integer cookId, IUserService userService) throws DishOrderItemNotFoundException, UserNotFoundException;
}
