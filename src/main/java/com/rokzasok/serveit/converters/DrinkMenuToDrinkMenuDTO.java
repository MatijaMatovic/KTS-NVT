package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkMenuDTO;
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
    private final DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO;

    public DrinkMenuToDrinkMenuDTO(DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO) {
        this.drinkPriceToDrinkPriceDTO = drinkPriceToDrinkPriceDTO;
    }

    @Override
    public DrinkMenuDTO convert(DrinkMenu source) {
        DrinkMenuDTO dto = DrinkMenuDTO.builder()
                .id(source.getId())
                .date(source.getDate())
                .drinks(new ArrayList<>())
                .build();
        if (source.getDrinks() == null) return dto;
        int sizeOfSet = source.getDrinks().size();
        if (sizeOfSet == 0) return dto;
        for (DrinkPrice dp : source.getDrinks()){
            dto.getDrinks().add(drinkPriceToDrinkPriceDTO.convert(dp));
        }
        return dto;
    }

    public List<DrinkMenuDTO> convert(List<DrinkMenu> source) {
        List<DrinkMenuDTO> result = new ArrayList<>();
        for (DrinkMenu menu : source) {
            result.add(convert(menu));
        }
        return result;
    }
}
