package com.smarthome.service;

import com.smarthome.model.Thermostat;
import com.smarthome.model.AirQualitySensor;
import com.smarthome.model.Room;
import com.smarthome.model.Home;
import com.smarthome.automation.ClimateRule;
import java.util.*;

public class ClimateService {
    private List<Thermostat> thermostats;
    private List<AirQualitySensor> airQualitySensors;
    private List<ClimateRule> climateRules;
    private Home home;
    private EnergyTracker energyTracker;
    
    public ClimateService(Home home) {
        this.home = home;
        this.thermostats = new ArrayList<>();
        this.airQualitySensors = new ArrayList<>();
        this.climateRules = new ArrayList<>();
        this.energyTracker = new EnergyTracker();
        discoverClimateDevices();
        initializeDefaultRules();
    }
    
    private void discoverClimateDevices() {
        for (Room room : home.getRooms()) {
            room.getDevices().forEach(device -> {
                if (device instanceof Thermostat) {
                    thermostats.add((Thermostat) device);
                } else if (device instanceof AirQualitySensor) {
                    airQualitySensors.add((AirQualitySensor) device);
                }
            });
        }
        System.out.println("Discovered " + thermostats.size() + " thermostats and " + 
                          airQualitySensors.size() + " air quality sensors");
    }
    
    private void initializeDefaultRules() {
        // Clear existing rules
        climateRules.clear();
        
        // Islamic Rules
        ClimateRule prayerRoomComfort = new ClimateRule(
            "Prayer Room Comfort",
            "Maintain comfortable temperature in prayer room",
            ClimateRule.ClimateCondition.TEMPERATURE_BELOW,
            ClimateRule.ClimateAction.SET_TEMPERATURE,
            21.0,
            "22.0",
            "Prayer Room"
        );
        
        ClimateRule wuduComfort = new ClimateRule(
            "Wudu Comfort",
            "Warm bathroom for comfortable ablution",
            ClimateRule.ClimateCondition.TEMPERATURE_BELOW,
            ClimateRule.ClimateAction.SET_TEMPERATURE,
            23.0,
            "24.0",
            "Bathroom"
        );
        
        // Climate Comfort Rules
        ClimateRule overheating = new ClimateRule(
            "Overheating Protection",
            "Cool down when too hot",
            ClimateRule.ClimateCondition.TEMPERATURE_ABOVE,
            ClimateRule.ClimateAction.SET_MODE,
            28.0,
            "COOL",
            "all"
        );
        
        ClimateRule freezing = new ClimateRule(
            "Freezing Protection",
            "Warm up when too cold",
            ClimateRule.ClimateCondition.TEMPERATURE_BELOW,
            ClimateRule.ClimateAction.SET_MODE,
            16.0,
            "HEAT",
            "all"
        );
        
        ClimateRule nightEnergy = new ClimateRule(
            "Night Energy Saving",
            "Save energy at night",
            ClimateRule.ClimateCondition.TEMPERATURE_ABOVE,
            ClimateRule.ClimateAction.APPLY_SCHEDULE,
            0,
            "NIGHT_TEMP",
            "all"
        );
        
        // Air Quality Rules
        ClimateRule airQualityAlert = new ClimateRule(
            "Air Quality Alert",
            "Alert when air quality is poor",
            ClimateRule.ClimateCondition.AIR_QUALITY_POOR,
            ClimateRule.ClimateAction.SEND_ALERT,
            0,
            "Poor air quality detected! Consider ventilation.",
            "all"
        );
        
        ClimateRule highCO2 = new ClimateRule(
            "High CO2 Alert",
            "Alert when CO2 levels are high",
            ClimateRule.ClimateCondition.HIGH_CO2,
            ClimateRule.ClimateAction.SEND_ALERT,
            1200.0,
            "High CO2 levels! Please ventilate the room.",
            "all"
        );
        
        // Add all rules
        climateRules.add(prayerRoomComfort);
        climateRules.add(wuduComfort);
        climateRules.add(overheating);
        climateRules.add(freezing);
        climateRules.add(nightEnergy);
        climateRules.add(airQualityAlert);
        climateRules.add(highCO2);
        
        System.out.println("✅ Initialized " + climateRules.size() + " default automation rules");
    }
    
    // Temperature Management
    public void setWholeHomeTemperature(double temperature) {
        thermostats.forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.setTemperature(temperature);
            }
        });
        System.out.println("Whole home temperature set to " + temperature + "°C");
    }
    
    public void setRoomTemperature(String roomName, double temperature) {
        thermostats.stream()
            .filter(thermostat -> thermostat.getLocation().equalsIgnoreCase(roomName) && thermostat.isOn())
            .forEach(thermostat -> thermostat.setTemperature(temperature));
        System.out.println("Room '" + roomName + "' temperature set to " + temperature + "°C");
    }
    
    public void optimizeForComfort() {
        thermostats.forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.optimizeForComfort();
            }
        });
        System.out.println("All thermostats optimized for comfort");
    }
    
    public void optimizeForEnergySavings() {
        thermostats.forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.optimizeForEnergySavings();
            }
        });
        System.out.println("All thermostats optimized for energy savings");
    }
    
    // Air Quality Management
    public void checkAirQualityStatus() {
        System.out.println("=== AIR QUALITY STATUS ===");
        airQualitySensors.forEach(sensor -> {
            if (sensor.isOn()) {
                String status = sensor.isAirQualityGood() ? "GOOD" : "POOR";
                System.out.println(sensor.getName() + " - AQI: " + 
                    String.format("%.0f", sensor.getAirQualityIndex()) + "/100 (" + status + ")");
            }
        });
    }
    
    public List<AirQualitySensor> getSensorsNeedingVentilation() {
        List<AirQualitySensor> needsVentilation = new ArrayList<>();
        airQualitySensors.forEach(sensor -> {
            if (sensor.isOn() && sensor.needsVentilation()) {
                needsVentilation.add(sensor);
            }
        });
        return needsVentilation;
    }
    
    // Energy Management
    public double getTotalEnergyConsumption() {
        return energyTracker.calculateTotalConsumption(thermostats);
    }
    
    public void generateEnergyReport() {
        energyTracker.generateReport(thermostats);
    }
    
    // Mode Management
    public void setAwayMode() {
        thermostats.forEach(thermostat -> {
            thermostat.setEcoMode(true);
            thermostat.setMode("ECO");
            thermostat.setTemperature(16.0);
        });
        System.out.println("Away mode activated - energy saving temperatures");
    }
    
    public void setHomeMode() {
        thermostats.forEach(thermostat -> {
            thermostat.setEcoMode(false);
            thermostat.setMode("AUTO");
            thermostat.setTemperature(22.0);
        });
        System.out.println("Home mode activated - comfort temperatures");
    }
    
    // Schedule Management
    public void applyDaySchedule() {
        thermostats.forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.applySchedule("DAY_TEMP");
            }
        });
        System.out.println("Day schedule applied");
    }
    
    public void applyNightSchedule() {
        thermostats.forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.applySchedule("NIGHT_TEMP");
            }
        });
        System.out.println("Night schedule applied");
    }
    
    // Automation Management
    public void addClimateRule(ClimateRule rule) {
        climateRules.add(rule);
        System.out.println("Added climate rule: " + rule.getName());
    }
    
    public void executeClimateRules() {
        System.out.println("Executing climate automation rules...");
        int rulesExecuted = 0;
        
        for (ClimateRule rule : climateRules) {
            // Check thermostats
            for (Thermostat thermostat : thermostats) {
                if (rule.appliesToDevice(thermostat.getLocation()) && 
                    rule.evaluateCondition(thermostat)) {
                    rule.executeAction(thermostat);
                    rulesExecuted++;
                }
            }
            
            // Check air quality sensors
            for (AirQualitySensor sensor : airQualitySensors) {
                if (rule.appliesToDevice(sensor.getLocation()) && 
                    rule.evaluateCondition(sensor)) {
                    rule.executeAction(sensor);
                    rulesExecuted++;
                }
            }
        }
        
        System.out.println("Executed " + rulesExecuted + " climate rules");
    }
    
    public void createOccupancyRule(String roomName, int emptyMinutesThreshold) {
        ClimateRule occupancyRule = new ClimateRule(
            "Occupancy-Based " + roomName,
            "Save energy when room is unoccupied",
            ClimateRule.ClimateCondition.TEMPERATURE_ABOVE,
            ClimateRule.ClimateAction.ENABLE_ECO_MODE,
            emptyMinutesThreshold,
            "true",
            roomName
        );
        addClimateRule(occupancyRule);
        System.out.println("Created occupancy rule for " + roomName);
    }
    
    // Getters
    public List<Thermostat> getThermostats() {
        return new ArrayList<>(thermostats);
    }
    
    public List<AirQualitySensor> getAirQualitySensors() {
        return new ArrayList<>(airQualitySensors);
    }
    
    public List<ClimateRule> getClimateRules() {
        return new ArrayList<>(climateRules);
    }
    
    public String getClimateSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== CLIMATE SUMMARY ===\n");
        summary.append("Thermostats: ").append(thermostats.size()).append("\n");
        summary.append("Air Quality Sensors: ").append(airQualitySensors.size()).append("\n");
        summary.append("Total Energy Consumption: ").append(String.format("%.2f", getTotalEnergyConsumption())).append(" kWh\n");
        
        summary.append("\n--- Thermostat Status ---\n");
        thermostats.forEach(thermostat -> {
            if (thermostat.isOn()) {
                summary.append(thermostat.getClimateStatus()).append("\n");
            }
        });
        
        summary.append("\n--- Air Quality Status ---\n");
        airQualitySensors.forEach(sensor -> {
            if (sensor.isOn()) {
                summary.append(sensor.getStatus()).append("\n");
            }
        });
        
        return summary.toString();
    }
}