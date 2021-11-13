package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DishCategory;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private DishCategory category;

    private String code;

    private String name;

    private String allergens;
    private String ingredients;
    private String recipe;
    private Double preparationPrice;


    private String description;
    private String imagePath;
    private Integer preparationTime;
}
