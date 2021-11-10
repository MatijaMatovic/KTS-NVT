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
public class DishOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private String note;
    private Integer amount;
    private Integer priority;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cook_id", referencedColumnName = "id")
    private User cook;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_price_id", referencedColumnName = "id")
    private DishPrice dish;
}
