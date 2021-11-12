package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.IDrinkService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Model -> DTO
 */
@Component
public class DrinkPriceToDrinkPriceDTO implements Converter<DrinkPrice, DrinkPriceDTO> {

    @Override
    public DrinkPriceDTO convert(DrinkPrice source) {
        return DrinkPriceDTO.builder()
                .id(source.getId())
                .priceDate(source.getPriceDate())
                .drinkId(source.getDrink().getId())
                .drinkCode(source.getDrink().getCode())
                .drinkName(source.getDrink().getName())
                .drinkCategory(source.getDrink().getCategory())
                .price(source.getPrice())
                .build();
    }

    public List<DrinkPriceDTO> convert(List<DrinkPrice> source) {
        List<DrinkPriceDTO> result = new ArrayList<>();
        for (DrinkPrice drinkPrice : source) {
            result.add(convert(drinkPrice));
        }
        return result;
    }
}
