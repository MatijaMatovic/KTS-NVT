package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.ItemStatus;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemWorkerStatusDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private ItemStatus status;
    private Integer workerId;
}
