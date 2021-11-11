package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Enumerated(EnumType.STRING)
    private DishCategory category;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    private String allergens;
    private String ingredients;
    private String recipe;
    private Double preparationPrice;


    private String description;
    private String imagePath;
    private Integer preparationTime;
    private Boolean isDeleted;
}
