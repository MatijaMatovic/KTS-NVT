package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishDTO;
import com.rokzasok.serveit.model.Dish;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DishDTOtoDish implements Converter<DishDTO, Dish> {

    @Override
    public Dish convert(DishDTO source) {
        return new Dish(
                source.getId(),
                source.getCategory(),
                source.getCode(),
                source.getAllergens(),
                source.getIngredients(),
                source.getRecipe(),
                source.getPreparationPrice(),
                source.getDescription(),
                source.getImagePath(),
                source.getPreparationTime(),
                false
        );
    }
}
