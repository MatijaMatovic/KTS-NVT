package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.service.impl.DishPriceService;
import com.rokzasok.serveit.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DishOrderItemDTOToDishOrderItem implements Converter<DishOrderItemDTO, DishOrderItem> {
    final
    UserService userService;

    final
    DishPriceService dishPriceService;

    public DishOrderItemDTOToDishOrderItem(UserService userService, DishPriceService dishPriceService) {
        this.userService = userService;
        this.dishPriceService = dishPriceService;
    }

    @Override
    public DishOrderItem convert(DishOrderItemDTO source) {
        DishOrderItem dish = new DishOrderItem();
        //TODO: check dish.setId(source.getId());
        dish.setId(source.getId());
        dish.setStatus(source.getStatus());
        dish.setNote(source.getNote());
        dish.setAmount(source.getAmount());
        dish.setPriority(source.getPriority());
        dish.setIsDeleted(false);

        dish.setCook(userService.findOne(source.getCookId()));
        dish.setDish(dishPriceService.findOne(source.getDishPriceId()));

        return dish;
    }
}
