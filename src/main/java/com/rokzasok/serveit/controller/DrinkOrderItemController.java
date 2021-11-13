package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkOrderItemToDrinkOrderItemDTO;
import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.dto.OrderItemStatusDTO;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.IDrinkOrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/drink-order-items")
public class DrinkOrderItemController {
    final IDrinkOrderItemService drinkOrderItemService;
    final DrinkOrderItemToDrinkOrderItemDTO drinkOrderItemToDrinkOrderItemDTO;


    public DrinkOrderItemController(IDrinkOrderItemService drinkOrderItemService, DrinkOrderItemToDrinkOrderItemDTO drinkOrderItemToDrinkOrderItemDTO) {
        this.drinkOrderItemService = drinkOrderItemService;
        this.drinkOrderItemToDrinkOrderItemDTO = drinkOrderItemToDrinkOrderItemDTO;
    }

    //todo: Da li treba raditi proveru da li je prethodni status pića READY? Ili to radimo na frontendu?
    @PutMapping(value = "/deliver-item/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> deliverOrderItem(@PathVariable Integer id) {
        DrinkOrderItem drinkOrderItem = drinkOrderItemService.findOne(id);
        drinkOrderItem.setStatus(ItemStatus.DELIVERED);
        drinkOrderItem = drinkOrderItemService.save(drinkOrderItem);

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(drinkOrderItem), HttpStatus.OK);
    }

    @PutMapping(value="/complete-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> completeDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemStatusDTO orderItemStatusDTO){

        Boolean isCompleted = drinkOrderItemService.changeStatusDrinkOrderItem(id, orderItemStatusDTO.getStatus());

        if(isCompleted)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/accept-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> acceptDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemStatusDTO orderItemStatusDTO){

        Boolean isCompleted = drinkOrderItemService.changeStatusDrinkOrderItem(id, orderItemStatusDTO.getStatus());

        if(isCompleted)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }


}
