package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.model.DrinkOrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DrinkOrderItemToDrinkOrderItemDTO implements Converter<DrinkOrderItem, DrinkOrderItemDTO> {
    @Override
    public DrinkOrderItemDTO convert(DrinkOrderItem source) {
        Integer assignedBartenderName
                = source.getBartender() == null ? null : source.getBartender().getId();
        return new DrinkOrderItemDTO(
                source.getId(),
                source.getStatus(),
                source.getNote(),
                source.getAmount(),
                assignedBartenderName,
                source.getDrink().getId(),
                source.getDrink().getDrink().getName()
        );
    }
}
