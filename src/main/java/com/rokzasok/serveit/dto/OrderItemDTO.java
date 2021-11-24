package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.ItemStatus;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private ItemStatus status;

    private String note;
    private Integer amount;

    public enum OrderItemType {DRINK, DISH}
    private OrderItemType itemType;

    private Integer priceId;

    private Map<String, Integer> specificMap;
    // if drink: private Integer bartenderId;
    // if dish: private Integer cookId; private Integer priority;
}
