package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.IUserSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user-salaries")
public class UserSalaryController {
    @Autowired
    IUserSalaryService userSalaryService;
}
