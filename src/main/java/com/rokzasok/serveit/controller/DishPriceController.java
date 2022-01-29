package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishPriceDTOToDishPrice;
import com.rokzasok.serveit.converters.DishPriceToDishPriceDTO;
import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.service.IDishPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/dish-prices")
public class DishPriceController {
    private final IDishPriceService dishPriceService;

    private final DishPriceDTOToDishPrice dishPriceDTOToDishPrice;
    private final DishPriceToDishPriceDTO dishPriceToDishPriceDTO;

    public DishPriceController(IDishPriceService dishPriceService, DishPriceDTOToDishPrice dishPriceDTOToDishPrice, DishPriceToDishPriceDTO dishPriceToDishPriceDTO) {
        this.dishPriceService = dishPriceService;
        this.dishPriceDTOToDishPrice = dishPriceDTOToDishPrice;
        this.dishPriceToDishPriceDTO = dishPriceToDishPriceDTO;
    }

    /***
     * Creates new dish price
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @param dishPriceDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishPriceDTO> create(@RequestBody DishPriceDTO dishPriceDTO) {
        DishPrice dishPrice = dishPriceDTOToDishPrice.convert(dishPriceDTO);

        boolean exists = false;

        if (dishPriceDTO.getId() != null) {
            exists = dishPriceService.findOne(dishPriceDTO.getId()) != null;
        }

        if (exists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        dishPrice = dishPriceService.save(dishPrice);

        return new ResponseEntity<>(dishPriceToDishPriceDTO.convert(dishPrice), HttpStatus.OK);
    }

    /***
     * Gets one dish price by id
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (ONE)
     *
     * @param id id of table
     * @return dishPriceDTO if found //todo , null otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER') || hasRole('ROLE_WAITER')")
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishPriceDTO> one(@PathVariable Integer id) {
        DishPrice dishPrice = dishPriceService.findOne(id);

        if (dishPrice == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(dishPriceToDishPriceDTO.convert(dishPrice), HttpStatus.OK);
    }

    /***
     * Gets all dish prices
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (ALL)
     *
     * @return list of dishPriceDTOs
     *
     */
    @PreAuthorize("hasRole('ROLE_MANAGER') || hasRole('ROLE_WAITER')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DishPriceDTO>> all() {
        List<DishPrice> dishPrices = dishPriceService.findAll();

        List<DishPriceDTO> dishPriceDTOs = dishPriceToDishPriceDTO.convert(dishPrices);
        return new ResponseEntity<>(dishPriceDTOs, HttpStatus.OK);
    }

    /***
     * Edits one dish price
     * author: isidora-stanic
     * authorized: MANAGER
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishPriceDTO> edit(@PathVariable Integer id, @RequestBody DishPriceDTO dishPriceDTO) throws Exception {
//        DishPrice dishPrice = dishPriceService.findOne(dishPriceDTO.getId());
//
//        if (dishPrice == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        DishPrice newDishPrice = dishPriceDTOToDishPrice.convert(dishPriceDTO);
//
//        dishPrice = dishPriceService.save(newDishPrice);
//        dishPriceService.deleteOne(id);
//        return new ResponseEntity<>(dishPriceToDishPriceDTO.convert(dishPrice), HttpStatus.OK);

        DishPrice dp;
        dp = dishPriceService.edit(id, dishPriceDTO);

        return new ResponseEntity<>(dishPriceToDishPriceDTO.convert(dp), HttpStatus.OK);
    }

    /***
     * Deletes one dish price
     * author: isidora-stanic
     * authorized: MANAGER
     * DELETE
     *
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) throws Exception {
        Boolean success;
        try {
            success = dishPriceService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}
