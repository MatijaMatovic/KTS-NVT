package com.rokzasok.serveit.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class Table {
    private Integer id;
    private String name;
    private Integer x;
    private Integer y;
    private Boolean isDeleted;
}
