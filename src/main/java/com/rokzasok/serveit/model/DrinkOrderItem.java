package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class DrinkOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private String note;
    private Integer amount;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bartender_id", referencedColumnName = "id")
    private User bartender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "drink_price_id", referencedColumnName = "id")
    private DrinkPrice drink;
}
