package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.ItemStatus;

public interface IDrinkOrderItemService extends IGenericService<DrinkOrderItem>{
    public Boolean changeStatusDrinkOrderItem(Integer id, ItemStatus itemStatus);
    public Boolean acceptDrinkOrderItem(Integer id, ItemStatus itemStatus, Integer bartenderId);
}
