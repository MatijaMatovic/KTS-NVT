package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DishPriceToDishPriceDTO {
    public List<DishPriceDTO> convert(Set<DishOrderItem> dishes) {
        return new ArrayList<>();
    }
}
