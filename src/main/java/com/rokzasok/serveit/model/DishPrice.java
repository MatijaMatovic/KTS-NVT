package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class DishPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private Double price;
    private Date priceDate;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", referencedColumnName = "id", nullable = false)
    private Dish dish;
}
