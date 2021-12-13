package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.service.impl.DrinkOrderItemService;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
import com.rokzasok.serveit.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DrinkOrderItemDTOToDrinkOrderItem implements Converter<DrinkOrderItemDTO, DrinkOrderItem> {
    final
    UserService userService;

    final
    DrinkPriceService drinkService;

    public DrinkOrderItemDTOToDrinkOrderItem(UserService userService, DrinkPriceService drinkService) {
        this.userService = userService;
        this.drinkService = drinkService;
    }

    @Override
    public DrinkOrderItem convert(DrinkOrderItemDTO source) {
        DrinkOrderItem drink = new DrinkOrderItem();
        //TODO: check drink.setId(source.getId());
        drink.setId(source.getId());
        drink.setStatus(source.getStatus());
        drink.setNote(source.getNote());
        drink.setAmount(source.getAmount());
        drink.setIsDeleted(false);

        drink.setBartender(userService.findOne(source.getBartenderId()));
        drink.setDrink(drinkService.findOne(source.getDrinkPriceId()));

        return drink;
    }
}
