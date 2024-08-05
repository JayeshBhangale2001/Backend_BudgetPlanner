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

    public Income updateIncome(Long id, Income updatedIncome) {
        return incomeRepository.findById(id).map(income -> {
            income.setMonth(updatedIncome.getMonth());
            income.setSource(updatedIncome.getSource());
            income.setAmount(updatedIncome.getAmount());
            income.setInvestments(updatedIncome.getInvestments());
            income.setDate(updatedIncome.getDate()); // Update date
            return incomeRepository.save(income);
        }).orElseThrow(() -> new RuntimeException("Income not found with id " + id));
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }
}
