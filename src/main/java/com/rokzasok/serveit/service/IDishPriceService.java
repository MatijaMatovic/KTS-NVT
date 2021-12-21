package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.DishPrice;

public interface IDishPriceService extends IGenericService<DishPrice>{
    DishPrice edit(Integer dishId, DishPriceDTO dishPriceDTO) throws Exception;
}
