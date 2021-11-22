package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class UserSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private Double salary;
    private LocalDate salaryDate;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
