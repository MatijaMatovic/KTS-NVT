package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.impl.DrinkPriceService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    DrinkPriceService drinkPriceService;
    // TODO -- DA LI OVO RADITI?

    @Override
    public DrinkMenu convert(DrinkMenuDTO source) {
        DrinkMenu drinkMenu = DrinkMenu.builder()
                .id(source.getId())
                .date(source.getDate())
                .drinks(new HashSet<>())
                .build();
        for (DrinkPriceDTO dp : source.getDrinks()){
            drinkMenu.getDrinks().add(drinkPriceService.findOne(dp.getId()));
        }
        return drinkMenu;
    }

    public List<DrinkMenu> convert(List<DrinkMenuDTO> source) {
        List<DrinkMenu> result = new ArrayList<>();
        for (DrinkMenuDTO drinkMenuDTO : source) {
            result.add(convert(drinkMenuDTO));
        }
        return result;
    }
}
