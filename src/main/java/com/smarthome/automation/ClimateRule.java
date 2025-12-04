package com.smarthome.automation;

import com.smarthome.model.Thermostat;
import com.smarthome.model.AirQualitySensor;

public class ClimateRule {
    public enum ClimateCondition {
        TEMPERATURE_ABOVE, 
        TEMPERATURE_BELOW, 
        AIR_QUALITY_POOR, 
        HIGH_HUMIDITY, 
        HIGH_CO2
    }
    
    public enum ClimateAction {
        SET_TEMPERATURE, 
        SET_MODE, 
        ENABLE_ECO_MODE, 
        SEND_ALERT, 
        APPLY_SCHEDULE
    }
    
    private String name;
    private String description;
    private ClimateCondition condition;
    private ClimateAction action;
    private double threshold;
    private String targetValue;
    private String targetDevice;
    
    public ClimateRule(String name, String description, ClimateCondition condition, 
                      ClimateAction action, double threshold, String targetValue, String targetDevice) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.action = action;
        this.threshold = threshold;
        this.targetValue = targetValue;
        this.targetDevice = targetDevice;
    }
    
    public boolean evaluateCondition(Object device) {
        if (device == null) return false;
        
        switch (condition) {
            case TEMPERATURE_ABOVE:
                if (device instanceof Thermostat) {
                    Thermostat thermostat = (Thermostat) device;
                    return thermostat.getCurrentTemperature() > threshold;
                }
                break;
                
            case TEMPERATURE_BELOW:
                if (device instanceof Thermostat) {
                    Thermostat thermostat = (Thermostat) device;
                    return thermostat.getCurrentTemperature() < threshold;
                }
                break;
                
            case AIR_QUALITY_POOR:
                if (device instanceof AirQualitySensor) {
                    AirQualitySensor sensor = (AirQualitySensor) device;
                    return !sensor.isAirQualityGood();
                }
                break;
                
            case HIGH_HUMIDITY:
                if (device instanceof AirQualitySensor) {
                    AirQualitySensor sensor = (AirQualitySensor) device;
                    return sensor.getHumidity() > threshold;
                }
                break;
                
            case HIGH_CO2:
                if (device instanceof AirQualitySensor) {
                    AirQualitySensor sensor = (AirQualitySensor) device;
                    return sensor.getCO2Level() > threshold;
                }
                break;
        }
        
        return false;
    }
    
    public void executeAction(Object device) {
        if (device == null) return;
        
        switch (action) {
            case SET_TEMPERATURE:
                if (device instanceof Thermostat) {
                    Thermostat thermostat = (Thermostat) device;
                    try {
                        double temperature = Double.parseDouble(targetValue);
                        thermostat.setTemperature(temperature);
                        System.out.println("🌡️  Climate Rule: Set " + thermostat.getName() + " to " + temperature + "°C");
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid temperature value: " + targetValue);
                    }
                }
                break;
                
            case SET_MODE:
                if (device instanceof Thermostat) {
                    Thermostat thermostat = (Thermostat) device;
                    thermostat.setMode(targetValue);
                    System.out.println("🔄 Climate Rule: Set " + thermostat.getName() + " to " + targetValue + " mode");
                }
                break;
                
            case ENABLE_ECO_MODE:
                if (device instanceof Thermostat) {
                    Thermostat thermostat = (Thermostat) device;
                    thermostat.setEcoMode(true);
                    System.out.println("🌿 Climate Rule: Enabled eco mode for " + thermostat.getName());
                }
                break;
                
            case SEND_ALERT:
                System.out.println("🚨 CLIMATE ALERT: " + targetValue);
                break;
                
            case APPLY_SCHEDULE:
                if (device instanceof Thermostat) {
                    Thermostat thermostat = (Thermostat) device;
                    thermostat.applySchedule(targetValue);
                    System.out.println("📅 Climate Rule: Applied " + targetValue + " schedule to " + thermostat.getName());
                }
                break;
        }
    }
    
    public boolean appliesToDevice(String deviceLocation) {
        return targetDevice.equals("all") || targetDevice.equalsIgnoreCase(deviceLocation);
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ClimateCondition getClimateCondition() {
        return condition;
    }
    
    public ClimateAction getClimateAction() {
        return action;
    }
    
    public double getThreshold() {
        return threshold;
    }
    
    public String getTargetValue() {
        return targetValue;
    }
    
    public String getTargetDevice() {
        return targetDevice;
    }
    
    public String getRuleDescription() {
        String conditionText = "";
        switch (condition) {
            case TEMPERATURE_ABOVE:
                conditionText = "temperature above " + threshold + "°C";
                break;
            case TEMPERATURE_BELOW:
                conditionText = "temperature below " + threshold + "°C";
                break;
            case AIR_QUALITY_POOR:
                conditionText = "poor air quality";
                break;
            case HIGH_HUMIDITY:
                conditionText = "humidity above " + threshold + "%";
                break;
            case HIGH_CO2:
                conditionText = "CO2 above " + threshold + " ppm";
                break;
        }
        
        String actionText = "";
        switch (action) {
            case SET_TEMPERATURE:
                actionText = "set temperature to " + targetValue + "°C";
                break;
            case SET_MODE:
                actionText = "set mode to " + targetValue;
                break;
            case ENABLE_ECO_MODE:
                actionText = "enable eco mode";
                break;
            case SEND_ALERT:
                actionText = "send alert: " + targetValue;
                break;
            case APPLY_SCHEDULE:
                actionText = "apply " + targetValue + " schedule";
                break;
        }
        
        return String.format("IF %s THEN %s", conditionText, actionText);
    }
    
    @Override
    public String toString() {
        return String.format("ClimateRule: %s | When %s %.1f | Then %s %s | Device: %s", 
            name, condition, threshold, action, targetValue, targetDevice);
    }
}