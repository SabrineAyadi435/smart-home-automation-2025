package com.smarthome.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object for climate reports and analytics
 */
public class ClimateReport {
    private LocalDateTime reportDate;
    private String reportPeriod; // "DAILY", "WEEKLY", "MONTHLY"
    private double totalEnergyConsumption;
    private double estimatedCost;
    private double energySavings;
    private Map<String, Double> roomEnergyUsage;
    private Map<String, Double> roomTemperatures;
    private Map<String, Double> airQualityReadings;
    private String recommendations;

    public ClimateReport() {
        this.reportDate = LocalDateTime.now();
        this.roomEnergyUsage = new HashMap<>();
        this.roomTemperatures = new HashMap<>();
        this.airQualityReadings = new HashMap<>();
    }

    // Constructor
    public ClimateReport(String reportPeriod, double totalEnergyConsumption, double estimatedCost) {
        this();
        this.reportPeriod = reportPeriod;
        this.totalEnergyConsumption = totalEnergyConsumption;
        this.estimatedCost = estimatedCost;
    }

    // Getters and Setters
    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public double getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }

    public void setTotalEnergyConsumption(double totalEnergyConsumption) {
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public double getEnergySavings() {
        return energySavings;
    }

    public void setEnergySavings(double energySavings) {
        this.energySavings = energySavings;
    }

    public Map<String, Double> getRoomEnergyUsage() {
        return new HashMap<>(roomEnergyUsage);
    }

    public void setRoomEnergyUsage(Map<String, Double> roomEnergyUsage) {
        this.roomEnergyUsage = new HashMap<>(roomEnergyUsage);
    }

    public void addRoomEnergyUsage(String roomName, double energy) {
        this.roomEnergyUsage.put(roomName, energy);
    }

    public Map<String, Double> getRoomTemperatures() {
        return new HashMap<>(roomTemperatures);
    }

    public void setRoomTemperatures(Map<String, Double> roomTemperatures) {
        this.roomTemperatures = new HashMap<>(roomTemperatures);
    }

    public void addRoomTemperature(String roomName, double temperature) {
        this.roomTemperatures.put(roomName, temperature);
    }

    public Map<String, Double> getAirQualityReadings() {
        return new HashMap<>(airQualityReadings);
    }

    public void setAirQualityReadings(Map<String, Double> airQualityReadings) {
        this.airQualityReadings = new HashMap<>(airQualityReadings);
    }

    public void addAirQualityReading(String roomName, double aqi) {
        this.airQualityReadings.put(roomName, aqi);
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    // Utility methods
    public double calculateAverageTemperature() {
        if (roomTemperatures.isEmpty()) return 0.0;
        return roomTemperatures.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double calculateAverageAirQuality() {
        if (airQualityReadings.isEmpty()) return 0.0;
        return airQualityReadings.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public String getFormattedReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== CLIMATE REPORT ===\n");
        report.append("Date: ").append(reportDate).append("\n");
        report.append("Period: ").append(reportPeriod).append("\n");
        report.append("Total Energy: ").append(String.format("%.2f", totalEnergyConsumption)).append(" kWh\n");
        report.append("Estimated Cost: $").append(String.format("%.2f", estimatedCost)).append("\n");
        
        if (energySavings > 0) {
            report.append("Energy Savings: ").append(String.format("%.2f", energySavings)).append(" kWh\n");
        }

        report.append("\n--- Room Temperatures ---\n");
        roomTemperatures.forEach((room, temp) -> 
            report.append(room).append(": ").append(String.format("%.1f", temp)).append("°C\n"));

        report.append("\n--- Air Quality ---\n");
        airQualityReadings.forEach((room, aqi) -> {
            String quality = aqi >= 80 ? "EXCELLENT" : aqi >= 60 ? "GOOD" : aqi >= 40 ? "FAIR" : "POOR";
            report.append(room).append(": ").append(String.format("%.0f", aqi)).append("/100 (").append(quality).append(")\n");
        });

        if (recommendations != null && !recommendations.isEmpty()) {
            report.append("\n--- Recommendations ---\n");
            report.append(recommendations).append("\n");
        }

        return report.toString();
    }

    @Override
    public String toString() {
        return String.format("ClimateReport[period=%s, energy=%.2f kWh, cost=$%.2f]", 
            reportPeriod, totalEnergyConsumption, estimatedCost);
    }
}