package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishDTOtoDish;
import com.rokzasok.serveit.converters.FoodMenuDTOToFoodMenu;
import com.rokzasok.serveit.converters.FoodMenuToFoodMenuDTO;
import com.rokzasok.serveit.converters.DishPriceDTOToDishPrice;
import com.rokzasok.serveit.dto.FoodMenuDTO;
import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.service.IDishService;
import com.rokzasok.serveit.service.IFoodMenuService;
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
@RequestMapping("api/food-menu")
public class FoodMenuController {
    private final IFoodMenuService foodMenuService;

    private final FoodMenuDTOToFoodMenu foodMenuDTOToFoodMenu;
    private final FoodMenuToFoodMenuDTO foodMenuToFoodMenuDTO;

    private final IDishService dishService;

    private final DishPriceDTOToDishPrice dishPriceDTOToDishPrice;

    public FoodMenuController(IFoodMenuService foodMenuService,
                              FoodMenuDTOToFoodMenu foodMenuDTOToFoodMenu,
                              FoodMenuToFoodMenuDTO foodMenuToFoodMenuDTO,
                              IDishService dishService,
                              DishPriceDTOToDishPrice dishPriceDTOToDishPrice) {
        this.foodMenuService = foodMenuService;
        this.foodMenuDTOToFoodMenu = foodMenuDTOToFoodMenu;
        this.foodMenuToFoodMenuDTO = foodMenuToFoodMenuDTO;
        this.dishService = dishService;
        this.dishPriceDTOToDishPrice = dishPriceDTOToDishPrice;
    }

    /***
     * Creates new food menu
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @param foodMenuDTO dto from frontend
     * @return true if successful, false otherwise
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> create(@RequestBody FoodMenuDTO foodMenuDTO) {
        FoodMenu foodMenu = foodMenuDTOToFoodMenu.convert(foodMenuDTO);
        FoodMenu foodMenuSaved = foodMenuService.save(foodMenu);
        if (foodMenuSaved == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /***
     * Gets one food menu by id
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ONE)
     *
     * @param id id of drink menu
     * @return FoodMenuDTO if found, null otherwise
     */
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodMenuDTO> one(@PathVariable Integer id) {
        FoodMenu foodMenu = foodMenuService.findOne(id);

        if (foodMenu == null){
            System.out.println("Drink menu je null");
            return new ResponseEntity<>(null, HttpStatus.OK); // TODO NOT_FOUND?
        }
        FoodMenuDTO foodMenuDTO = foodMenuToFoodMenuDTO.convert(foodMenu);
        if (foodMenuDTO == null) {
            System.out.println("Food menu DTO je null");
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(foodMenuDTO, HttpStatus.OK);
    }

    /***
     * Gets all food menus
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ALL)
     *
     * @return list of FoodMenuDTOs
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FoodMenuDTO>> all() {
        List<FoodMenu> foodMenus = foodMenuService.findAll();

        List<FoodMenuDTO> foodMenuDTOs = foodMenuToFoodMenuDTO.convert(foodMenus);
        return new ResponseEntity<>(foodMenuDTOs, HttpStatus.OK);
    }

    // TODO: GET LATEST FOOD MENU ____ WAITER

    /***
     * Edits one food menu
     * author: isidora-stanic
     * authorized: MANAGER
     * UPDATE (EDIT)
     *
     * @return true if successful, false otherwise
     */
    @PutMapping(value = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodMenuDTO> edit(@PathVariable Integer id, @RequestBody FoodMenuDTO foodMenuDTO) {
        /*
        FoodMenu FoodMenu;
        try {
            FoodMenu = FoodMenuService.edit(id, FoodMenuDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(FoodMenuToFoodMenuDTO.convert(FoodMenu), HttpStatus.OK);*/
        FoodMenu foodMenu = foodMenuService.findOne(foodMenuDTO.getId());

        if (foodMenu == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FoodMenu newFoodMenu = foodMenuDTOToFoodMenu.convert(foodMenuDTO);

        foodMenu = foodMenuService.save(newFoodMenu);
        return new ResponseEntity<>(foodMenuToFoodMenuDTO.convert(foodMenu), HttpStatus.OK);
    }

    /***
     * Deletes one food menu
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
            success = foodMenuService.deleteOne(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    /***
     * Adds new dish to food menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param menuId id of the menu
     * @param dishPriceDTO dto of the dish price
     * @return true if successful, false otherwise
     */
    @PostMapping("/{menuId}/add-dish")
    public ResponseEntity<Boolean> addDish(@PathVariable Integer menuId, @RequestBody DishPriceDTO dishPriceDTO) {
        FoodMenu foodMenu = foodMenuService.findOne(menuId);
        if (foodMenu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        foodMenu.getDishes().add(DishPrice.builder()
                .isDeleted(false)
                .price(dishPriceDTO.getPrice())
                .priceDate(dishPriceDTO.getPriceDate())
                .dish(dishService.findOne(dishPriceDTO.getDishId()))
                .build());

        foodMenuService.save(foodMenu);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /***
     * Creates new food menu based on current one
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param currentMenuDTO dto of the current food menu
     * @return new food menu or null if unsuccessful
     */
    @PostMapping("/copy-create")
    public ResponseEntity<FoodMenuDTO> addDrink(@RequestBody FoodMenuDTO currentMenuDTO) {
        FoodMenu currentFoodMenu = foodMenuService.findOne(currentMenuDTO.getId());

        FoodMenu newFoodMenu = FoodMenu.builder()
                .isDeleted(false)
                .date(new Date())
                .dishes(new HashSet<>())
                .build();

        for (DishPriceDTO priceDTO : currentMenuDTO.getDishes()){
            DishPrice price = dishPriceDTOToDishPrice.convert(priceDTO);
            newFoodMenu.getDishes().add(price);
        }

        if (foodMenuService.save(newFoodMenu) == null)
            return new ResponseEntity<>(null, HttpStatus.OK);

        FoodMenuDTO newFoodMenuDTO = foodMenuToFoodMenuDTO.convert(newFoodMenu);

        return new ResponseEntity<>(newFoodMenuDTO, HttpStatus.OK);
    }

    /***
     * Deletes one dish price from menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @return true if successful, false otherwise
     */
    @DeleteMapping("/{menuId}/delete-drink/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer menuId, @PathVariable Integer id) {
        FoodMenu menu = foodMenuService.findOne(menuId);

        Boolean success;
        if (menuId == null){
            success = false;
        } else {
            menu.setDishes(menu.getDishes()
                    .stream()
                    .filter(dishPrice -> !dishPrice.getId().equals(id))
                    .collect(Collectors.toSet()));
            success = true;
        }
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // TODO change price - napravi novu cenu, izbaci staru cenu iz menija i metne novu umesto nje je u meni
}
