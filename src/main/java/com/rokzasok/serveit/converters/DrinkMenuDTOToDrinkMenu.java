package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.service.IDrinkPriceService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * DTO -> Model
 */
@Component
public class DrinkMenuDTOToDrinkMenu implements Converter<DrinkMenuDTO, DrinkMenu> {
    private final IDrinkPriceService drinkPriceService;

    public DrinkMenuDTOToDrinkMenu(IDrinkPriceService drinkPriceService) {
        this.drinkPriceService = drinkPriceService;
    }

    @Override
    public DrinkMenu convert(DrinkMenuDTO source) {
        /*for (DrinkPriceDTO dp : source.getDrinks()){
            drinkMenu.getDrinks().add(drinkPriceService.findOne(dp.getId()));
        }*/ // todo what
        return DrinkMenu.builder()
                .id(source.getId())
                .date(source.getDate())
                .drinks(new HashSet<>())
                .isDeleted(false)
                .build();
    }

    public List<DrinkMenu> convert(List<DrinkMenuDTO> source) {
        List<DrinkMenu> result = new ArrayList<>();
        for (DrinkMenuDTO drinkMenuDTO : source) {
            result.add(convert(drinkMenuDTO));
        }
        return result;
    }
}
