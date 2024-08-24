package com.bezkoder.spring.login.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bezkoder.spring.login.dto.ExpenseDTO;
import com.bezkoder.spring.login.models.Expense;
import com.bezkoder.spring.login.security.services.UserDetailsImpl;
import com.bezkoder.spring.login.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public List<Expense> getAllExpenses(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        logger.info("Fetching expenses for userId: {}", userId);

        List<Expense> expenses = expenseService.getAllExpenses(userId);

        logger.info("Expenses fetched: {}", expenses);

        return expenses;
    }

    @GetMapping("/account/{accountName}")
    public List<Expense> getExpensesByAccountName(@PathVariable String accountName) {
        return expenseService.getExpensesByAccountName(accountName);
    }

    @PostMapping // Changed to simplify path
    public Expense createExpense(@RequestBody ExpenseDTO expenseDTO, Authentication authentication) {
        System.out.println("Received expenseDTO: " + expenseDTO);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return expenseService.saveExpense(expenseDTO, userId);
    }


    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody Expense expense, Authentication authentication) {
    	  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return expenseService.updateExpense(id, expense, userDetails.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}
