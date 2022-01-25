package com.rokzasok.serveit.service;

import com.rokzasok.serveit.exceptions.DrinkOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.impl.UserService;

import java.util.List;

public interface IDrinkOrderItemService extends IGenericService<DrinkOrderItem>{
    List<DrinkOrderItem> findAllByBartenderID(Integer bartenderID);
    DrinkOrderItem completeDrinkOrderItem(Integer id, Integer workerId, IUserService userService)
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException;
    DrinkOrderItem acceptDrinkOrderItem(Integer id, Integer workerId, IUserService userService)
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException;

    DrinkOrderItem deliverDrinkOrderItem(Integer id)
            throws DrinkOrderItemNotFoundException, ItemStatusSetException;
}
