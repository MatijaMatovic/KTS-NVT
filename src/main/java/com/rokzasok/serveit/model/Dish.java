package com.rokzasok.serveit.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class Dish {
    private Integer id;
    private String code;
    private String allergens;
    private String ingredients;
    private String recipe;
    private Double preparationPrice;
    private DishCategory category;
    private String description;
    private String imagePath;
    private Integer preparationTime;
    private Boolean isDeleted;
}
