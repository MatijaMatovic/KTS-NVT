package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.FoodMenuDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.FoodMenu;

public interface IFoodMenuService extends IGenericService<FoodMenu>{
    FoodMenu edit(Integer id, FoodMenuDTO foodMenuDTO);
    FoodMenu last();
}
