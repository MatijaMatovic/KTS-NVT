package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishPriceDTOToDishPrice;
import com.rokzasok.serveit.converters.DishPriceToDishPriceDTO;
import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.service.IDishPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> create(@RequestBody DishPriceDTO dishPriceDTO) {
        DishPrice dishPrice = dishPriceDTOToDishPrice.convert(dishPriceDTO);
        DishPrice dishPriceSaved = dishPriceService.save(dishPrice);
        if (dishPriceSaved == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
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
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishPriceDTO> one(@PathVariable Integer id) {
        DishPrice dishPrice = dishPriceService.findOne(id);

        if (dishPrice == null){
            System.out.println("Dish menu je null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DishPriceDTO dishPriceDTO = dishPriceToDishPriceDTO.convert(dishPrice);
        return new ResponseEntity<>(dishPriceDTO, HttpStatus.OK);
    }

    /***
     * Gets all dish prices
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (ALL)
     *
     * @return list of dishPriceDTOs
     */
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
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishPriceDTO> edit(@PathVariable Integer id, @RequestBody DishPriceDTO dishPriceDTO) {
        DishPrice dishPrice = dishPriceService.findOne(dishPriceDTO.getId());

        if (dishPrice == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DishPrice newDishPrice = dishPriceDTOToDishPrice.convert(dishPriceDTO);

        dishPrice = dishPriceService.save(newDishPrice);
        return new ResponseEntity<>(dishPriceToDishPriceDTO.convert(dishPrice), HttpStatus.OK);
    }

    /***
     * Deletes one dish price
     * author: isidora-stanic
     * authorized: MANAGER
     * DELETE
     *
     * @return true if successful, false otherwise
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        Boolean success;
        try {
            success = dishPriceService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}
