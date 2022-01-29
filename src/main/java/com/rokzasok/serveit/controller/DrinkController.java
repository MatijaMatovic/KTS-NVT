package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkDTOtoDrink;
import com.rokzasok.serveit.converters.DrinkToDrinkDTO;
import com.rokzasok.serveit.dto.DrinkDTO;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.service.IDrinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Drink controller.
 */
@RestController
@RequestMapping("api/drinks")
public class DrinkController {
    /**
     * The Drink service.
     */
    final IDrinkService drinkService;
    /**
     * The Drink to drink dto.
     */
    final DrinkToDrinkDTO drinkToDrinkDTO;
    /**
     * The Drink dto to drink.
     */
    final DrinkDTOtoDrink drinkDTOtoDrink;


    /**
     * Instantiates a new Drink controller.
     *
     * @param drinkService    the drink service
     * @param drinkToDrinkDTO the drink to drink dto
     * @param drinkDTOtoDrink the drink dt oto drink
     */
    public DrinkController(IDrinkService drinkService, DrinkToDrinkDTO drinkToDrinkDTO, DrinkDTOtoDrink drinkDTOtoDrink) {
        this.drinkService = drinkService;
        this.drinkToDrinkDTO = drinkToDrinkDTO;
        this.drinkDTOtoDrink = drinkDTOtoDrink;
    }

    /**
     * Gets drinks.
     *
     * @return the drinks
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping
    public ResponseEntity<List<DrinkDTO>> getDrinks() {
        List<Drink> drinks = drinkService.findAll();

        List<DrinkDTO> dtoDrinks = drinkToDrinkDTO.convert(drinks);

        return new ResponseEntity<>(dtoDrinks, HttpStatus.OK);

    }

    /**
     * Gets drink.
     *
     * @param id the id
     * @return the drink
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<DrinkDTO> getDrink(@PathVariable Integer id) {
        Drink drink = drinkService.findOne(id);

        if (drink == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(drinkToDrinkDTO.convert(drink), HttpStatus.OK);
    }

    /**
     * Save drink.
     *
     * @param drinkDTO the drink dto
     * @return the response entity
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<DrinkDTO> addNewDrink(@RequestBody DrinkDTO drinkDTO) {
        Drink drink = drinkDTOtoDrink.convert(drinkDTO);
        boolean exists = false;

        if (drinkDTO.getId() != null) {
            exists = drinkService.findOne(drinkDTO.getId()) != null;
        }

        if (exists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        drink = drinkService.save(drink);

        return new ResponseEntity<>(drinkToDrinkDTO.convert(drink), HttpStatus.OK);
    }

    /**
     * Update drink.
     *
     * @param drinkDTO the drink dto
     * @return the response entity
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping(consumes = "application/json")
    public ResponseEntity<DrinkDTO> updateDrink(@RequestBody DrinkDTO drinkDTO) {
        Drink drink = drinkService.findOne(drinkDTO.getId());

        if (drink == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Drink newDrink = drinkDTOtoDrink.convert(drinkDTO);

        drink = drinkService.save(newDrink);
        return new ResponseEntity<>(drinkToDrinkDTO.convert(drink), HttpStatus.OK);
    }

    /**
     * Delete drink.
     *
     * @param id the id
     * @return the response entity
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteDrink(@PathVariable Integer id) {
        boolean success = false;
        try {
            success = drinkService.deleteOne(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(success, success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }


}
