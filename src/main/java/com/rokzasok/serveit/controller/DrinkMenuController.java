package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IDrinkMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/drink-menu")
public class DrinkMenuController {
    @Autowired
    IDrinkMenuService drinkMenuService;
}