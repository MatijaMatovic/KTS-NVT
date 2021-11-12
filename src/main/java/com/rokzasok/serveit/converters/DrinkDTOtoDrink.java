package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkDTO;
import com.rokzasok.serveit.model.Drink;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DrinkDTOtoDrink implements Converter<DrinkDTO, Drink> {
    @Override
    public Drink convert(DrinkDTO source) {
        return new Drink(
                source.getId(),
                source.getCode(),
                source.getCategory(),
                source.getAllergens(),
                source.getIngredients(),
                source.getPurchasePrice(),
                source.getDescription(),
                source.getImagePath(),
                false);
    }
}
