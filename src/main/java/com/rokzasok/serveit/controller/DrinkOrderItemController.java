package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkOrderItemToDrinkOrderItemDTO;
import com.rokzasok.serveit.dto.DrinkOrderItemDTO;
import com.rokzasok.serveit.dto.OrderItemWorkerDTO;
import com.rokzasok.serveit.exceptions.DrinkOrderItemNotFoundException;
import com.rokzasok.serveit.exceptions.ItemStatusSetException;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.ItemStatus;
import com.rokzasok.serveit.service.IDrinkOrderItemService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/drink-order-items")
public class DrinkOrderItemController {
    final IDrinkOrderItemService drinkOrderItemService;
    final DrinkOrderItemToDrinkOrderItemDTO drinkOrderItemToDrinkOrderItemDTO;
    final IUserService userService;


    public DrinkOrderItemController(IDrinkOrderItemService drinkOrderItemService,
                                    DrinkOrderItemToDrinkOrderItemDTO drinkOrderItemToDrinkOrderItemDTO,
                                    IUserService userService) {
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

    /***
     * dodaj opis
     * author: jovana-klimenta
     * authorized: BARTENDER
     * PUT
     *
     * @param orderItemWorkerDTO dto from frontend
     * @return DrinkOrderItemDTO if successful
     */
    @PreAuthorize("hasRole('ROLE_BARTENDER')")
    @PutMapping(value = "/complete-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> completeDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO)
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {
        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.completeDrinkOrderItem(id, orderItemWorkerDTO.getWorkerId(), userService);

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(savedDrinkOrderItem), HttpStatus.OK);
    }

    /***
     * dodaj opis
     * author: jovana-klimenta
     * authorized: BARTENDER
     * PUT
     *
     * @param orderItemWorkerDTO dto from frontend
     * @return DrinkOrderItemDTO if successful
     */
    //@PreAuthorize("hasRole('ROLE_BARTENDER')")
    @PutMapping(value = "/accept-drink-order/{id}", consumes = "application/json")
    public ResponseEntity<DrinkOrderItemDTO> acceptDrinkOrderItem(@PathVariable Integer id, @RequestBody OrderItemWorkerDTO orderItemWorkerDTO)
            throws DrinkOrderItemNotFoundException, UserNotFoundException, ItemStatusSetException {
        DrinkOrderItem savedDrinkOrderItem = drinkOrderItemService.acceptDrinkOrderItem(id, orderItemWorkerDTO.getWorkerId(), userService);

        return new ResponseEntity<>(drinkOrderItemToDrinkOrderItemDTO.convert(savedDrinkOrderItem), HttpStatus.OK);
    }

    /***
     * dodaj opis
     * author: jovana-klimenta
     * authorized: BARTENDER
     * GET
     *
     * @return list of DrinkOrderItemDTO
     */
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

    //------------------------------------------------------------------------------------------------------------------
    @ExceptionHandler({ DrinkOrderItemNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")
    public void handleNotFoundException() {

    }
}
