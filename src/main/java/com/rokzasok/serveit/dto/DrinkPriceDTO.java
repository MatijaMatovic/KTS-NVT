package com.rokzasok.serveit.dto;

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

    private DrinkDTO drink;
}
