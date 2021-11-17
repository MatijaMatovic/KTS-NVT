package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.service.IDishService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO -> Model
 */
@Component
public class DishPriceDTOToDishPrice implements Converter<DishPriceDTO, DishPrice> {
    private final IDishService dishService;

    public DishPriceDTOToDishPrice(IDishService dishService) {
        this.dishService = dishService;
    }

    @Override
    public DishPrice convert(DishPriceDTO source) {
        return DishPrice.builder()
                .id(source.getId())
                .price(source.getPrice())
                .priceDate(source.getPriceDate())
                .isDeleted(false)
                .dish(dishService.findOne(source.getDishId()))
                .build();
    }

    public List<DishPrice> convert(List<DishPriceDTO> source) {
        List<DishPrice> result = new ArrayList<>();
        for (DishPriceDTO dto : source) {
            result.add(convert(dto));
        }
        return result;
    }
}
