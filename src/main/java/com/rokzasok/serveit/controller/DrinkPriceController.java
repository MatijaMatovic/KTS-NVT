package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkPriceDTOToDrinkPrice;
import com.rokzasok.serveit.converters.DrinkPriceToDrinkPriceDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.IDrinkPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/drink-prices")
public class DrinkPriceController {
    @Autowired
    IDrinkPriceService drinkPriceService;


    @Autowired
    DrinkPriceDTOToDrinkPrice drinkPriceDTOToDrinkPrice;
    @Autowired
    DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO;

    /***
     * Creates new drink price
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @param drinkPriceDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> create(@RequestBody DrinkPriceDTO drinkPriceDTO) {
        DrinkPrice drinkPrice = drinkPriceDTOToDrinkPrice.convert(drinkPriceDTO);
        DrinkPrice drinkPriceSaved = drinkPriceService.save(drinkPrice);
        if (drinkPriceSaved == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /***
     * Gets one drink price by id
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (ONE)
     *
     * @param id id of table
     * @return drinkPriceDTO if found, null otherwise
     */
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkPriceDTO> one(@PathVariable Integer id) {
        DrinkPrice drinkPrice = drinkPriceService.findOne(id);

        if (drinkPrice == null){
            System.out.println("Drink menu je null");
            return new ResponseEntity<>(null, HttpStatus.OK); // TODO NOT_FOUND?
        }
        DrinkPriceDTO drinkPriceDTO = drinkPriceToDrinkPriceDTO.convert(drinkPrice);
        if (drinkPriceDTO == null) {
            System.out.println("Drink menu DTO je null");
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(drinkPriceDTO, HttpStatus.OK);
    }

    /***
     * Gets all drink prices
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (ALL)
     *
     * @return list of drinkPriceDTOs
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DrinkPriceDTO>> all() {
        List<DrinkPrice> drinkPrices = drinkPriceService.findAll();

        List<DrinkPriceDTO> drinkPriceDTOs = drinkPriceToDrinkPriceDTO.convert(drinkPrices);
        return new ResponseEntity<>(drinkPriceDTOs, HttpStatus.OK);
    }

    /***
     * Edits one drink price
     * author: isidora-stanic
     * authorized: MANAGER
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkPriceDTO> edit(@PathVariable Integer id, @RequestBody DrinkPriceDTO drinkPriceDTO) {
        DrinkPrice drinkPrice = drinkPriceService.findOne(drinkPriceDTO.getId());

        if (drinkPrice == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DrinkPrice newDrinkPrice = drinkPriceDTOToDrinkPrice.convert(drinkPriceDTO);

        drinkPrice = drinkPriceService.save(newDrinkPrice);
        return new ResponseEntity<>(drinkPriceToDrinkPriceDTO.convert(drinkPrice), HttpStatus.OK);
    }

    /***
     * Deletes one drink price
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
            success = drinkPriceService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }


}
