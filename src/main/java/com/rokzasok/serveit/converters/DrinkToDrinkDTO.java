package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DrinkDTO;
import com.rokzasok.serveit.model.Drink;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DrinkToDrinkDTO implements Converter<Drink, DrinkDTO> {

    @Override
    public DrinkDTO convert(Drink source) {
        DrinkDTO dto = new DrinkDTO();

        dto.setId(source.getId());
        dto.setCode(source.getCode());
        dto.setCategory(source.getCategory());
        dto.setAllergens(source.getAllergens());
        dto.setIngredients(source.getIngredients());
        dto.setPurchasePrice(source.getPurchasePrice());
        dto.setDescription(source.getDescription());
        dto.setImagePath(source.getImagePath());

        return dto;
    }

    public List<DrinkDTO> convert(List<Drink> drinks) {
        List<DrinkDTO> dtoList = new ArrayList<>();
        for (Drink d : drinks) {
            dtoList.add(convert(d));
        }
        return dtoList;
    }
}
