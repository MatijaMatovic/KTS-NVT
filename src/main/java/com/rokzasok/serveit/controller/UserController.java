package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    IUserService userService;
}
