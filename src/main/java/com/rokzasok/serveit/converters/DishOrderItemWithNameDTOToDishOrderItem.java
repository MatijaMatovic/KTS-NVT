package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.DishOrderItemWithNameDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.service.impl.DishPriceService;
import com.rokzasok.serveit.service.impl.UserService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DishOrderItemWithNameDTOToDishOrderItem implements Converter<DishOrderItemWithNameDTO, DishOrderItem> {
    final
    UserService userService;

    final
    DishPriceService dishPriceService;

    public DishOrderItemWithNameDTOToDishOrderItem(UserService userService, DishPriceService dishPriceService)

    {
        this.userService = userService;
        this.dishPriceService = dishPriceService;
    }

    @Override
    public DishOrderItem convert(DishOrderItemWithNameDTO source) {
        DishOrderItem dish = new DishOrderItem();
        //TODO: check dish.setId(source.getId());
        dish.setId(source.getId());
        dish.setStatus(source.getStatus());
        dish.setNote(source.getNote());
        dish.setAmount(source.getAmount());
        dish.setPriority(source.getPriority());
        dish.setIsDeleted(false);

        if (source.getCookId() != null)
            dish.setCook(userService.findOne(source.getCookId()));
        dish.setDish(dishPriceService.findOne(source.getDishPriceId()));

        return dish;
    }
}
