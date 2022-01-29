package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DishCategory;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DishPriceDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private Double price;
    private LocalDate priceDate;

    private Integer dishId;
    private String dishCode;
    private String dishName;
    private DishCategory dishCategory;
}
