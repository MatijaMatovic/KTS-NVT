package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemDTO;
import com.rokzasok.serveit.dto.DishOrderItemDTO;
import com.rokzasok.serveit.dto.OrderItemStatusDTO;
import com.rokzasok.serveit.dto.OrderItemWorkerStatusDTO;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.IDishOrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dish-order-items")
public class DishOrderItemController {
    final IDishOrderItemService dishOrderItemService;
    final DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO;

    public DishOrderItemController(IDishOrderItemService dishOrderItemService, DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO) {
        this.dishOrderItemService = dishOrderItemService;
        this.dishOrderItemToDishOrderItemDTO = dishOrderItemToDishOrderItemDTO;
    }


    //todo: Da li treba raditi proveru da li je prethodni status jela READY? Ili to radimo na frontendu?
    @PutMapping(value="/deliver-item/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> deliverOrderItem(@PathVariable Integer id){
        DishOrderItem dishOrderItem = dishOrderItemService.findOne(id);
        dishOrderItem.setStatus(ItemStatus.DELIVERED);
        dishOrderItem = dishOrderItemService.save(dishOrderItem);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem), HttpStatus.OK);
    }

    @PutMapping(value="/complete-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> completeDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemStatusDTO orderItemStatusDTO){

        Boolean isCompleted = dishOrderItemService.changeStatusDishOrderItem(id, orderItemStatusDTO.getStatus());

        if(isCompleted)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/accept-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> acceptDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerStatusDTO orderItemWorkerStatusDTO){

        Boolean isCompleted = dishOrderItemService.acceptDishOrderItem(id, orderItemWorkerStatusDTO.getStatus(), orderItemWorkerStatusDTO.getWorkerId());

        if(isCompleted)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
