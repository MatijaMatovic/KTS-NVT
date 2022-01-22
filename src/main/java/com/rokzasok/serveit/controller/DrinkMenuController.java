package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DrinkDTOtoDrink;
import com.rokzasok.serveit.converters.DrinkMenuDTOToDrinkMenu;
import com.rokzasok.serveit.converters.DrinkMenuToDrinkMenuDTO;
import com.rokzasok.serveit.converters.DrinkPriceDTOToDrinkPrice;
import com.rokzasok.serveit.dto.DrinkMenuDTO;
import com.rokzasok.serveit.dto.DrinkPriceDTO;
import com.rokzasok.serveit.model.Drink;
import com.rokzasok.serveit.model.DrinkMenu;
import com.rokzasok.serveit.model.DrinkPrice;
import com.rokzasok.serveit.service.IDrinkMenuService;
import com.rokzasok.serveit.service.IDrinkPriceService;
import com.rokzasok.serveit.service.IDrinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
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

    /***
     * Creates new drink menu
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @param drinkMenuDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> create(@RequestBody DrinkMenuDTO drinkMenuDTO) {
        DrinkMenu drinkMenu = drinkMenuDTOToDrinkMenu.convert(drinkMenuDTO);

        boolean exists = false;

        if (drinkMenuDTO.getId() != null) {
            exists = drinkMenuService.findOne(drinkMenuDTO.getId()) != null;
        }

        if (exists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        drinkMenu = drinkMenuService.save(drinkMenu);

        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(drinkMenu), HttpStatus.OK);
    }

    /***
     * Gets one drink menu by id
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ONE)
     *
     * @param id id of drink menu
     * @return drinkMenuDTO if found //todo , null otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> one(@PathVariable Integer id) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(id);

        if (drinkMenu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(drinkMenu), HttpStatus.OK);
    }

    /***
     * Gets all drink menus
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ALL)
     *
     * @return list of drinkMenuDTOs
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DrinkMenuDTO>> all() {
        List<DrinkMenu> drinkMenus = drinkMenuService.findAll();

        List<DrinkMenuDTO> drinkMenuDTOs = drinkMenuToDrinkMenuDTO.convert(drinkMenus);
        return new ResponseEntity<>(drinkMenuDTOs, HttpStatus.OK);
    }

    /***
     * Gets last drink menu
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (LAST)
     *
     * @return drinkMenuDTO if found, null otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER') || hasRole('ROLE_WAITER')")
    @GetMapping(value = "/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> last() {
        DrinkMenu drinkMenu = drinkMenuService.last();

        if (drinkMenu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DrinkMenuDTO drinkMenuDTO = drinkMenuToDrinkMenuDTO.convert(drinkMenu);
        return new ResponseEntity<>(drinkMenuDTO, HttpStatus.OK);
    }

    /***
     * Edits one drink menu
     * author: isidora-stanic
     * authorized: MANAGER
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DrinkMenuDTO> edit(@PathVariable Integer id, @RequestBody DrinkMenuDTO drinkMenuDTO) throws Exception {
//        DrinkMenu drinkMenu = drinkMenuService.findOne(drinkMenuDTO.getId());

//        if (drinkMenu == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
        DrinkMenu drinkMenu;
        drinkMenu = drinkMenuService.edit(id, drinkMenuDTO);

//        drinkMenu = drinkMenuService.save(newDrinkMenu);
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
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) throws Exception {
        Boolean success;
        try {
            success = drinkMenuService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    /***
     * Adds NEW drink to drink menu
     * Drink MUST BE NEW
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param menuId id of the menu
     * @param drinkPriceDTO dto of the new drink with price
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/{menuId}/add-drink")
    public ResponseEntity<DrinkMenuDTO> addDrink(@PathVariable Integer menuId, @RequestBody DrinkPriceDTO drinkPriceDTO) {
        DrinkMenu drinkMenu = drinkMenuService.findOne(menuId);
        if (drinkMenu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Drink drink = drinkService.findOne(drinkPriceDTO.getDrinkId());
        if (drink == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (DrinkPrice dp : drinkMenu.getDrinks()){
            if (dp.getDrink().getId().equals(drink.getId())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        DrinkPrice newPrice = DrinkPrice.builder()
                .isDeleted(false)
                .price(drinkPriceDTO.getPrice())
                .priceDate(drinkPriceDTO.getPriceDate())
                .drink(drink)
                .build();
        DrinkPrice newPriceS = drinkPriceService.save(newPrice);

        drinkMenu.getDrinks().add(newPriceS);
        drinkMenuService.save(drinkMenu);

        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(drinkMenu), HttpStatus.OK);
    }

    /***
     * Creates new drink menu based on current one
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param oldMenuId dto of the current drink menu
     * @return new drink menu or null if unsuccessful
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/copy-create/{oldMenuId}")
    public ResponseEntity<DrinkMenuDTO> copyCreate(@PathVariable Integer oldMenuId) {
        DrinkMenu currentDrinkMenu = drinkMenuService.findOne(oldMenuId);

        if (currentDrinkMenu == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DrinkMenu newDrinkMenu = DrinkMenu.builder()
                .isDeleted(false)
                .date(LocalDate.now())
                .drinks(new HashSet<>())
                .build();

        for (DrinkPrice price : currentDrinkMenu.getDrinks()){
            newDrinkMenu.getDrinks().add(price);
        }

        DrinkMenu saved = drinkMenuService.save(newDrinkMenu);

        DrinkMenuDTO newDrinkMenuDTO = drinkMenuToDrinkMenuDTO.convert(saved);

        return new ResponseEntity<>(newDrinkMenuDTO, HttpStatus.OK);
    }

    /***
     * Deletes one drink price from menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @return true if successful, false otherwise
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{menuId}/delete-drink/{id}")
    public ResponseEntity<DrinkMenuDTO> deleteDrink(@PathVariable Integer menuId, @PathVariable Integer id) throws Exception {
        DrinkMenu menu = drinkMenuService.findOne(menuId);

        if (menu == null) {
            System.out.println("error: menu = null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        DrinkPrice oldPrice = drinkPriceService.findOne(id);
        if (oldPrice == null) {
            System.out.println("error: oldPrice = null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println(menu);
        System.out.println(menu.getDrinks());
        boolean succ = menu.getDrinks().remove(oldPrice);

        if (!succ) {
            System.out.println("error: cannot remove old price");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        drinkMenuService.save(menu);

        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(menu), HttpStatus.OK);
    }

    /***
     * Edits price of a drink from menu
     * 1 Makes new price
     * 2 Deletes old price from menu and logically
     * 3 Adds new price to the menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param menuId id of a menu
     * @param newPriceDTO dto for a new price
     * @return dto for changed menu
     */
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{menuId}/edit-drink-price")
    public ResponseEntity<DrinkMenuDTO> editDrinkPrice(@PathVariable Integer menuId, @RequestBody DrinkPriceDTO newPriceDTO) throws Exception {
        DrinkMenu menu = drinkMenuService.findOne(menuId);
        if (menu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DrinkPrice newPrice = drinkPriceDTOToDrinkPrice.convert(newPriceDTO);
        DrinkPrice newPriceS = drinkPriceService.save(newPrice);
        try {
            DrinkPrice oldPrice = menu.getDrinks()
                    .stream()
                    .filter(dPrice -> dPrice.getDrink().getId().equals(newPriceDTO.getDrinkId()))
                    .collect(Collectors.toList()).get(0);
            menu.getDrinks().remove(oldPrice);
            menu.getDrinks().add(newPriceS);
        } catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        drinkMenuService.save(menu);

        return new ResponseEntity<>(drinkMenuToDrinkMenuDTO.convert(menu), HttpStatus.OK);
    }
}
