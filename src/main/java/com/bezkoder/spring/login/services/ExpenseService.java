package com.bezkoder.spring.login.services;

import com.bezkoder.spring.login.dto.ExpenseDTO;
import com.bezkoder.spring.login.models.Account;
import com.bezkoder.spring.login.models.Expense;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.AccountRepository;
import com.bezkoder.spring.login.repository.ExpenseRepository;
import com.bezkoder.spring.login.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    public List<Expense> getAllExpenses(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getExpensesByAccountName(String accountName) {
        return expenseRepository.findByAccount_AccountName(accountName);
    }

    public Expense saveExpense(ExpenseDTO expenseDTO, Long userId) {
        // Validate and set the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Fetch and set the account
        if (expenseDTO.getAccountId() != null) {
            Account account = accountRepository.findByIdAndUserId(expenseDTO.getAccountId(), userId)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
            
            Expense expense = new Expense();
            expense.setAccount(account);
            expense.setExpenseType(expenseDTO.getExpenseType());
            expense.setExpenseAmount(expenseDTO.getExpenseAmount());
            expense.setExpenseDate(expenseDTO.getExpenseDate());
            expense.setUser(user);

            // Save and return the expense
            return expenseRepository.save(expense);
        } else {
            throw new IllegalArgumentException("Account information is missing");
        }
    }



    public Expense updateExpense(Long id, Expense expense, Long userId) {
        if (expenseRepository.existsById(id)) {
        	Optional<User> user = userRepository.findById(userId);
            user.ifPresent(expense::setUser);
            expense.setId(id);
            if (expense.getAccount() != null && expense.getAccount().getId() != null) {
                Optional<Account> accountOptional = accountRepository.findByIdAndUserId(expense.getAccount().getId(),userId);
                if (accountOptional.isPresent()) {
                    expense.setAccount(accountOptional.get());
                } else {
                    // Handle the case where the account does not exist
                    throw new EntityNotFoundException("Account not found");
                }
            } else {
                // Handle the case where the account is null
                throw new IllegalArgumentException("Account information is missing");
            }
            return expenseRepository.save(expense);
        }
        return null;
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
