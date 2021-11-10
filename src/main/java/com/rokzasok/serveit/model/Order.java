package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime creationDateTime;
    private String note;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private Table table;

    @ManyToMany
    @JoinTable(name = "order_dishes",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"))
    private Set<DishOrderItem> dishes;

    @ManyToMany
    @JoinTable(name = "order_drinks",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"))
    private Set<DrinkOrderItem> drinks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waiter_id", referencedColumnName = "id")
    private User waiter;
}
