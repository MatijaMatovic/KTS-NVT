package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemDTO;
import com.rokzasok.serveit.converters.DrinkDTOtoDrink;
import com.rokzasok.serveit.dto.*;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.IDishOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import com.rokzasok.serveit.service.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/dish-order-items")
public class DishOrderItemController {
    final IDishOrderItemService dishOrderItemService;
    final DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO;
    final IUserService userService;

    public DishOrderItemController(IDishOrderItemService dishOrderItemService, DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO, DrinkDTOtoDrink drinkDTOtoDrink, IUserService userService) {
        this.dishOrderItemService = dishOrderItemService;
        this.dishOrderItemToDishOrderItemDTO = dishOrderItemToDishOrderItemDTO;
        this.userService = userService;
    }


    //todo: Da li treba raditi proveru da li je prethodni status jela READY? Ili to radimo na frontendu?
    @PutMapping(value="/deliver-item/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> deliverOrderItem(@PathVariable Integer id){
        DishOrderItem dishOrderItem = dishOrderItemService.findOne(id);
        dishOrderItem.setStatus(ItemStatus.DELIVERED);
        dishOrderItem = dishOrderItemService.save(dishOrderItem);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem), HttpStatus.OK);
    }

    //TODO:jos malo pogledati koju poruku vraca u slucaju greske
    @PutMapping(value="/complete-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> completeDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemStatusDTO orderItemStatusDTO){
        DishOrderItem dishOrderItem;
        try {
        dishOrderItem = dishOrderItemService.changeStatusDishOrderItem(id, orderItemStatusDTO.getStatus());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem), HttpStatus.OK);

    }

    //TODO:jos malo pogledati koju poruku vraca u slucaju greske
    @PutMapping(value="/accept-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> acceptDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerStatusDTO orderItemWorkerStatusDTO){

        DishOrderItem dishOrderItem;
        try {
        dishOrderItem = dishOrderItemService.acceptDishOrderItem(id, orderItemWorkerStatusDTO.getStatus(), orderItemWorkerStatusDTO.getWorkerId(), userService);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COOK')")
    @GetMapping(value = "/cook-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DishOrderItemDTO>> getWaiterOrders(@PathVariable Integer id) {
        List<DishOrderItem> orders = dishOrderItemService.findAllByCookID(id);
        List<DishOrderItemDTO> ordersDTO = new ArrayList<>();
        for (DishOrderItem dishOrderItem : orders)
        {
            ordersDTO.add(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem));
        }
        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }
}
