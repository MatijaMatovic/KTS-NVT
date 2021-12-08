package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.FoodMenuDTO;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.model.FoodMenu;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Model -> DTO
 */
@Component
public class FoodMenuToFoodMenuDTO implements Converter<FoodMenu, FoodMenuDTO> {
    private final DishPriceToDishPriceDTO dishPriceToDishPriceDTO;

    public FoodMenuToFoodMenuDTO(DishPriceToDishPriceDTO dishPriceToDishPriceDTO) {
        this.dishPriceToDishPriceDTO = dishPriceToDishPriceDTO;
    }

    @Override
    public FoodMenuDTO convert(FoodMenu source) {
        FoodMenuDTO dto = FoodMenuDTO.builder()
                .id(source.getId())
                .date(source.getDate())
                .dishes(new ArrayList<>())
                .build();
        if (source.getDishes() == null) return dto;
        for (DishPrice dp : source.getDishes()){
            dto.getDishes().add(dishPriceToDishPriceDTO.convert(dp));
        }
        return dto;
    }

    public List<FoodMenuDTO> convert(List<FoodMenu> source) {
        List<FoodMenuDTO> result = new ArrayList<>();
        for (FoodMenu menu : source) {
            result.add(convert(menu));
        }
        return result;
    }
}
