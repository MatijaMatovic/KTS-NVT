package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.FoodMenu;

public interface IDrinkMenuService extends IGenericService<DrinkMenu>{
    DrinkMenu edit(Integer id, DrinkMenuDTO drinkMenuDTO);

    DrinkMenu last();
}
