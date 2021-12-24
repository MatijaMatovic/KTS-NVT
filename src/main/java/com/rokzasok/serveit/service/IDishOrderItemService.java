package com.rokzasok.serveit.service;

import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.IllegalUserException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;

import java.util.List;
import java.util.Optional;

public interface IDishOrderItemService extends IGenericService<DishOrderItem> {
    List<DishOrderItem> findAllByCookID(Integer cookId);
    DishOrderItem acceptDishOrderItem(Integer id, Integer cookId, IUserService userService)
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException;
    DishOrderItem completeDishOrderItem(Integer id, Integer cookId, IUserService userService)
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException, IllegalUserException;
}
