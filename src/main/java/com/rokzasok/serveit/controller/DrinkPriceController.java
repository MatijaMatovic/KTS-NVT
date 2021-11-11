package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IDrinkPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/drink-prices")
public class DrinkPriceController {
    @Autowired
    IDrinkPriceService drinkPriceService;
}
