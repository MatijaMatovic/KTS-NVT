package com.rokzasok.serveit.model;

import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class FoodMenu {
    private Integer id;
    private Date date;
    private Boolean isDeleted;

    private Set<DishPrice> dishes;
}
