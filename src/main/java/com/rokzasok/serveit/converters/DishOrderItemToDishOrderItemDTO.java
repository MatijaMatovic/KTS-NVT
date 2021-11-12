package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DishOrderItemToDishOrderItemDTO implements Converter<DishOrderItem, DishOrderItemDTO> {
    @Override
    public DishOrderItemDTO convert(DishOrderItem source) {
        return new DishOrderItemDTO(
                source.getId(),
                source.getStatus(),
                source.getNote(),
                source.getAmount(),
                source.getPriority(),
                source.getCook().getId(),
                source.getDish().getId()
        );
    }
}
