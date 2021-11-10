package com.rokzasok.serveit.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class Drink {
    private Integer id;
    private String code;
    private String allergens;
    private String ingredients;
    private Double purchasePrice;
    private DrinkCategory category;
    private String description;
    private String imagePath;
    private Boolean isDeleted;
}
