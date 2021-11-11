package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.service.ISittingTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sitting-tables")
public class SittingTableController {
    @Autowired
    ISittingTableService sittingTableService;
}
