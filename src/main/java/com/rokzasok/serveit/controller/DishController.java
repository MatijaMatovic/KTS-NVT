package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.DishDTOtoDish;
import com.rokzasok.serveit.converters.DishToDishDTO;
import com.rokzasok.serveit.dto.DishDTO;
import com.rokzasok.serveit.model.Dish;
import com.rokzasok.serveit.service.IDishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Dish controller.
 */
@RestController
@RequestMapping("api/dishes")
public class DishController {
    /**
     * The Dish service.
     */
    final IDishService dishService;
    /**
     * The Dish to dish dto.
     */
    final DishToDishDTO dishToDishDTO;
    /**
     * The Dish dto to dish.
     */
    final DishDTOtoDish dishDTOtoDish;

    /**
     * Instantiates a new Dish controller.
     *
     * @param dishService   the dish service
     * @param dishToDishDTO the dish to dish dto
     * @param dishDTOtoDish the dish dt oto dish
     */
    public DishController(IDishService dishService, DishToDishDTO dishToDishDTO, DishDTOtoDish dishDTOtoDish) {
        this.dishService = dishService;
        this.dishToDishDTO = dishToDishDTO;
        this.dishDTOtoDish = dishDTOtoDish;
    }

    /**
     * Gets dishes.
     *
     * @return the dishes
     */
    @GetMapping
    public ResponseEntity<List<DishDTO>> getDishes() {
        List<Dish> dishes = dishService.findAll();

        List<DishDTO> dtoDishes = dishToDishDTO.convert(dishes);

        return new ResponseEntity<>(dtoDishes, HttpStatus.OK);

    }

    /**
     * Gets dish.
     *
     * @param id the id
     * @return the dish
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<DishDTO> getDish(@PathVariable Integer id) {
        Dish dish = dishService.findOne(id);

        if (dish == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(dishToDishDTO.convert(dish), HttpStatus.OK);
    }

    /**
     * Add new dish response entity.
     *
     * @param dishDTO the dish dto
     * @return the response entity
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<DishDTO> addNewDish(@RequestBody DishDTO dishDTO) {
        Dish dish = dishDTOtoDish.convert(dishDTO);
        boolean exists = false;

        if (dishDTO.getId() != null) {
            exists = dishService.findOne(dishDTO.getId()) != null;
        }

        if (exists) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        dish = dishService.save(dish);

        return new ResponseEntity<>(dishToDishDTO.convert(dish), HttpStatus.OK);
    }

    /**
     * Update dish response entity.
     *
     * @param dishDTO the dish dto
     * @return the response entity
     */
    @PutMapping(consumes = "application/json")
    public ResponseEntity<DishDTO> updateDish(@RequestBody DishDTO dishDTO) {
        Dish dish = dishService.findOne(dishDTO.getId());

        if (dish == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Dish newDish = dishDTOtoDish.convert(dishDTO);

        dish = dishService.save(newDish);
        return new ResponseEntity<>(dishToDishDTO.convert(dish), HttpStatus.OK);
    }

    /**
     * Delete dish response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteDish(@PathVariable Integer id) {
        boolean success = false;
        try {
            success = dishService.deleteOne(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(success, success ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
