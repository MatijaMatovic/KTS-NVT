package com.rokzasok.serveit.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class DrinkOrderItem {
    private Integer id;
    private String note;
    private Integer amount;
    private ItemStatus status;
    private Boolean isDeleted;

    private User bartender;
    private DrinkPrice drink;
}
