package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DishPrice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Model -> DTO
 */
@Component
public class DishPriceToDishPriceDTO implements Converter<DishPrice, DishPriceDTO> {

    @Override
    public DishPriceDTO convert(DishPrice source) {
        return DishPriceDTO.builder()
                .id(source.getId())
                .priceDate(source.getPriceDate())
                .dishId(source.getDish().getId())
                .dishCode(source.getDish().getCode())
                .dishName(source.getDish().getName())
                .dishCategory(source.getDish().getCategory())
                .price(source.getPrice())
                .build();
    }

    public List<DishPriceDTO> convert(List<DishPrice> source) {
        List<DishPriceDTO> result = new ArrayList<>();
        for (DishPrice dishPrice : source) {
            result.add(convert(dishPrice));
        }
        return result;
    }
}
