package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DrinkCategory;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrinkDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private String code;

    private String name;

    private DrinkCategory category;

    private String allergens;
    private String ingredients;
    private Double purchasePrice;
    private String description;
    private String imagePath;
}
