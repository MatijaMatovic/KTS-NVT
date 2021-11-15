package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.model.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderToOrderDTO implements Converter<Order, OrderDTO> {
    final DishPriceToDishPriceDTO dishPriceToDishPriceDTO;
    final DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO;

    public OrderToOrderDTO(DishPriceToDishPriceDTO dishPriceToDishPriceDTO, DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO) {
        this.dishPriceToDishPriceDTO = dishPriceToDishPriceDTO;
        this.drinkPriceToDrinkPriceDTO = drinkPriceToDrinkPriceDTO;
    }

    @Override
    public OrderDTO convert(Order source) {
        return new OrderDTO(
                source
        );
    }
}
