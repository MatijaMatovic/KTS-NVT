package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishPriceDTOToDishPrice;
import com.rokzasok.serveit.converters.FoodMenuDTOToFoodMenu;
import com.rokzasok.serveit.converters.FoodMenuToFoodMenuDTO;
import com.rokzasok.serveit.dto.DishPriceDTO;
import com.rokzasok.serveit.dto.FoodMenuDTO;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.model.DishPrice;
import com.rokzasok.serveit.model.FoodMenu;
import com.rokzasok.serveit.service.IDishPriceService;
import com.rokzasok.serveit.service.IDishService;
import com.rokzasok.serveit.service.IFoodMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
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

    private final IDishPriceService dishPriceService;

    public FoodMenuController(IFoodMenuService foodMenuService,
                              FoodMenuDTOToFoodMenu foodMenuDTOToFoodMenu,
                              FoodMenuToFoodMenuDTO foodMenuToFoodMenuDTO,
                              IDishService dishService,
                              DishPriceDTOToDishPrice dishPriceDTOToDishPrice, IDishPriceService dishPriceService) {
        this.foodMenuService = foodMenuService;
        this.foodMenuDTOToFoodMenu = foodMenuDTOToFoodMenu;
        this.foodMenuToFoodMenuDTO = foodMenuToFoodMenuDTO;
        this.dishService = dishService;
        this.dishPriceDTOToDishPrice = dishPriceDTOToDishPrice;
        this.dishPriceService = dishPriceService;
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
    public ResponseEntity<FoodMenuDTO> create(@RequestBody FoodMenuDTO foodMenuDTO) {
        FoodMenu foodMenu = foodMenuDTOToFoodMenu.convert(foodMenuDTO);

        boolean exists = false;

        if (foodMenuDTO.getId() != null) {
            exists = foodMenuService.findOne(foodMenuDTO.getId()) != null;
        }

        if (exists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        foodMenu = foodMenuService.save(foodMenu);

        return new ResponseEntity<>(foodMenuToFoodMenuDTO.convert(foodMenu), HttpStatus.OK);
    }

    /***
     * Gets one food menu by id
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ONE)
     *
     * @param id id of drink menu
     * @return FoodMenuDTO if found //todo , null otherwise
     */
    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodMenuDTO> one(@PathVariable Integer id) {
        FoodMenu foodMenu = foodMenuService.findOne(id);

        if (foodMenu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        FoodMenuDTO foodMenuDTO = ;
//        if (foodMenuDTO == null) {
//            System.out.println("Food menu DTO je null");
//            return new ResponseEntity<>(null, HttpStatus.OK);
//        }
        return new ResponseEntity<>(foodMenuToFoodMenuDTO.convert(foodMenu), HttpStatus.OK);
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

    /***
     * Gets last food menu
     * author: isidora-stanic
     * authorized: MANAGER, WAITER
     * READ (LAST)
     *
     * @return foodMenuDTO if found, null otherwise
     */
    @GetMapping(value = "/last", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodMenuDTO> last() {
        FoodMenu foodMenu = foodMenuService.last();

        if (foodMenu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        FoodMenuDTO foodMenuDTO = foodMenuToFoodMenuDTO.convert(foodMenu);
        return new ResponseEntity<>(foodMenuDTO, HttpStatus.OK);
    }

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
    public ResponseEntity<FoodMenuDTO> edit(@PathVariable Integer id, @RequestBody FoodMenuDTO foodMenuDTO) throws Exception {
//        FoodMenu foodMenu = foodMenuService.findOne(foodMenuDTO.getId());
//
//        if (foodMenu == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        FoodMenu newFoodMenu = foodMenuDTOToFoodMenu.convert(foodMenuDTO);

        FoodMenu menu;
        menu = foodMenuService.edit(id, foodMenuDTO);
//
//        foodMenu = foodMenuService.save(newFoodMenu);
        return new ResponseEntity<>(foodMenuToFoodMenuDTO.convert(menu), HttpStatus.OK);
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
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) throws Exception {
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
    public ResponseEntity<FoodMenuDTO> addDish(@PathVariable Integer menuId, @RequestBody DishPriceDTO dishPriceDTO) {
        FoodMenu foodMenu = foodMenuService.findOne(menuId);
        if (foodMenu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Dish dish = dishService.findOne(dishPriceDTO.getDishId());
        if (dish == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (DishPrice dp : foodMenu.getDishes()){
            if (dp.getDish().getId().equals(dishPriceDTO.getDishId())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        DishPrice newPrice = DishPrice.builder()
                .isDeleted(false)
                .price(dishPriceDTO.getPrice())
                .priceDate(dishPriceDTO.getPriceDate())
                .dish(dish)
                .build();
        DishPrice newPriceS = dishPriceService.save(newPrice);

        foodMenu.getDishes().add(newPriceS);
        foodMenuService.save(foodMenu);

        return new ResponseEntity<>(foodMenuToFoodMenuDTO.convert(foodMenu), HttpStatus.OK);
    }

    /***
     * Creates new food menu based on current one
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @param oldMenuId id of the current food menu
     * @return new food menu or null if unsuccessful
     */
    @GetMapping("/copy-create/{oldMenuId}")
    public ResponseEntity<FoodMenuDTO> copyCreate(@PathVariable Integer oldMenuId) {
        FoodMenu currentFoodMenu = foodMenuService.findOne(oldMenuId);

        if (currentFoodMenu == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FoodMenu newFoodMenu = FoodMenu.builder()
                .isDeleted(false)
                .date(LocalDate.now())
                .dishes(new HashSet<>())
                .build();

        for (DishPrice price : currentFoodMenu.getDishes()){
            newFoodMenu.getDishes().add(price);
        }

        FoodMenu saved = foodMenuService.save(newFoodMenu);

        FoodMenuDTO newFoodMenuDTO = foodMenuToFoodMenuDTO.convert(saved);

        return new ResponseEntity<>(newFoodMenuDTO, HttpStatus.OK);
    }

    /***
     * Deletes one dish price from menu
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @return true if successful, false otherwise
     */
    @DeleteMapping("/{menuId}/delete-dish/{id}")
    public ResponseEntity<FoodMenuDTO> deleteDish(@PathVariable Integer menuId, @PathVariable Integer id) throws Exception {
        FoodMenu menu = foodMenuService.findOne(menuId);

        if (menu == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        DishPrice oldPrice = dishPriceService.findOne(id);
        if (oldPrice == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


        boolean succ = menu.getDishes().remove(oldPrice);

        if (!succ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        foodMenuService.save(menu);

        return new ResponseEntity<>(foodMenuToFoodMenuDTO.convert(menu), HttpStatus.OK);
    }

    /***
     * Edits price of a dish from menu
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
    @PutMapping("/{menuId}/edit-dish-price")
    public ResponseEntity<FoodMenuDTO> editDishPrice(@PathVariable Integer menuId, @RequestBody DishPriceDTO newPriceDTO) throws Exception {
        FoodMenu menu = foodMenuService.findOne(menuId);
        if (menu == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (dishPriceService.findOne(newPriceDTO.getDishId()) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DishPrice newPrice = dishPriceDTOToDishPrice.convert(newPriceDTO);
        DishPrice newPriceS = dishPriceService.save(newPrice);

        try {
            DishPrice oldPrice = menu.getDishes()
                    .stream()
                    .filter(dPrice -> dPrice.getDish().getId().equals(newPriceDTO.getDishId()))
                    .collect(Collectors.toList()).get(0);
            menu.getDishes().remove(oldPrice);
            menu.getDishes().add(newPriceS);
        } catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        foodMenuService.save(menu);

        return new ResponseEntity<>(foodMenuToFoodMenuDTO.convert(menu), HttpStatus.OK);
    }
}
