package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSalaryDTO {
    @EqualsAndHashCode.Include
    private Integer id;
    private Double salary;
    private LocalDate salaryDate;
    private Integer userId;
}
