package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface IDishOrderItemService extends IGenericService<DishOrderItem> {
    List<DishOrderItem> findAllByCookID(Integer cookId);
}
