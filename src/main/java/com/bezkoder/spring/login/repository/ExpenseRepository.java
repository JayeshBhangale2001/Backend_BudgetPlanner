package com.bezkoder.spring.login.repository;


import com.bezkoder.spring.login.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByAccount_AccountName(String accountName); // Find by account name
    List<Expense> findByUserId(Long userId);
    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.expenseDate BETWEEN :startDate AND :endDate")
    List<Expense> findByUserIdAndExpenseDateBetween(
        @Param("userId") Long userId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate
    );
    List<Expense> findByUserIdAndExpenseDate(Long userId, Date date);
}
