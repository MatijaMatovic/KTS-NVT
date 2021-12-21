package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkPrice;

public interface IDrinkPriceService extends IGenericService<DrinkPrice>{
    DrinkPrice edit(Integer drinkId, DrinkPriceDTO drinkPriceDTO) throws Exception;
}
