package com.smarthome.service;

import com.smarthome.model.Thermostat;
import java.util.List;

public class EnergyTracker {
    private double totalEnergyConsumed;
    private double energyCostPerKwh = 0.15;
    
    public EnergyTracker() {
        this.totalEnergyConsumed = 0.0;
    }
    
    public double calculateTotalConsumption(List<Thermostat> thermostats) {
        totalEnergyConsumed = thermostats.stream()
            .filter(Thermostat::isOn)
            .mapToDouble(Thermostat::getEnergyConsumption)
            .sum();
        return totalEnergyConsumed;
    }
    
    public double calculateCost() {
        return totalEnergyConsumed * energyCostPerKwh;
    }
    
    public void generateReport(List<Thermostat> thermostats) {
        calculateTotalConsumption(thermostats);
        
        System.out.println("=== ENERGY CONSUMPTION REPORT ===");
        System.out.printf("Total Consumption: %.2f kWh%n", totalEnergyConsumed);
        System.out.printf("Estimated Cost: $%.2f%n", calculateCost());
        
        System.out.println("\n--- Device Breakdown ---");
        thermostats.forEach(thermostat -> {
            if (thermostat.isOn()) {
                double consumption = thermostat.getEnergyConsumption();
                System.out.printf("%s: %.2f kWh%n", thermostat.getName(), consumption);
            }
        });
        
        System.out.println("\n--- Energy Saving Tips ---");
        if (totalEnergyConsumed > 10.0) {
            System.out.println("💡 Consider using eco mode during off-peak hours");
        }
        if (thermostats.stream().anyMatch(t -> !t.isEcoModeActive())) {
            System.out.println("🌿 Enable eco mode for additional savings");
        }
    }
    
    public void setEnergyCost(double costPerKwh) {
        this.energyCostPerKwh = costPerKwh;
    }
    
    public double getEnergySavingsPotential() {
        return totalEnergyConsumed * 0.20;
    }
}