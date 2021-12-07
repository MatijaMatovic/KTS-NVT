package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.impl.UserService;

import java.util.List;

public interface IDrinkOrderItemService extends IGenericService<DrinkOrderItem>{
    public DrinkOrderItem changeStatusDrinkOrderItem(Integer id, ItemStatus itemStatus) throws Exception;
    public DrinkOrderItem acceptDrinkOrderItem(Integer id, ItemStatus itemStatus, Integer bartenderId, IUserService userService) throws Exception;
    List<DrinkOrderItem> findAllByBartenderID(Integer bartenderID);
}
