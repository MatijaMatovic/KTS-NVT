package com.rokzasok.serveit.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SittingTableDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private Integer x;
    private Integer y;
}
