package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DishCategory;
import lombok.*;
import java.util.Date;

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
    private Date priceDate;

    //private Dish dish; // TODO previse referenciranja ako ovo

    private Integer dishId;
    private String dishCode;
    private String dishName;
    private DishCategory dishCategory;
}
