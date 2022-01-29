package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkPriceDTOToDrinkPrice;
import com.rokzasok.serveit.converters.DrinkPriceToDrinkPriceDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.IDrinkPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/drink-prices")
public class DrinkPriceController {
    private final IDrinkPriceService drinkPriceService;

    private final DrinkPriceDTOToDrinkPrice drinkPriceDTOToDrinkPrice;
    private final DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO;

    public DrinkPriceController(IDrinkPriceService drinkPriceService, DrinkPriceDTOToDrinkPrice drinkPriceDTOToDrinkPrice, DrinkPriceToDrinkPriceDTO drinkPriceToDrinkPriceDTO) {
        this.drinkPriceService = drinkPriceService;
        this.drinkPriceDTOToDrinkPrice = drinkPriceDTOToDrinkPrice;
        this.drinkPriceToDrinkPriceDTO = drinkPriceToDrinkPriceDTO;
    }

    /***
     * Creates new drink price
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @param drinkPriceDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkPriceDTO> create(@RequestBody DrinkPriceDTO drinkPriceDTO) {
        DrinkPrice drinkPrice = drinkPriceDTOToDrinkPrice.convert(drinkPriceDTO);

        boolean exists = false;

        if (drinkPriceDTO.getId() != null) {
            exists = drinkPriceService.findOne(drinkPriceDTO.getId()) != null;
        }

        if (exists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        drinkPrice = drinkPriceService.save(drinkPrice);

        return new ResponseEntity<>(drinkPriceToDrinkPriceDTO.convert(drinkPrice), HttpStatus.OK);
    }

    /***
     * Gets one drink price by id
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (ONE)
     *
     * @param id id of table
     * @return drinkPriceDTO if found //todo , null otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER') || hasRole('ROLE_WAITER')")
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkPriceDTO> one(@PathVariable Integer id) {
        DrinkPrice drinkPrice = drinkPriceService.findOne(id);

        if (drinkPrice == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(drinkPriceToDrinkPriceDTO.convert(drinkPrice), HttpStatus.OK);
    }

    /***
     * Gets all drink prices
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (ALL)
     *
     * @return list of drinkPriceDTOs
     */
    @PreAuthorize("hasRole('ROLE_MANAGER') || hasRole('ROLE_WAITER')")
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
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkPriceDTO> edit(@PathVariable Integer id, @RequestBody DrinkPriceDTO drinkPriceDTO) throws Exception {
//        DrinkPrice drinkPrice = drinkPriceService.findOne(drinkPriceDTO.getId());
//
//        if (drinkPrice == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        DrinkPrice newDrinkPrice = drinkPriceDTOToDrinkPrice.convert(drinkPriceDTO);
//
//        drinkPrice = drinkPriceService.save(newDrinkPrice);
//        return new ResponseEntity<>(drinkPriceToDrinkPriceDTO.convert(drinkPrice), HttpStatus.OK);
        DrinkPrice dp;
        dp = drinkPriceService.edit(id, drinkPriceDTO);

        return new ResponseEntity<>(drinkPriceToDrinkPriceDTO.convert(dp), HttpStatus.OK);
    }

    /***
     * Deletes one drink price
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
            success = drinkPriceService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}
