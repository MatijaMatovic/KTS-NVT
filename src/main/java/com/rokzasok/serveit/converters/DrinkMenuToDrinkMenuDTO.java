package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.DrinkPrice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Model -> DTO
 */
@Component
public class DrinkMenuToDrinkMenuDTO implements Converter<DrinkMenu, DrinkMenuDTO> {
    @Override
    public DrinkMenuDTO convert(DrinkMenu source) {
        DrinkMenuDTO dto = DrinkMenuDTO.builder()
                .id(source.getId())
                .date(source.getDate())
                .drinks(new ArrayList<>())
                .build();
        for (DrinkPrice dp : source.getDrinks()){
            // TODO zameni konverterom za drink price u dto ili se dogovori da se na front prosledjuje lista id-jeva?
            dto.getDrinks().add(DrinkPriceDTO.builder().build());
        }
        return dto;
    }

    public List<DrinkMenuDTO> convert(List<DrinkMenu> source) {
        List<DrinkMenuDTO> result = new ArrayList<>();
        for (DrinkMenu drinkMenu : source) {
            result.add(convert(drinkMenu));
        }
        return result;
    }
}
