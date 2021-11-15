package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.dto.OrderDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.Order;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OrderDTOToOrderConverter implements Converter<OrderDTO, Order> {
    final
    UserService userService;

    final
    DishOrderItemDTOToDishOrderItem dishConverter;

    final
    DrinkOrderItemDTOToDrinkOrderItem drinkConverter;

    public OrderDTOToOrderConverter
    (UserService userService, DishOrderItemDTOToDishOrderItem dishConverter
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

        Set<DishOrderItem> dishes = new HashSet<>();
        for (DishOrderItemDTO dish : source.getDishes()) {
            dishes.add(dishConverter.convert(dish));
        }

        Set<DrinkOrderItem> drinks = new HashSet<>();
        for (DrinkOrderItemDTO drink : source.getDrinks()) {
            drinks.add(drinkConverter.convert(drink));
        }

        order.setWaiter(waiter);
        order.setDishes(dishes);
        order.setDrinks(drinks);

        return order;
    }

}
