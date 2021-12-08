package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkOrderItemToDrinkOrderItemDTO;
import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.service.IDrinkOrderItemService;
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
@RequestMapping("api/drink-order-items")
public class DrinkOrderItemController {
    final IDrinkOrderItemService drinkOrderItemService;
    final DrinkOrderItemToDrinkOrderItemDTO drinkOrderItemToDrinkOrderItemDTO;
    final IUserService userService;


    public DrinkOrderItemController(IDrinkOrderItemService drinkOrderItemService, DrinkOrderItemToDrinkOrderItemDTO drinkOrderItemToDrinkOrderItemDTO, IUserService userService) {
        this.drinkOrderItemService = drinkOrderItemService;
        this.drinkOrderItemToDrinkOrderItemDTO = drinkOrderItemToDrinkOrderItemDTO;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_WAITER')")
    @PutMapping(value = "/deliver-item/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> deliverOrderItem(@PathVariable Integer id) {
        DrinkOrderItem drinkOrderItem = drinkOrderItemService.findOne(id);

        if (drinkOrderItem.getStatus() != ItemStatus.READY) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        drinkOrderItem.setStatus(ItemStatus.DELIVERED);
        drinkOrderItem = drinkOrderItemService.save(drinkOrderItem);

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(drinkOrderItem), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_BARTENDER')")
    @PutMapping(value = "/complete-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> completeDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO) {
        DrinkOrderItem drinkOrderItem = drinkOrderItemService.findOne(id);
        if (drinkOrderItem == null || !Objects.equals(id, orderItemWorkerDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User bartender = userService.findOne(orderItemWorkerDTO.getWorkerId());
        if (bartender == null || !Objects.equals(bartender.getId(), drinkOrderItem.getBartender().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        drinkOrderItem.setStatus(ItemStatus.READY);
        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(drinkOrderItem);

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(savedDrinkOrderItem), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_BARTENDER')")
    @PutMapping(value = "/accept-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> acceptDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO) {

        DrinkOrderItem drinkOrderItem = drinkOrderItemService.findOne(orderItemWorkerDTO.getId());
        if (drinkOrderItem == null || !Objects.equals(id, orderItemWorkerDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User bartender = userService.findOne(orderItemWorkerDTO.getWorkerId());
        if (bartender == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        drinkOrderItem.setBartender(bartender);
        drinkOrderItem.setStatus(ItemStatus.IN_PROGRESS);

        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.save(drinkOrderItem);

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(savedDrinkOrderItem), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_BARTENDER')")
    @GetMapping(value = "/bartender-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DrinkOrderItemDTO>> getBartenderOrders(@PathVariable Integer id) {
        List<DrinkOrderItem> orders = drinkOrderItemService.findAllByBartenderID(id);
        List<DrinkOrderItemDTO> ordersDTO = new ArrayList<>();
        for (DrinkOrderItem drinkOrderItem : orders) {
            ordersDTO.add(drinkOrderItemToDrinkOrderItemDTO.convert(drinkOrderItem));
        }
        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

}
