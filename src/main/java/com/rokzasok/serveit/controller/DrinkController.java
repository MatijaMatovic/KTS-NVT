package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IDrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/drinks")
public class DrinkController {
    @Autowired
    IDrinkService drinkService;
}
