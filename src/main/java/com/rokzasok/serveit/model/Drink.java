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
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@SQLDelete(sql
        = "UPDATE drink "
        + "SET is_deleted = true "
        + "WHERE id = ?")
@Where(clause = "is_deleted=false")
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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
