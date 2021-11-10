package com.rokzasok.serveit.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class DrinkPrice {
    private Integer id;
    private Double price;
    private Date priceDate;
    private Boolean isDeleted;

    private Drink drink;
}
