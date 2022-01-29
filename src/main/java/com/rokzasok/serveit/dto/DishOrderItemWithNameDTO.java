package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishOrderItemWithNameDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private ItemStatus status;

    private String note;
    private Integer amount;
    private Integer priority;

    private Integer cookId;

    private Integer dishPriceId;
    private String dishName;

}
