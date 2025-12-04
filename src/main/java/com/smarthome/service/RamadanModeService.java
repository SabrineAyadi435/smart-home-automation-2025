package com.smarthome.service;

import com.smarthome.model.Thermostat;
import com.smarthome.model.AirQualitySensor;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.*;

public class RamadanModeService {
    private ClimateService climateService;
    private boolean ramadanModeActive;
    private LocalDate ramadanStartDate;
    private LocalDate ramadanEndDate;
    private Map<String, LocalTime> ramadanSchedule;
    
    public RamadanModeService(ClimateService climateService) {
        this.climateService = climateService;
        this.ramadanModeActive = false;
        this.ramadanSchedule = new HashMap<>();
        initializeRamadanSchedule();
    }
    
    private void initializeRamadanSchedule() {
        ramadanSchedule.put("SUHOOR_END", LocalTime.of(5, 0));
        ramadanSchedule.put("FASTING_START", LocalTime.of(5, 30));
        ramadanSchedule.put("AFTERNOON_REST", LocalTime.of(14, 0));
        ramadanSchedule.put("IFTAR_PREP", LocalTime.of(17, 30));
        ramadanSchedule.put("IFTAR_TIME", LocalTime.of(18, 30));
        ramadanSchedule.put("TARAWEEH_START", LocalTime.of(20, 30));
        ramadanSchedule.put("TARAWEEH_END", LocalTime.of(22, 0));
    }
    
    public void enableRamadanMode() {
        this.ramadanModeActive = true;
        System.out.println("🌙 RAMADAN MODE ACTIVATED");
        System.out.println("🕌 Special climate settings for blessed month enabled");
        applyRamadanDefaults();
    }
    
    public void disableRamadanMode() {
        this.ramadanModeActive = false;
        System.out.println("Ramadan Mode deactivated");
    }
    
    public boolean isRamadanModeActive() {
        return ramadanModeActive;
    }
    
    private void applyRamadanDefaults() {
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.setEcoMode(true);
                thermostat.setMode("ECO");
                System.out.println("💤 " + thermostat.getName() + " set for Ramadan energy savings");
            }
        });
    }
    
    public void optimizeFastingHours() {
        if (!ramadanModeActive) return;
        LocalTime now = LocalTime.now();
        
        if (now.isAfter(ramadanSchedule.get("FASTING_START")) && 
            now.isBefore(ramadanSchedule.get("AFTERNOON_REST"))) {
            setFastingEnergySavingMode();
        } else if (now.isAfter(ramadanSchedule.get("AFTERNOON_REST")) && 
                   now.isBefore(ramadanSchedule.get("IFTAR_PREP"))) {
            setAfternoonComfortMode();
        } else if (now.isAfter(ramadanSchedule.get("IFTAR_PREP")) && 
                   now.isBefore(ramadanSchedule.get("IFTAR_TIME"))) {
            prepareForIftar();
        }
    }
    
    private void setFastingEnergySavingMode() {
        System.out.println("🌅 Fasting hours - Energy saving mode active");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn()) {
                double currentTemp = thermostat.getTargetTemperature();
                if (thermostat.getMode().equals("COOL") && currentTemp < 25.0) {
                    thermostat.setTemperature(25.0);
                } else if (thermostat.getMode().equals("HEAT") && currentTemp > 19.0) {
                    thermostat.setTemperature(19.0);
                }
                thermostat.setEcoMode(true);
            }
        });
    }
    
    private void setAfternoonComfortMode() {
        System.out.println("😴 Afternoon fasting fatigue - Enhanced comfort mode");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn() && thermostat.getLocation().toLowerCase().contains("living")) {
                thermostat.setTemperature(21.0);
                thermostat.setEcoMode(false);
                System.out.println("💆 " + thermostat.getName() + " enhanced for afternoon comfort");
            }
        });
    }
    
    public void prepareForIftar() {
        System.out.println("🍽️ Preparing home for Iftar...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn() && thermostat.getLocation().toLowerCase().contains("dining")) {
                thermostat.setTemperature(20.0);
                thermostat.setEcoMode(false);
                System.out.println("❄️  " + thermostat.getName() + " cooled for Iftar comfort");
            }
        });
    }
    
    public void prepareForTaraweeh() {
        System.out.println("📖 Preparing for Taraweeh prayers...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.setTemperature(21.0);
                thermostat.setMode("AUTO");
                thermostat.setEcoMode(true);
                System.out.println("🕌 " + thermostat.getName() + " optimized for Taraweeh");
            }
        });
    }
    
    public void prepareForSuhoor() {
        System.out.println("🌅 Preparing for Suhoor...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn() && thermostat.getLocation().toLowerCase().contains("kitchen")) {
                thermostat.setTemperature(22.0);
                System.out.println("☀️  " + thermostat.getName() + " ready for Suhoor preparation");
            }
        });
    }
    
    public void generateRamadanEnergyReport() {
        System.out.println("🌙 RAMADAN ENERGY REPORT");
        System.out.println("=== Special Ramadan Analysis ===");
        double totalEnergy = climateService.getTotalEnergyConsumption();
        double estimatedSavings = calculateRamadanSavings();
        
        System.out.printf("Total Consumption: %.2f kWh%n", totalEnergy);
        System.out.printf("Estimated Ramadan Savings: %.2f kWh%n", estimatedSavings);
        System.out.printf("Potential Charity Donation: $%.2f%n", estimatedSavings * 0.15);
        
        System.out.println("\n💡 Ramadan Energy Tips:");
        System.out.println("• Use eco mode during fasting hours");
        System.out.println("• Consider donating energy savings as Sadaqah");
    }
    
    private double calculateRamadanSavings() {
        double totalEnergy = climateService.getTotalEnergyConsumption();
        return totalEnergy * 0.15;
    }
    
    public String getRamadanStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== RAMADAN MODE STATUS ===\n");
        status.append("Ramadan Mode: ").append(ramadanModeActive ? "ACTIVE 🌙" : "INACTIVE").append("\n");
        
        if (ramadanModeActive) {
            status.append("\n--- Daily Schedule ---\n");
            ramadanSchedule.forEach((event, time) -> 
                status.append(event).append(": ").append(time).append("\n"));
        }
        
        return status.toString();
    }
    
    public void setRamadanSchedule(String event, LocalTime time) {
        ramadanSchedule.put(event.toUpperCase(), time);
        System.out.println("🕰️  Ramadan " + event + " scheduled for " + time);
    }
    
    public Map<String, LocalTime> getRamadanSchedule() {
        return new HashMap<>(ramadanSchedule);
    }
}