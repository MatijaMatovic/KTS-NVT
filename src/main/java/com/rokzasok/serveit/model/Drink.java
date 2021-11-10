package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private DrinkCategory category;

    private String allergens;
    private String ingredients;
    private Double purchasePrice;
    private String description;
    private String imagePath;
    private Boolean isDeleted;
}
