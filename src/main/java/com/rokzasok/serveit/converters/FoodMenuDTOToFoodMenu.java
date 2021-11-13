package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.FoodMenuDTO;
import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.service.IDishPriceService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * DTO -> Model
 */
@Component
public class FoodMenuDTOToFoodMenu implements Converter<FoodMenuDTO, FoodMenu> {
    private final IDishPriceService dishPriceService;

    public FoodMenuDTOToFoodMenu(IDishPriceService dishPriceService) {
        this.dishPriceService = dishPriceService;
    }

    @Override
    public FoodMenu convert(FoodMenuDTO source) {
        FoodMenu foodMenu = FoodMenu.builder()
                .id(source.getId())
                .date(source.getDate())
                .dishes(new HashSet<>())
                .build();
        for (DishPriceDTO dp : source.getDishes()){
            foodMenu.getDishes().add(dishPriceService.findOne(dp.getId()));
        }
        return foodMenu;
    }

    public List<FoodMenu> convert(List<FoodMenuDTO> source) {
        List<FoodMenu> result = new ArrayList<>();
        for (FoodMenuDTO dishMenuDTO : source) {
            result.add(convert(dishMenuDTO));
        }
        return result;
    }
}
