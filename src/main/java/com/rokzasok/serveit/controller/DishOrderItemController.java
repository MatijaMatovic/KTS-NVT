package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemDTO;
import com.rokzasok.serveit.dto.*;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.service.IDishOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/dish-order-items")
public class DishOrderItemController {
    final IDishOrderItemService dishOrderItemService;
    final DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO;
    final IUserService userService;

    public DishOrderItemController(IDishOrderItemService dishOrderItemService, DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO, IUserService userService) {
        this.dishOrderItemService = dishOrderItemService;
        this.dishOrderItemToDishOrderItemDTO = dishOrderItemToDishOrderItemDTO;
        this.userService = userService;
    }


    // @PreAuthorize("hasRole('ROLE_WAITER')")
    @PutMapping(value = "/deliver-item/{id}")
    public ResponseEntity<DishOrderItemDTO> deliverOrderItem(@PathVariable Integer id) {
        DishOrderItem dishOrderItem = dishOrderItemService.findOne(id);

        if (dishOrderItem.getStatus() != ItemStatus.READY) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        dishOrderItem.setStatus(ItemStatus.DELIVERED);
        dishOrderItem = dishOrderItemService.save(dishOrderItem);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COOK')")
    @PutMapping(value = "/complete-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> completeDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO) {
        DishOrderItem dishOrderItem = dishOrderItemService.findOne(id);
        if (dishOrderItem == null || !Objects.equals(id, orderItemWorkerDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User cook = userService.findOne(orderItemWorkerDTO.getWorkerId());
        if (cook == null || !Objects.equals(cook.getId(), dishOrderItem.getCook().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        dishOrderItem.setStatus(ItemStatus.READY);
        DishOrderItem savedDishOrderItem = dishOrderItemService.save(dishOrderItem);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(savedDishOrderItem), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_COOK')")
    @PutMapping(value = "/accept-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> acceptDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO) {

        DishOrderItem dishOrderItem = dishOrderItemService.findOne(orderItemWorkerDTO.getId());
        if (dishOrderItem == null || !Objects.equals(id, orderItemWorkerDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User cook = userService.findOne(orderItemWorkerDTO.getWorkerId());
        if (cook == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        dishOrderItem.setCook(cook);
        dishOrderItem.setStatus(ItemStatus.IN_PROGRESS);

        DishOrderItem savedDishOrderItem = dishOrderItemService.save(dishOrderItem);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(savedDishOrderItem), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_COOK')")
    @GetMapping(value = "/cook-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DishOrderItemDTO>> getCookOrders(@PathVariable Integer id) {
        List<DishOrderItem> orders = dishOrderItemService.findAllByCookID(id);
        List<DishOrderItemDTO> ordersDTO = new ArrayList<>();
        for (DishOrderItem dishOrderItem : orders) {
            ordersDTO.add(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem));
        }
        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }
}
