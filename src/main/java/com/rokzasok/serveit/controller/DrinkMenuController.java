package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkDTOtoDrink;
import com.rokzasok.serveit.converters.DrinkMenuDTOToDrinkMenu;
import com.rokzasok.serveit.converters.DrinkMenuToDrinkMenuDTO;
import com.rokzasok.serveit.converters.DrinkPriceDTOToDrinkPrice;
import com.rokzasok.serveit.dto.DrinkDTO;
import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.IDrinkMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/drink-menu")
public class DrinkMenuController {
    private final IDrinkMenuService drinkMenuService;

    private final DrinkMenuDTOToDrinkMenu drinkMenuDTOToDrinkMenu;
    private final DrinkMenuToDrinkMenuDTO drinkMenuToDrinkMenuDTO;

    private final DrinkDTOtoDrink drinkDTOtoDrink;

    private final DrinkPriceDTOToDrinkPrice drinkPriceDTOToDrinkPrice;

    public DrinkMenuController(IDrinkMenuService drinkMenuService,
                               DrinkMenuDTOToDrinkMenu drinkMenuDTOToDrinkMenu,
                               DrinkMenuToDrinkMenuDTO drinkMenuToDrinkMenuDTO,
                               DrinkDTOtoDrink drinkDTOtoDrink, DrinkPriceDTOToDrinkPrice drinkPriceDTOToDrinkPrice) {
        this.drinkMenuService = drinkMenuService;
        this.drinkMenuDTOToDrinkMenu = drinkMenuDTOToDrinkMenu;
        this.drinkMenuToDrinkMenuDTO = drinkMenuToDrinkMenuDTO;
        this.drinkDTOtoDrink = drinkDTOtoDrink;
        this.drinkPriceDTOToDrinkPrice = drinkPriceDTOToDrinkPrice;
    }

    /***
     * Creates new drink menu
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @param drinkMenuDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> create(@RequestBody DrinkMenuDTO drinkMenuDTO) {
        DrinkMenu drinkMenu = drinkMenuDTOToDrinkMenu.convert(drinkMenuDTO);
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
     * @param id id of drink menu
     * @return drinkMenuDTO if found, null otherwise
     */
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> one(@PathVariable Integer id) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(id);

        if (drinkMenu == null){
            System.out.println("Drink menu je null");
            return new ResponseEntity<>(null, HttpStatus.OK); // TODO NOT_FOUND?
        }
        DrinkMenuDTO drinkMenuDTO = drinkMenuToDrinkMenuDTO.convert(drinkMenu);
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

        List<DrinkMenuDTO> drinkMenuDTOs = drinkMenuToDrinkMenuDTO.convert(drinkMenus);
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
        /*
        DrinkMenu drinkMenu;
        try {
            drinkMenu = drinkMenuService.edit(id, drinkMenuDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(drinkMenu), HttpStatus.OK);*/
        DrinkMenu drinkMenu = drinkMenuService.findOne(drinkMenuDTO.getId());

        if (drinkMenu == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DrinkMenu newDrinkMenu = drinkMenuDTOToDrinkMenu.convert(drinkMenuDTO);

        drinkMenu = drinkMenuService.save(newDrinkMenu);
        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(drinkMenu), HttpStatus.OK);
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

    /***
     * Adds new drink to drink menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param menuId id of the menu
     * @param drinkDTO dto of the new drink
     * @param drinkPriceDTO dto of the drink price
     * @return true if successful, false otherwise
     */
    @PostMapping("/{menuId}/add-drink")
    public ResponseEntity<Boolean> addDrink(@PathVariable Integer menuId, @RequestBody DrinkDTO drinkDTO, @RequestBody DrinkPriceDTO drinkPriceDTO) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(menuId);
        if (drinkMenu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        drinkMenu.getDrinks().add(DrinkPrice.builder()
                .isDeleted(false)
                .price(drinkPriceDTO.getPrice())
                .priceDate(drinkPriceDTO.getPriceDate())
                .drink(drinkDTOtoDrink.convert(drinkDTO))
                .build());

        drinkMenuService.save(drinkMenu);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /***
     * Creates new drink menu based on current one
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param currentMenuDTO dto of the current drink menu
     * @return new drink menu or null if unsuccessful
     */
    @PostMapping("/copy-create")
    public ResponseEntity<DrinkMenuDTO> addDrink(@RequestBody DrinkMenuDTO currentMenuDTO) {
        DrinkMenu currentDrinkMenu = drinkMenuService.findOne(currentMenuDTO.getId());

        DrinkMenu newDrinkMenu = DrinkMenu.builder()
                .isDeleted(false)
                .date(new Date())
                .drinks(new HashSet<>())
                .build();

        for (DrinkPriceDTO priceDTO : currentMenuDTO.getDrinks()){
            DrinkPrice price = drinkPriceDTOToDrinkPrice.convert(priceDTO);
            newDrinkMenu.getDrinks().add(price);
        }

        if (drinkMenuService.save(newDrinkMenu) == null)
            return new ResponseEntity<>(null, HttpStatus.OK);

        DrinkMenuDTO newDrinkMenuDTO = drinkMenuToDrinkMenuDTO.convert(newDrinkMenu);

        return new ResponseEntity<>(newDrinkMenuDTO, HttpStatus.OK);
    }

    /***
     * Deletes one drink price from menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @return true if successful, false otherwise
     */
    @DeleteMapping("/{menuId}/delete-drink/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer menuId, @PathVariable Integer id) {
        DrinkMenu menu = drinkMenuService.findOne(menuId);

        Boolean success;
        if (menuId == null){
            success = false;
        } else {
            menu.setDrinks(menu.getDrinks()
                    .stream()
                    .filter(drinkPrice -> !drinkPrice.getId().equals(id))
                    .collect(Collectors.toSet()));
            success = true;
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}
