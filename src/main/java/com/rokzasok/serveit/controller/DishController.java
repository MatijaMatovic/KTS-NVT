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

@RestController
@RequestMapping("api/dishes")
public class DishController {
    final IDishService dishService;
    final DishToDishDTO dishToDishDTO;
    final DishDTOtoDish dishDTOtoDish;

    public DishController(IDishService dishService, DishToDishDTO dishToDishDTO, DishDTOtoDish dishDTOtoDish) {
        this.dishService = dishService;
        this.dishToDishDTO = dishToDishDTO;
        this.dishDTOtoDish = dishDTOtoDish;
    }

    @GetMapping
    public ResponseEntity<List<DishDTO>> getDishes() {
        List<Dish> dishes = dishService.findAll();

        List<DishDTO> dtoDishes = dishToDishDTO.convert(dishes);

        return new ResponseEntity<>(dtoDishes, HttpStatus.OK);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DishDTO> getDish(@PathVariable Integer id) {
        Dish dish = dishService.findOne(id);

        if (dish == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(dishToDishDTO.convert(dish), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DishDTO> saveDish(@RequestBody DishDTO dishDTO) {
        Dish dish = dishDTOtoDish.convert(dishDTO);
        dish = dishService.save(dish);

        return new ResponseEntity<>(dishToDishDTO.convert(dish), HttpStatus.OK);
    }

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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteDish(@PathVariable Integer id) {
        return new ResponseEntity<>(dishService.deleteOne(id), HttpStatus.OK);
    }

}
