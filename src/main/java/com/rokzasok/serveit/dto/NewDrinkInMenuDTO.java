package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DrinkCategory;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewDrinkInMenuDTO {
    @EqualsAndHashCode.Include
    private DrinkDTO drink;
    private Integer id; // for price
    private Double price;
    private Date priceDate;
}
