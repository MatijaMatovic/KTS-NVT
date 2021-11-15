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

public class DrinkPriceDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private Double price;
    private Date priceDate;

    private Integer drinkId;
    private String drinkCode;
    private String drinkName;
    private DrinkCategory drinkCategory;
}
