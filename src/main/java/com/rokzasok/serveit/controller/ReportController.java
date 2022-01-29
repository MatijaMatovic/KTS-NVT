package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.model.DishOrderItem;
import com.rokzasok.serveit.model.DrinkOrderItem;
import com.rokzasok.serveit.model.Order;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.service.impl.OrderService;
import com.rokzasok.serveit.service.impl.UserSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/reports")
public class ReportController {
    @Autowired
    OrderService orderService;

    @Autowired
    UserSalaryService userSalaryService;

    @GetMapping(value = "/{interval}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Double>> getReport(@PathVariable String interval) {
        List<Order> orders = orderService.findAll();
        List<UserSalary> salaries = userSalaryService.findAll();
        LocalDateTime now = LocalDateTime.now();

        HashMap<String, Double> incomeMap = new HashMap<>();

        for (Order order : orders) {
            LocalDateTime orderDate = order.getCreationDateTime();
            if (interval.equals("weekly")) {
                if (now.getDayOfYear() - orderDate.getDayOfYear() > 7 ||
                    now.getDayOfYear() - orderDate.getDayOfYear() < 0)
                    continue;
            }
            else if (interval.equals("monthly")) {
                if (now.getMonthValue() != orderDate.getMonthValue())
                    continue;
            }
            Double dishesIncome = incomeMap.getOrDefault("dishesIncome", 0.);
            Double drinksIncome = incomeMap.getOrDefault("drinksIncome", 0.);
            Double dishesExpense = incomeMap.getOrDefault("dishesExpense", 0.);
            Double drinksExpense = incomeMap.getOrDefault("drinksExpense", 0.);
            for (DishOrderItem dish : order.getDishes()) {
                dishesIncome += dish.getDish().getPrice() * dish.getAmount();
                dishesExpense -= dish.getDish().getDish().getPreparationPrice() * dish.getAmount();
            }
            for (DrinkOrderItem drink : order.getDrinks()) {
                drinksIncome += drink.getDrink().getPrice() * drink.getAmount();
                drinksExpense -= drink.getDrink().getDrink().getPurchasePrice() * drink.getAmount();
            }

            incomeMap.put("dishesIncome", dishesIncome);
            incomeMap.put("dishesExpense", dishesExpense);
            incomeMap.put("drinksIncome", drinksIncome);
            incomeMap.put("drinksExpense", drinksExpense);
        }

        for (UserSalary salary : salaries) {
            LocalDate salaryDate = salary.getSalaryDate();
            if (interval.equals("monthly")) {
                if (now.getDayOfMonth() < salaryDate.getDayOfMonth())
                    continue;
            }
            if (interval.equals("weekly")) {
                if (now.getDayOfMonth() - salaryDate.getDayOfMonth() > 7 ||
                    now.getDayOfMonth() - salaryDate.getDayOfMonth() < 0)
                    continue;
            }

            Double salaryExpense = incomeMap.getOrDefault("salaryExpense", 0.);
            salaryExpense -= salary.getSalary();
            incomeMap.put("salaryExpense", salaryExpense);
        }

        return new ResponseEntity<>(incomeMap, HttpStatus.OK);
    }

    public String stringifyMonth(Month month) {
        int monthOrdinal = month.getValue();
        String monthString = String.format("%d. %s", monthOrdinal, month.toString());
        return monthOrdinal >= 10 ? monthString : "0" + monthString;
    }

}
