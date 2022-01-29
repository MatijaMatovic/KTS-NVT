package com.rokzasok.serveit.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderItemWorkerDTO {
    @EqualsAndHashCode.Include
    private Integer id;

    private Integer workerId;
}
