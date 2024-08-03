package com.bezkoder.spring.login.services;

import com.bezkoder.spring.login.models.Income;
import com.bezkoder.spring.login.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public Income saveIncome(Income income) {
        return incomeRepository.save(income);
    }
}
