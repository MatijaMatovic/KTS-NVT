package com.rokzasok.serveit.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class Order {
    private Integer id;
    private LocalDateTime creationDateTime;
    private String note;
    private OrderStatus status;
    private Boolean isDeleted;

    private Table table;
    private Set<DishOrderItem> dishes;
    private Set<DrinkOrderItem> drinks;
    private User waiter;
}
