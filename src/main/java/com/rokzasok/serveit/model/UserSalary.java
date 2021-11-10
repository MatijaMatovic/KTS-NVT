package com.rokzasok.serveit.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class UserSalary {
    private Integer id;
    private Double salary;
    private Date salaryDate;
    private Boolean isDeleted;

    private User user;
}
