package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.model.DrinkOrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DrinkOrderItemToDrinkOrderItemDTO implements Converter<DrinkOrderItem, DrinkOrderItemDTO> {
    @Override
    public DrinkOrderItemDTO convert(DrinkOrderItem source) {
        return new DrinkOrderItemDTO(
                source.getId(),
                source.getStatus(),
                source.getNote(),
                source.getAmount(),
                source.getBartender().getId(),
                source.getDrink().getId()
        );
    }
}
