package com.rokzasok.serveit.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class DishOrderItem {
    private Integer id;
    private String note;
    private Integer amount;
    private ItemStatus status;
    private Integer priority;
    private Boolean isDeleted;

    private User cook;
    private DishPrice dish;
}
