package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import lombok.*;

import javax.persistence.*;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrinkOrderItemDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private ItemStatus status;

    private String note;
    private Integer amount;

    private Integer bartenderId;

    private Integer drinkPriceId;
}
