package com.bezkoder.spring.login.dto;

import java.util.Map;

public class ExpenseStatisticsDTO {
    private Map<String, Double> trendData;
    private Map<String, Double> categoryData;

    public ExpenseStatisticsDTO(Map<String, Double> trendData, Map<String, Double> categoryData) {
        this.trendData = trendData;
        this.categoryData = categoryData;
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
}
