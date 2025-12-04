package com.smarthome.model;

import com.smarthome.interfaces.Controllable;
import com.smarthome.interfaces.EnergyConsumer;
import com.smarthome.interfaces.Schedulable;
import com.smarthome.interfaces.ClimateControllable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Thermostat extends SmartDevice implements Controllable, EnergyConsumer, Schedulable, ClimateControllable {
    private double targetTemperature;
    private double currentTemperature;
    private String energyMode;
    private Map<LocalTime, Runnable> scheduledTasks;
    private String location;
    
    private String climateMode;
    private boolean ecoMode;
    private double minTemperature = 10.0;
    private double maxTemperature = 35.0;
    private boolean isActive;
    private long lastUpdateTime;
    private double totalEnergyConsumed;
    private Map<String, Double> schedules;
    
    public Thermostat(String deviceId, String name,  double targetTemperature) {
        super(deviceId, name);
        
        this.targetTemperature = targetTemperature;
        this.currentTemperature = 20.0;
        this.energyMode = "NORMAL";
        this.scheduledTasks = new HashMap<>();
        this.climateMode = "AUTO";
        this.ecoMode = false;
        this.totalEnergyConsumed = 0.0;
        this.lastUpdateTime = System.currentTimeMillis();
        this.schedules = new HashMap<>();
        initializeDefaultSchedules();
        updateDeviceState();
    }
    
    private void initializeDefaultSchedules() {
        schedules.put("DAY_TEMP", 22.0);
        schedules.put("NIGHT_TEMP", 18.0);
        schedules.put("AWAY_TEMP", 16.0);
        schedules.put("ECO_TEMP", 20.0);
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " turned ON - Target: " + targetTemperature + "°C");
        updateDeviceState();
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        this.climateMode = "OFF";
        System.out.println(name + " turned OFF");
        updateDeviceState();
    }
    
    @Override
    public String getStatus() {
        if (!isOn) return "OFF";
        return String.format("ON (Mode: %s, Target: %.1f°C, Current: %.1f°C, Eco: %s)", 
            climateMode, targetTemperature, getCurrentTemperature(), ecoMode);
    }
    
    public void setTargetTemperature(double temperature) {
        if (temperature < minTemperature || temperature > maxTemperature) {
            throw new IllegalArgumentException("Temperature must be between " + minTemperature + " and " + maxTemperature + "°C");
        }
        
        if (ecoMode && climateMode.equals("HEAT") && temperature > 20.0) {
            temperature = 20.0;
        } else if (ecoMode && climateMode.equals("COOL") && temperature < 25.0) {
            temperature = 25.0;
        }
        
        this.targetTemperature = temperature;
        System.out.println(name + " target temperature set to " + temperature + "°C");
        updateDeviceState();
    }
    
    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            turnOn();
        } else if (command.equalsIgnoreCase("OFF")) {
            turnOff();
        } else if (command.startsWith("SET_TEMP:")) {
            try {
                double temp = Double.parseDouble(command.split(":")[1]);
                setTargetTemperature(temp);
            } catch (Exception e) {
                System.out.println("Invalid temperature command");
            }
        } else if (command.startsWith("SET_MODE:")) {
            setMode(command.split(":")[1]);
        } else if (command.equalsIgnoreCase("ECO_ON")) {
            setEcoMode(true);
        } else if (command.equalsIgnoreCase("ECO_OFF")) {
            setEcoMode(false);
        }
    }
    
    @Override
    public boolean isControllable() {
        return true;
    }
    
    @Override
    public double getEnergyConsumption() {
        updateEnergyConsumption();
        return totalEnergyConsumed;
    }
    
    @Override
    public void setEnergyMode(String mode) {
        this.energyMode = mode;
        this.ecoMode = mode.equals("ECO");
    }
    
    @Override
    public void scheduleTask(LocalTime time, Runnable task) {
        scheduledTasks.put(time, task);
        System.out.println("Task scheduled for " + time);
    }
    
    @Override
    public void cancelScheduledTasks() {
        scheduledTasks.clear();
        System.out.println("All scheduled tasks cancelled");
    }
    
    @Override
    public void setTemperature(double temperature) {
        setTargetTemperature(temperature);
    }
    
    @Override
    public double getCurrentTemperature() {
        double variation = (Math.random() * 2.0) - 1.0;
        return targetTemperature + variation;
    }
    
    @Override
    public double getTargetTemperature() {
        return targetTemperature;
    }
    
    @Override
    public void setMode(String mode) {
        String[] validModes = {"HEAT", "COOL", "AUTO", "OFF", "ECO"};
        for (String validMode : validModes) {
            if (validMode.equals(mode)) {
                this.climateMode = mode;
                if (mode.equals("OFF")) {
                    turnOff();
                } else if (!isOn) {
                    turnOn();
                }
                updateDeviceState();
                return;
            }
        }
        throw new IllegalArgumentException("Invalid mode: " + mode);
    }
    
    @Override
    public String getMode() {
        return climateMode;
    }
    
    @Override
    public void setEcoMode(boolean enabled) {
        this.ecoMode = enabled;
        this.energyMode = enabled ? "ECO" : "NORMAL";
        if (enabled) {
            double currentTemp = getTargetTemperature();
            if (climateMode.equals("HEAT") && currentTemp > 20.0) {
                setTargetTemperature(20.0);
            } else if (climateMode.equals("COOL") && currentTemp < 25.0) {
                setTargetTemperature(25.0);
            }
        }
    }
    
    @Override
    public boolean isEcoModeActive() {
        return ecoMode;
    }
    
    @Override
    public double getMinTemperature() {
        return minTemperature;
    }
    
    @Override
    public double getMaxTemperature() {
        return maxTemperature;
    }
    
    @Override
    public boolean isActive() {
        return isActive && isOn && !climateMode.equals("OFF");
    }
    
    @Override
    public String getLocation() {
        return location;
    }
    
    public void setSchedule(String scheduleType, double temperature) {
        if (schedules.containsKey(scheduleType)) {
            schedules.put(scheduleType, temperature);
            System.out.println(name + " " + scheduleType.toLowerCase() + " schedule set to " + temperature + "°C");
        }
    }
    
    public void applySchedule(String scheduleType) {
        if (schedules.containsKey(scheduleType) && isOn) {
            setTargetTemperature(schedules.get(scheduleType));
            System.out.println(name + " applied " + scheduleType.toLowerCase() + " schedule");
        }
    }
    
    public void optimizeForComfort() {
        setEcoMode(false);
        setMode("AUTO");
        setTargetTemperature(22.0);
        System.out.println(name + " optimized for comfort");
    }
    
    public void optimizeForEnergySavings() {
        setEcoMode(true);
        setMode("ECO");
        setTargetTemperature(18.0);
        System.out.println(name + " optimized for energy savings");
    }
    
    public String getClimateStatus() {
        return String.format("Thermostat: %s | Mode: %s | Target: %.1f°C | Current: %.1f°C | Eco: %s", 
            getName(), climateMode, targetTemperature, getCurrentTemperature(), ecoMode);
    }
    
    private void updateEnergyConsumption() {
        if (!isOn) {
            isActive = false;
            return;
        }
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - lastUpdateTime;
        if (isActive) {
            double consumptionRate = calculateConsumptionRate();
            double hours = timeDiff / (1000.0 * 60.0 * 60.0);
            totalEnergyConsumed += consumptionRate * hours;
        }
        lastUpdateTime = currentTime;
    }
    
    private double calculateConsumptionRate() {
        switch (climateMode) {
            case "HEAT": return ecoMode ? 1.5 : 2.5;
            case "COOL": return ecoMode ? 1.2 : 2.0;
            case "AUTO": return ecoMode ? 1.0 : 1.8;
            case "ECO": return 0.5;
            default: return 0.1;
        }
    }
    
    private void updateDeviceState() {
        double currentTemp = getCurrentTemperature();
        double tempDiff = Math.abs(currentTemp - targetTemperature);
        this.isActive = isOn && !climateMode.equals("OFF") && tempDiff > 0.5;
    }
}