package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.DishOrderItemWithNameDTO;
import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.model.*;
import com.rokzasok.serveit.service.impl.SittingTableService;
import com.rokzasok.serveit.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
public class OrderDTOToOrderConverter implements Converter<OrderDTO, Order> {
    final
    UserService userService;

    final
    DishOrderItemWithNameDTOToDishOrderItem dishConverter;

    final
    DrinkOrderItemDTOToDrinkOrderItem drinkConverter;

    @Autowired
    SittingTableService sittingTableService;

    public OrderDTOToOrderConverter
    (UserService userService, DishOrderItemWithNameDTOToDishOrderItem dishConverter
            , DrinkOrderItemDTOToDrinkOrderItem drinkConverter) {
        this.userService = userService;
        this.dishConverter = dishConverter;
        this.drinkConverter = drinkConverter;
    }

    @Override
    public Order convert(OrderDTO source) {
        Order order = new Order();
        //TODO: check order.setId(source.getId());
        order.setStatus(source.getStatus());
        order.setNote(source.getNote());
        order.setIsDeleted(false);

        User waiter = userService.findOne(source.getWaiterID());

        SittingTable table = sittingTableService.findOne(source.getSittingTable().getId());

        Set<DishOrderItem> dishes = new HashSet<>();
        for (DishOrderItemWithNameDTO dish : source.getDishes()) {
            dishes.add(dishConverter.convert(dish));
        }

        Set<DrinkOrderItem> drinks = new HashSet<>();
        for (DrinkOrderItemDTO drink : source.getDrinks()) {
            drinks.add(drinkConverter.convert(drink));
        }



        order.setWaiter(waiter);
        order.setSittingTable(table);
        order.setDishes(dishes);
        order.setDrinks(drinks);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        order.setCreationDateTime(LocalDateTime.parse(source.getCreationDateTime(), formatter));

        return order;
    }

}
