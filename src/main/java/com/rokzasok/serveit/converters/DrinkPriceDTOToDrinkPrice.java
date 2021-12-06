package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.IDrinkService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO -> Model
 */
@Component
public class DrinkPriceDTOToDrinkPrice implements Converter<DrinkPriceDTO, DrinkPrice> {
    private final IDrinkService drinkService;

    public DrinkPriceDTOToDrinkPrice(IDrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @Override
    public DrinkPrice convert(DrinkPriceDTO source) {
        return DrinkPrice.builder()
                .id(source.getId())
                .price(source.getPrice())
                .priceDate(source.getPriceDate())
                .isDeleted(false)
                .drink(drinkService.findOne(source.getDrinkId()))
                .build();
    }

    public List<DrinkPrice> convert(List<DrinkPriceDTO> source) {
        List<DrinkPrice> result = new ArrayList<>();
        for (DrinkPriceDTO dto : source) {
            result.add(convert(dto));
        }
        return result;
    }
}
