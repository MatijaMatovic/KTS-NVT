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
        Integer assignedCookId = source.getCook() == null ? null : source.getCook().getId();

        return new DishOrderItemWithNameDTO(
                source.getId(),
                source.getStatus(),
                source.getNote(),
                source.getAmount(),
                source.getPriority(),
                assignedCookId,
                source.getDish().getId(),
                source.getDish().getDish().getName()
        );
    }
}
