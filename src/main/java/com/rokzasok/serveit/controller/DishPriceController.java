package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IDishPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dish-prices")
public class DishPriceController {
    @Autowired
    IDishPriceService dishPriceService;
}