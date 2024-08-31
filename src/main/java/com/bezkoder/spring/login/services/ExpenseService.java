package com.bezkoder.spring.login.services;

import com.bezkoder.spring.login.dto.ExpenseDTO;
import com.bezkoder.spring.login.dto.ExpenseStatisticsDTO;
import com.bezkoder.spring.login.models.Account;
import com.bezkoder.spring.login.models.Expense;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.AccountRepository;
import com.bezkoder.spring.login.repository.ExpenseRepository;
import com.bezkoder.spring.login.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    
    
 // Fetch expenses by date range
    public List<Expense> getExpensesByDateRange(Long userId, Date startDate, Date endDate) {
        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
    }

    // Calculate expense statistics based on timeline and date range
    public ExpenseStatisticsDTO getExpenseStatistics(Long userId, Date startDate, Date endDate, String timeline) {
        List<Expense> expenses = getExpensesByDateRange(userId, startDate, endDate);

        // Calculate trend data based on the timeline
        Map<String, Double> trendData = calculateTrendData(expenses, startDate, endDate, timeline);

        // Categorize expenses
        Map<String, Double> categoryData = calculateCategoryData(expenses);

        // Calculate the total amount spent
        Double totalAmount = expenses.stream()
                                     .mapToDouble(Expense::getExpenseAmount)
                                     .sum();

     // Get the top 5 expenses
        List<ExpenseDTO> topExpenses = expenses.stream()
            .filter(expense -> expense.getExpenseAmount() != null) // Filter out null amounts if necessary
            .sorted(Comparator.comparing(Expense::getExpenseAmount, Comparator.nullsLast(Double::compare)).reversed())
            .limit(5)
            .map(expense -> {
                ExpenseDTO dto = new ExpenseDTO();
                dto.setAccountId(expense.getAccount() != null ? expense.getAccount().getId() : null);
                dto.setExpenseType(expense.getExpenseType());
                dto.setExpenseAmount(expense.getExpenseAmount());
                dto.setExpenseDate(new java.sql.Date(expense.getExpenseDate().getTime())); // Ensure proper date conversion
                return dto;
            })
            .collect(Collectors.toList());

        return new ExpenseStatisticsDTO(trendData, categoryData, totalAmount, topExpenses);
    }

    // Logic to calculate trend data based on timeline
    private Map<String, Double> calculateTrendData(List<Expense> expenses, Date startDate, Date endDate, String timeline) {
        Map<String, Double> trendData = new LinkedHashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        switch (timeline) {
            case "7D":
                for (int i = 0; i < 7; i++) {
                    String dateLabel = calendar.getTime().toString(); // Format date as needed
                    double totalAmount = expenses.stream()
                            .filter(expense -> isSameDay(expense.getExpenseDate(), calendar.getTime()))
                            .mapToDouble(Expense::getExpenseAmount)
                            .sum();
                    trendData.put(dateLabel, totalAmount);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case "30D":
                for (int i = 0; i < 30; i++) {
                    String dateLabel = calendar.getTime().toString(); // Format date as needed
                    double totalAmount = expenses.stream()
                            .filter(expense -> isSameDay(expense.getExpenseDate(), calendar.getTime()))
                            .mapToDouble(Expense::getExpenseAmount)
                            .sum();
                    trendData.put(dateLabel, totalAmount);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                break;
            case "12W":
                for (int i = 0; i < 12; i++) {
                    String weekLabel = "Week " + (i + 1); // Customize week label as needed
                    double totalAmount = expenses.stream()
                            .filter(expense -> isSameWeek(expense.getExpenseDate(), calendar.getTime()))
                            .mapToDouble(Expense::getExpenseAmount)
                            .sum();
                    trendData.put(weekLabel, totalAmount);
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }
                break;
            case "6M":
                for (int i = 0; i < 6; i++) {
                    String monthLabel = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                    double totalAmount = expenses.stream()
                            .filter(expense -> isSameMonth(expense.getExpenseDate(), calendar.getTime()))
                            .mapToDouble(Expense::getExpenseAmount)
                            .sum();
                    trendData.put(monthLabel, totalAmount);
                    calendar.add(Calendar.MONTH, 1);
                }
                break;
            case "1Y":
                for (int i = 0; i < 12; i++) {
                    String monthLabel = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                    double totalAmount = expenses.stream()
                            .filter(expense -> isSameMonth(expense.getExpenseDate(), calendar.getTime()))
                            .mapToDouble(Expense::getExpenseAmount)
                            .sum();
                    trendData.put(monthLabel, totalAmount);
                    calendar.add(Calendar.MONTH, 1);
                }
                break;
        }

        return trendData;
    }

    // Helper methods to determine if two dates are in the same day/week/month
    private boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isSameWeek(java.util.Date date1, java.util.Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    private boolean isSameMonth(java.util.Date date1, java.util.Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    // Logic to categorize expenses
    private Map<String, Double> calculateCategoryData(List<Expense> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getExpenseType,
                        Collectors.summingDouble(Expense::getExpenseAmount)
                ));
    }
}
