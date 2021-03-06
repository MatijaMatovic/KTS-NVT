package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemWithNameDTO;
import com.rokzasok.serveit.converters.SittingTableToSittingTableDTO;
import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemDTO;
import com.rokzasok.serveit.converters.DrinkOrderItemToDrinkOrderItemDTO;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    /*@EqualsAndHashCode.Include
    private Integer id;

    private OrderStatus status;
    private String creationDateTime;
    private String note;
    private Integer sittingTableID;
    private Integer waiterID;
    private List<DishOrderItemDTO> dishes = new ArrayList<>();
    private List<DrinkOrderItemDTO> drinks = new ArrayList<>();

    // TODO ovo mora u converter
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.note = order.getNote();
        this.sittingTableID = order.getSittingTable().getId();
        this.waiterID = order.getWaiter().getId();

        DishOrderItemToDishOrderItemDTO dish_converter
                = new DishOrderItemToDishOrderItemDTO();
        for (DishOrderItem dish : order.getDishes())
            dishes.add(dish_converter.convert(dish));

        DrinkOrderItemToDrinkOrderItemDTO drink_converter
                = new DrinkOrderItemToDrinkOrderItemDTO();
        for (DrinkOrderItem drink : order.getDrinks())
            drinks.add(drink_converter.convert(drink));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.creationDateTime = order.getCreationDateTime().format(formatter);
    }*/
/*
    private LocalDateTime creationDateTime;
    private Integer tableId;

    //todo: da li je ovo najbolje re??enje?
    private List<DishPriceDTO> orderDishesId;
    private List<DrinkPriceDTO> orderDrinksId;

    private Integer waiterId;
*/

    @EqualsAndHashCode.Include
    private Integer id;
    private OrderStatus status;
    private String note;
    private String creationDateTime;
    private SittingTableDTO sittingTable;
    private Integer waiterID;
    private List<DishOrderItemWithNameDTO> dishes = new ArrayList<>();
    private List<DrinkOrderItemDTO> drinks = new ArrayList<>();

    // TODO ovo mora u converter
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.note = order.getNote();

        DishOrderItemToDishOrderItemWithNameDTO dish_converter
                = new DishOrderItemToDishOrderItemWithNameDTO();
        for (DishOrderItem dish : order.getDishes())
            dishes.add(dish_converter.convert(dish));

        DrinkOrderItemToDrinkOrderItemDTO drink_converter
                = new DrinkOrderItemToDrinkOrderItemDTO();
        for (DrinkOrderItem drink : order.getDrinks())
            drinks.add(drink_converter.convert(drink));

        this.waiterID = order.getWaiter().getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.creationDateTime = order.getCreationDateTime().format(formatter);

        SittingTableToSittingTableDTO sittingTableConverter = new SittingTableToSittingTableDTO();
        this.sittingTable = sittingTableConverter.convert(order.getSittingTable());
    }
}
