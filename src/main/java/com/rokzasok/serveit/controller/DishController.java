package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dishes")
public class DishController {
    @Autowired
    IDishService dishService;
}
