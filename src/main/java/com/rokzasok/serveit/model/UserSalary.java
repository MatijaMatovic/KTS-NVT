package com.rokzasok.serveit.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@SQLDelete(sql
        = "UPDATE drink "
        + "SET is_deleted = true "
        + "WHERE id = ?")
@Where(clause = "is_deleted = false")
public class UserSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private Double salary;
    private Date salaryDate;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
