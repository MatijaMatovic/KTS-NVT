package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkMenuDTOToDrinkMenu;
import com.rokzasok.serveit.converters.DrinkMenuToDrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.service.IDrinkMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/drink-menu")
public class DrinkMenuController {
    @Autowired
    IDrinkMenuService drinkMenuService;

    @Autowired
    DrinkMenuDTOToDrinkMenu drinkMenuDTOToDrinkMenuConverter;
    @Autowired
    DrinkMenuToDrinkMenuDTO drinkMenuToDrinkMenuDTOConverter;

    /***
     * Creates new drink menu
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @param drinkMenuDTO dto from frontend
     * @return true if username is free, false otherwise
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> create(@RequestBody DrinkMenuDTO drinkMenuDTO) {
        DrinkMenu drinkMenu = drinkMenuDTOToDrinkMenuConverter.convert(drinkMenuDTO);
        DrinkMenu drinkMenuSaved = drinkMenuService.save(drinkMenu);
        if (drinkMenuSaved == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /***
     * Gets one drink menu by id
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ONE)
     *
     * @param id id of table
     * @return drinkMenuDTO if found, null otherwise
     */
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> one(@PathVariable Integer id) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(id);

        if (drinkMenu == null){
            System.out.println("Drink menu je null");
            return new ResponseEntity<>(null, HttpStatus.OK); // TODO NOT_FOUND?
        }
        DrinkMenuDTO drinkMenuDTO = drinkMenuToDrinkMenuDTOConverter.convert(drinkMenu);
        if (drinkMenuDTO == null) {
            System.out.println("Drink menu DTO je null");
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(drinkMenuDTO, HttpStatus.OK);
    }

    /***
     * Gets all drink menus
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ALL)
     *
     * @return list of drinkMenuDTOs
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DrinkMenuDTO>> all() {
        List<DrinkMenu> drinkMenus = drinkMenuService.findAll();

        List<DrinkMenuDTO> drinkMenuDTOs = drinkMenuToDrinkMenuDTOConverter.convert(drinkMenus);
        return new ResponseEntity<>(drinkMenuDTOs, HttpStatus.OK);
    }

    // TODO: GET LATEST DRINK MENU ____ WAITER

    /***
     * Edits one drink menu
     * author: isidora-stanic
     * authorized: MANAGER
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> edit(@PathVariable Integer id, @RequestBody DrinkMenuDTO drinkMenuDTO) {
        DrinkMenu table;
        try {
            table = drinkMenuService.edit(id, drinkMenuDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(drinkMenuToDrinkMenuDTOConverter.convert(table), HttpStatus.OK);
    }

    /***
     * Deletes one drink menu
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
            success = drinkMenuService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}
