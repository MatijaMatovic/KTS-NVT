package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkOrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DrinkPriceToDrinkPriceDTO {
    public List<DrinkPriceDTO> convert(Set<DrinkOrderItem> drinks) {
        return new ArrayList<>();
    }
}
