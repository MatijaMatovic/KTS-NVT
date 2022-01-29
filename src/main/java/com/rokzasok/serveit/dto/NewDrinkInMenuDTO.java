package com.rokzasok.serveit.dto;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate priceDate;
}
