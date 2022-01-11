package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.DishOrderItemWithNameDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DishOrderItemToDishOrderItemWithNameDTO implements Converter<DishOrderItem, DishOrderItemWithNameDTO> {
    @Override
    public DishOrderItemWithNameDTO convert(DishOrderItem source) {
        return new DishOrderItemWithNameDTO(
                source.getId(),
                source.getStatus(),
                source.getNote(),
                source.getAmount(),
                source.getPriority(),
                source.getCook().getId(),
                source.getDish().getId(),
                source.getDish().getDish().getName()
        );
    }
}
