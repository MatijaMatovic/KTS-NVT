package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DrinkCategory;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DrinkPriceDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private Double price;
    private LocalDate priceDate;

    private Integer drinkId;
    private String drinkCode;
    private String drinkName;
    private DrinkCategory drinkCategory;
}
