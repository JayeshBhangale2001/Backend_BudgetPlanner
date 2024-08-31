package com.bezkoder.spring.login.dto;

import java.util.List;
import java.util.Map;

public class ExpenseStatisticsDTO {
    private Map<String, Double> trendData;
    private Map<String, Double> categoryData;
    private double totalAmount;
    private List<ExpenseDTO> topExpenses;

    public ExpenseStatisticsDTO(Map<String, Double> trendData, Map<String, Double> categoryData, double totalAmount, List<ExpenseDTO> topExpenses) {
        this.trendData = trendData;
        this.categoryData = categoryData;
        this.totalAmount = totalAmount;
        this.topExpenses = topExpenses;
    }

    public Map<String, Double> getTrendData() {
        return trendData;
    }

    public void setTrendData(Map<String, Double> trendData) {
        this.trendData = trendData;
    }

    public Map<String, Double> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(Map<String, Double> categoryData) {
        this.categoryData = categoryData;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ExpenseDTO> getTopExpenses() {
        return topExpenses;
    }

    public void setTopExpenses(List<ExpenseDTO> topExpenses) {
        this.topExpenses = topExpenses;
    }
}
