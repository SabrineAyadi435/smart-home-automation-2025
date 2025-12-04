package com.smarthome.dto;

/**
 * Data Transfer Object for energy statistics
 */
public class EnergyStats {
    private double currentConsumption;
    private double dailyConsumption;
    private double weeklyConsumption;
    private double monthlyConsumption;
    private double estimatedMonthlyCost;
    private double potentialSavings;
    private String efficiencyRating;

    public EnergyStats() {}

    // Constructor
    public EnergyStats(double currentConsumption, double dailyConsumption, double weeklyConsumption) {
        this.currentConsumption = currentConsumption;
        this.dailyConsumption = dailyConsumption;
        this.weeklyConsumption = weeklyConsumption;
        calculateDerivedValues();
    }

    // Getters and Setters
    public double getCurrentConsumption() {
        return currentConsumption;
    }

    public void setCurrentConsumption(double currentConsumption) {
        this.currentConsumption = currentConsumption;
    }

    public double getDailyConsumption() {
        return dailyConsumption;
    }

    public void setDailyConsumption(double dailyConsumption) {
        this.dailyConsumption = dailyConsumption;
    }

    public double getWeeklyConsumption() {
        return weeklyConsumption;
    }

    public void setWeeklyConsumption(double weeklyConsumption) {
        this.weeklyConsumption = weeklyConsumption;
    }

    public double getMonthlyConsumption() {
        return monthlyConsumption;
    }

    public void setMonthlyConsumption(double monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
    }

    public double getEstimatedMonthlyCost() {
        return estimatedMonthlyCost;
    }

    public void setEstimatedMonthlyCost(double estimatedMonthlyCost) {
        this.estimatedMonthlyCost = estimatedMonthlyCost;
    }

    public double getPotentialSavings() {
        return potentialSavings;
    }

    public void setPotentialSavings(double potentialSavings) {
        this.potentialSavings = potentialSavings;
    }

    public String getEfficiencyRating() {
        return efficiencyRating;
    }

    public void setEfficiencyRating(String efficiencyRating) {
        this.efficiencyRating = efficiencyRating;
    }

    // Utility methods
    private void calculateDerivedValues() {
        this.monthlyConsumption = dailyConsumption * 30;
        this.estimatedMonthlyCost = monthlyConsumption * 0.15; // Assuming $0.15 per kWh
        this.potentialSavings = monthlyConsumption * 0.20; // Assume 20% savings potential
        
        // Calculate efficiency rating
        if (dailyConsumption < 5) {
            efficiencyRating = "EXCELLENT";
        } else if (dailyConsumption < 10) {
            efficiencyRating = "GOOD";
        } else if (dailyConsumption < 15) {
            efficiencyRating = "FAIR";
        } else {
            efficiencyRating = "POOR";
        }
    }

    public String getFormattedStats() {
        return String.format(
            "Energy Statistics:\n" +
            "Current: %.2f kWh\n" +
            "Daily: %.2f kWh\n" +
            "Weekly: %.2f kWh\n" +
            "Monthly: %.2f kWh\n" +
            "Estimated Monthly Cost: $%.2f\n" +
            "Potential Savings: $%.2f\n" +
            "Efficiency Rating: %s",
            currentConsumption, dailyConsumption, weeklyConsumption,
            monthlyConsumption, estimatedMonthlyCost, potentialSavings, efficiencyRating
        );
    }

    @Override
    public String toString() {
        return String.format("EnergyStats[daily=%.2f kWh, rating=%s]", dailyConsumption, efficiencyRating);
    }
}