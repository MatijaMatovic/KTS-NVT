package com.rokzasok.serveit.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
public class DrinkMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private Date date;
    private Boolean isDeleted;

    @ManyToMany
    @JoinTable(name = "drink_menu_prices",
                joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "price_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<DrinkPrice> drinks = new HashSet<>();
}
