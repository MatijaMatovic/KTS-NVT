package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.ItemStatus;

public interface IDishOrderItemService extends IGenericService<DishOrderItem>{
    public Boolean changeStatusDishOrderItem(Integer id, ItemStatus itemStatus);
}
