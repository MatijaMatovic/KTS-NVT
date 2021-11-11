package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@Table(name = "orders") //mora jer je "order" keyword
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime creationDateTime;
    private String note;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private SittingTable sittingTable;

    @ManyToMany
    @JoinTable(name = "order_dishes",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<DishOrderItem> dishes;

    @ManyToMany
    @JoinTable(name = "order_drinks",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<DrinkOrderItem> drinks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waiter_id", referencedColumnName = "id")
    private User waiter;
}
