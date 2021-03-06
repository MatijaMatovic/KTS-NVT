package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemDTO;
import com.rokzasok.serveit.converters.DishOrderItemToDishOrderItemWithNameDTO;
import com.rokzasok.serveit.dto.*;
import com.rokzasok.serveit.exceptions.DishOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.IDishOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/dish-order-items")
public class DishOrderItemController {
    final IDishOrderItemService dishOrderItemService;
    final DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO;
    final DishOrderItemToDishOrderItemWithNameDTO dishOrderItemToDishOrderItemWithNameDTO;
    final IUserService userService;

    public DishOrderItemController(IDishOrderItemService dishOrderItemService,
                                   DishOrderItemToDishOrderItemDTO dishOrderItemToDishOrderItemDTO,
                                   DishOrderItemToDishOrderItemWithNameDTO dishOrderItemToDishOrderItemWithNameDTO,
                                   IUserService userService) {
        this.dishOrderItemService = dishOrderItemService;
        this.dishOrderItemToDishOrderItemDTO = dishOrderItemToDishOrderItemDTO;
        this.dishOrderItemToDishOrderItemWithNameDTO = dishOrderItemToDishOrderItemWithNameDTO;
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ROLE_WAITER')")
    @PutMapping(value = "/deliver-item/{id}")
    public ResponseEntity<DishOrderItemDTO> deliverOrderItem(@PathVariable Integer id) {
        DishOrderItem dishOrderItem = dishOrderItemService.findOne(id);

        if (dishOrderItem.getStatus() != ItemStatus.READY){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        dishOrderItem.setStatus(ItemStatus.DELIVERED);
        dishOrderItem = dishOrderItemService.save(dishOrderItem);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(dishOrderItem), HttpStatus.OK);
    }

    /***
     * dodaj opis
     * author: jovana-klimenta
     * authorized: COOK
     * PUT
     *
     * @param orderItemWorkerDTO dto from frontend
     * @return DishOrderItemDTO if successful
     */
    @PreAuthorize("hasRole('ROLE_COOK')")
    @PutMapping(value = "/complete-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> completeDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO)
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DishOrderItem savedDishOrderItem = dishOrderItemService.completeDishOrderItem(id, orderItemWorkerDTO.getWorkerId(), userService);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(savedDishOrderItem), HttpStatus.OK);

    }

    /***
     * dodaj opis
     * author: jovana-klimenta
     * authorized: COOK
     * PUT
     *
     * @param orderItemWorkerDTO dto from frontend
     * @return DishOrderItemDTO if successful
     */
    @PreAuthorize("hasRole('ROLE_COOK')")
    @PutMapping(value = "/accept-dish-order/{id}", consumes = "application/json")
    public ResponseEntity<DishOrderItemDTO> acceptDishOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO)
            throws DishOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {

        DishOrderItem savedDishOrderItem = dishOrderItemService.acceptDishOrderItem(id, orderItemWorkerDTO.getWorkerId(), userService);

        return new ResponseEntity<>(dishOrderItemToDishOrderItemDTO.convert(savedDishOrderItem), HttpStatus.OK);
    }


    @PutMapping(value = "/deliver-dish-order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_WAITER')")
    public ResponseEntity<DishOrderItemWithNameDTO> deliverDishOrderItem(@PathVariable Integer id)
            throws ItemStatusSetException, DishOrderItemNotFoundException {
        DishOrderItem deliveredDishOrderItem = dishOrderItemService.deliverDishOrderItem(id);

        return new ResponseEntity<>(
                dishOrderItemToDishOrderItemWithNameDTO.convert(deliveredDishOrderItem),
                HttpStatus.OK
        );
    }

    /***
     * dodaj opis
     * author: jovana-klimenta
     * authorized: COOK
     * GET
     *
     * @return list of DishOrderItemDTO
     */
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

    //------------------------------------------------------------------------------------------------------------------
    @ExceptionHandler({ DishOrderItemNotFoundException.class, UserNotFoundException.class, ItemStatusSetException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request")
    public void handleNotFoundException() {

    }
}
