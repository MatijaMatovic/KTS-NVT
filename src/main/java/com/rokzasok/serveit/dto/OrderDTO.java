package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private OrderStatus status;

    private LocalDateTime creationDateTime;
    private String note;

    private Integer tableId;

    //todo: da li je ovo najbolje rešenje?
     private List<DishPriceDTO> orderDishesId;
     private List<DrinkPriceDTO> orderDrinksId;

    private Integer waiterId;
}
