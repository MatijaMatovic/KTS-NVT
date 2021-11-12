package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import lombok.*;

import javax.persistence.*;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishOrderItemDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private ItemStatus status;

    private String note;
    private Integer amount;
    private Integer priority;

    private Integer cookId;

    private Integer dishPriceId;
}
