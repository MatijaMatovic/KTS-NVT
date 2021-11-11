package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IDrinkOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/drink-order-items")
public class DrinkOrderItemController {
    @Autowired
    IDrinkOrderItemService drinkOrderItemService;
}
