package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishDTO;
import com.rokzasok.serveit.model.Dish;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DishToDishDTO implements Converter<Dish, DishDTO> {
    @Override
    public DishDTO convert(Dish source) {
        return new DishDTO(
                source.getId(),
                source.getCategory(),
                source.getCode(),
                source.getName(),
                source.getAllergens(),
                source.getIngredients(),
                source.getRecipe(),
                source.getPreparationPrice(),
                source.getDescription(),
                source.getImagePath(),
                source.getPreparationTime()
        );
    }

    public List<DishDTO> convert(List<Dish> dishes) {
        List<DishDTO> dtoList = new ArrayList<>();
        for (Dish d : dishes) {
            dtoList.add(convert(d));
        }
        return dtoList;
    }
}
