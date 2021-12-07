package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkOrderItemToDrinkOrderItemDTO;
import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.dto.OrderItemStatusDTO;
import com.rokzasok.serveit.dto.OrderItemWorkerStatusDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.IDrinkOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import com.rokzasok.serveit.service.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    //todo: Da li treba raditi proveru da li je prethodni status pića READY? Ili to radimo na frontendu?
    @PutMapping(value = "/deliver-item/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> deliverOrderItem(@PathVariable Integer id) {
        DrinkOrderItem drinkOrderItem = drinkOrderItemService.findOne(id);
        drinkOrderItem.setStatus(ItemStatus.DELIVERED);
        drinkOrderItem = drinkOrderItemService.save(drinkOrderItem);

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(drinkOrderItem), HttpStatus.OK);
    }

    //TODO:jos malo pogledati koju poruku vraca u slucaju greske
    @PutMapping(value="/complete-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> completeDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemStatusDTO orderItemStatusDTO){
        DrinkOrderItem drinkOrderItem;
        try {
            drinkOrderItem = drinkOrderItemService.changeStatusDrinkOrderItem(id, orderItemStatusDTO.getStatus());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(drinkOrderItem), HttpStatus.OK);
    }

    //TODO:jos malo pogledati koju poruku vraca u slucaju greske
    @PutMapping(value="/accept-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> acceptDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerStatusDTO orderItemWorkerStatusDTO){
        DrinkOrderItem drinkOrderItem;
        try {
            drinkOrderItem = drinkOrderItemService.acceptDrinkOrderItem(id, orderItemWorkerStatusDTO.getStatus(),
                    orderItemWorkerStatusDTO.getWorkerId(), userService);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(drinkOrderItem), HttpStatus.OK);

    }

    @GetMapping(value = "/bartender-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DrinkOrderItemDTO>> getCookOrders(@PathVariable Integer id) {
        List<DrinkOrderItem> orders = drinkOrderItemService.findAllByBartenderID(id);
        List<DrinkOrderItemDTO> ordersDTO = new ArrayList<>();
        for (DrinkOrderItem drinkOrderItem : orders)
        {
            ordersDTO.add(drinkOrderItemToDrinkOrderItemDTO.convert(drinkOrderItem));
        }
        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

}
