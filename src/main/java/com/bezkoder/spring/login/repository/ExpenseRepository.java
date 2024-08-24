package com.bezkoder.spring.login.repository;


import com.bezkoder.spring.login.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByAccount_AccountName(String accountName); // Find by account name
    List<Expense> findByUserId(Long userId);
	
}
