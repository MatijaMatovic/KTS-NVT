package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.OrderItemDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.service.impl.*;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDTOToDishOrDrinkItem {
    final UserService userService;
    final DrinkPriceService drinkPriceService;

    final DishPriceService dishPriceService;


    public OrderItemDTOToDishOrDrinkItem(UserService userService, DrinkPriceService drinkPriceService,
                                         DrinkOrderItemService drinkOrderItemService, DishPriceService dishPriceService,
                                         DishOrderItemService dishOrderItemService) {
        this.userService = userService;
        this.drinkPriceService = drinkPriceService;
        this.dishPriceService = dishPriceService;
    }

    public DrinkOrderItem convertToDrinkItem(OrderItemDTO o) {
        return new DrinkOrderItem(o.getId(),
                o.getStatus(),
                o.getNote(),
                o.getAmount(),
                false,
                userService.findOne(o.getSpecificMap().get("bartenderId")),
                drinkPriceService.findOne(o.getPriceId()));
    }

    public DishOrderItem convertToDishItem(OrderItemDTO o) {
        return new DishOrderItem(o.getId(),
                o.getStatus(),
                o.getNote(),
                o.getAmount(),
                o.getSpecificMap().get("priority"),
                false,
                userService.findOne(o.getSpecificMap().get("bartenderId")),
                dishPriceService.findOne(o.getPriceId()));
    }
}

