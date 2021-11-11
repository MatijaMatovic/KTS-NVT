package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IFoodMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/food-menu")
public class FoodMenuController {
    @Autowired
    IFoodMenuService foodMenuService;
}
