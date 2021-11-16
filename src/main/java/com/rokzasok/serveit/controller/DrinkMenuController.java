package com.rokzasok.serveit.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.rokzasok.serveit.converters.DrinkDTOtoDrink;
import com.rokzasok.serveit.converters.DrinkMenuDTOToDrinkMenu;
import com.rokzasok.serveit.converters.DrinkMenuToDrinkMenuDTO;
import com.rokzasok.serveit.converters.DrinkPriceDTOToDrinkPrice;
import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.dto.NewDrinkInMenuDTO;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.IDrinkMenuService;
import com.rokzasok.serveit.service.IDrinkPriceService;
import com.rokzasok.serveit.service.IDrinkService;
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

    private final IDrinkPriceService drinkPriceService;
    private final IDrinkService drinkService;

    public DrinkMenuController(IDrinkMenuService drinkMenuService,
                               DrinkMenuDTOToDrinkMenu drinkMenuDTOToDrinkMenu,
                               DrinkMenuToDrinkMenuDTO drinkMenuToDrinkMenuDTO,
                               DrinkDTOtoDrink drinkDTOtoDrink,
                               DrinkPriceDTOToDrinkPrice drinkPriceDTOToDrinkPrice,
                               IDrinkPriceService drinkPriceService,
                               IDrinkService drinkService) {
        this.drinkMenuService = drinkMenuService;
        this.drinkMenuDTOToDrinkMenu = drinkMenuDTOToDrinkMenu;
        this.drinkMenuToDrinkMenuDTO = drinkMenuToDrinkMenuDTO;
        this.drinkDTOtoDrink = drinkDTOtoDrink;
        this.drinkPriceDTOToDrinkPrice = drinkPriceDTOToDrinkPrice;
        this.drinkPriceService = drinkPriceService;
        this.drinkService = drinkService;
    }

    // TODO: RADI
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

    // TODO: RADI
    /***
     * Gets one drink menu by id
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ONE)
     *
     * @param id id of drink menu
     * @return drinkMenuDTO if found //todo , null otherwise
     */
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> one(@PathVariable Integer id) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(id);

        if (drinkMenu == null){
            System.out.println("Ne postoji karta pica sa tim id-jem: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DrinkMenuDTO drinkMenuDTO = drinkMenuToDrinkMenuDTO.convert(drinkMenu);
        return new ResponseEntity<>(drinkMenuDTO, HttpStatus.OK);
    }

    // TODO: RADI
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

    // TODO: RADI
    /***
     * Gets last drink menu
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (LAST)
     *
     * @return drinkMenuDTO if found, null otherwise
     */
    @GetMapping(value = "/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> last() {
        DrinkMenu drinkMenu = drinkMenuService.last();

        if (drinkMenu == null){
            System.out.println("Nema karti pica");
            return new ResponseEntity<>(null, HttpStatus.OK); // todo NOT_FOUND?
        }
        DrinkMenuDTO drinkMenuDTO = drinkMenuToDrinkMenuDTO.convert(drinkMenu);
        return new ResponseEntity<>(drinkMenuDTO, HttpStatus.OK);
    }

    // TODO: RADI
    /***
     * Edits one drink menu
     * author: isidora-stanic
     * authorized: MANAGER
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE) // TODO: RADI ALI VISAK METODA, NIGDE SE NECE KORISTITI
    public ResponseEntity<DrinkMenuDTO> edit(@PathVariable Integer id, @RequestBody DrinkMenuDTO drinkMenuDTO) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(drinkMenuDTO.getId());

        if (drinkMenu == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DrinkMenu newDrinkMenu = drinkMenuDTOToDrinkMenu.convert(drinkMenuDTO);

        drinkMenu = drinkMenuService.save(newDrinkMenu);
        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(drinkMenu), HttpStatus.OK);
    }

    // TODO: RADI sa spageti kodom
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

    // TODO: RADI
    /***
     * Adds NEW drink to drink menu
     * Drink MUST BE NEW
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param menuId id of the menu
     * @param newDrinkInMenuDTO dto of the new drink with price
     * @return true if successful, false otherwise
     */
    @PostMapping("/{menuId}/add-new-drink")
    public ResponseEntity<Boolean> addDrink(@PathVariable Integer menuId, @RequestBody NewDrinkInMenuDTO newDrinkInMenuDTO) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(menuId);
        if (drinkMenu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Drink newDrink = drinkDTOtoDrink.convert(newDrinkInMenuDTO.getDrink());
        Drink newDrinkS = drinkService.save(newDrink); // todo

        System.out.println(newDrinkS);

        DrinkPrice newDrinkPrice = DrinkPrice.builder()
                .isDeleted(false)
                .price(newDrinkInMenuDTO.getPrice())
                .priceDate(newDrinkInMenuDTO.getPriceDate())
                .drink(newDrinkS)
                .build();
        DrinkPrice newDrinkPriceS = drinkPriceService.save(newDrinkPrice); // todo

        System.out.println(newDrinkPriceS);

        drinkMenu.getDrinks().add(newDrinkPriceS);
        drinkMenuService.save(drinkMenu);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // TODO: RADI
    /***
     * Creates new drink menu based on current one
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param oldMenuId dto of the current drink menu
     * @return new drink menu or null if unsuccessful
     */
    @PostMapping("/{oldMenuId}/copy-create")
    public ResponseEntity<DrinkMenuDTO> copyCreate(@PathVariable Integer oldMenuId) {
        DrinkMenu currentDrinkMenu = drinkMenuService.findOne(oldMenuId);

        DrinkMenu newDrinkMenu = DrinkMenu.builder()
                .isDeleted(false)
                .date(new Date())
                .drinks(new HashSet<>())
                .build();

        for (DrinkPrice price : currentDrinkMenu.getDrinks()){
            newDrinkMenu.getDrinks().add(price);
        }

        if (drinkMenuService.save(newDrinkMenu) == null)
            return new ResponseEntity<>(null, HttpStatus.OK);

        DrinkMenuDTO newDrinkMenuDTO = drinkMenuToDrinkMenuDTO.convert(newDrinkMenu);

        return new ResponseEntity<>(newDrinkMenuDTO, HttpStatus.OK);
    }

    // TODO: RADI kao - is deleted se ne azurira ali se na frontu ne vidi
    /***
     * Deletes one drink price from menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @return true if successful, false otherwise
     */
    @DeleteMapping("/{menuId}/delete-drink/{id}")
    public ResponseEntity<Boolean> deleteDrink(@PathVariable Integer menuId, @PathVariable Integer id) {
        DrinkMenu menu = drinkMenuService.findOne(menuId);

        Boolean success;
        if (menu == null){
            success = false;
        } else {
            DrinkPrice oldPrice = drinkPriceService.findOne(id);
            drinkPriceService.deleteOne(oldPrice.getId()); // TODO

            menu.getDrinks().remove(oldPrice);
            drinkMenuService.save(menu);
            success = true;
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // TODO RADI
    /***
     * Edits price of a drink from menu
     * 1 Makes new price
     * // todo - da li treba? - 2 Deletes old price from menu and logically
     * 3 Adds new price to the menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param menuId id of a menu
     * @param newPriceDTO dto for a new price
     * @return dto for changed menu
     */
    @PutMapping("/{menuId}/edit-drink-price")
    public ResponseEntity<DrinkMenuDTO> editDrinkPrice(@PathVariable Integer menuId, @RequestBody DrinkPriceDTO newPriceDTO) {
        DrinkMenu menu = drinkMenuService.findOne(menuId);
        if (menu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DrinkPrice newPrice = drinkPriceDTOToDrinkPrice.convert(newPriceDTO);
        DrinkPrice newPriceS = drinkPriceService.save(newPrice);

        DrinkPrice oldPrice = menu.getDrinks()
                .stream()
                .filter(dPrice -> dPrice.getDrink().getId().equals(newPriceDTO.getDrinkId()))
                .collect(Collectors.toList()).get(0);
        drinkPriceService.deleteOne(oldPrice.getId()); // TODO

        menu.getDrinks().remove(oldPrice);
        menu.getDrinks().add(newPriceS);

        drinkMenuService.save(menu);
        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(menu), HttpStatus.OK);
    }
}
