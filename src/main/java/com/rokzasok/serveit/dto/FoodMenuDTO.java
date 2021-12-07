package com.rokzasok.serveit.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FoodMenuDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private LocalDate date;

    @ToString.Exclude
    private List<DishPriceDTO> dishes = new ArrayList<>();
}
