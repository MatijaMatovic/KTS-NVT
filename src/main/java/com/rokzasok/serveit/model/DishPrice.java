package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class DishPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double price;
    private Date priceDate;
    private Boolean isDeleted;

    //TODO: Pitati da li se referencira ID ili Code kolona
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_code", referencedColumnName = "code", nullable = false)
    private Dish dish;
}
