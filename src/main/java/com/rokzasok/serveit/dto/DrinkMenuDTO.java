package com.rokzasok.serveit.dto;

import lombok.*;
import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DrinkMenuDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private Date date;

    @ToString.Exclude
    private List<DrinkPriceDTO> drinks = new ArrayList<>();
}
