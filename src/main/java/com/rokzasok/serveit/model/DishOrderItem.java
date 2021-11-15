package com.rokzasok.serveit.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

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
        = "UPDATE dish_order_item "
        + "SET is_deleted = true "
        + "WHERE id = ?")
@Where(clause = "is_deleted=false")
public class DishOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private String note;
    private Integer amount;
    private Integer priority;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cook_id", referencedColumnName = "id")
    private User cook;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_price_id", referencedColumnName = "id")
    private DishPrice dish;
}
