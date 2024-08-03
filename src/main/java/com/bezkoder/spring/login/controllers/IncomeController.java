package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.Income;
import com.bezkoder.spring.login.services.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping
    public List<Income> getAllIncomes() {
        return incomeService.getAllIncomes();
    }

    @PostMapping
    public Income saveIncome(@RequestBody Income income) {
        return incomeService.saveIncome(income);
    }
}
