package com.rokzasok.serveit.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql
        = "UPDATE drink "
        + "SET is_deleted = true "
        + "WHERE id = ?")
@Where(clause = "is_deleted=false")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Enumerated(EnumType.STRING)
    private DishCategory category;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    private String name;

    private String allergens;
    private String ingredients;
    private String recipe;
    private Double preparationPrice;


    private String description;
    private String imagePath;
    private Integer preparationTime;
    private Boolean isDeleted;
}
