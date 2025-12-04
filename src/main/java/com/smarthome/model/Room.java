package com.smarthome.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private List<SmartDevice> devices;
    private double preferredTemperature;
    private String climateZoneType;
    private boolean climateOptimized;
    
    public Room(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
        this.preferredTemperature = 22.0;
        this.climateZoneType = "LIVING";
        this.climateOptimized = false;
        autoDetectClimateZone();
    }
    
    public void addDevice(SmartDevice device) {
        devices.add(device);
        System.out.println("Device " + device.getName() + " added to " + name);
    }
    
    
    
    
    
    public void setPreferredTemperature(double temperature) {
        this.preferredTemperature = temperature;
        System.out.println(name + " preferred temperature set to " + temperature + "°C");
    }
    
    public double getPreferredTemperature() {
        return preferredTemperature;
    }
    
    public void setClimateZoneType(String zoneType) {
        this.climateZoneType = zoneType;
        applyZoneDefaults(zoneType);
    }
    
    public String getClimateZoneType() {
        return climateZoneType;
    }
    
    public boolean isClimateOptimized() {
        return climateOptimized;
    }
    
    public void setClimateOptimized(boolean optimized) {
        this.climateOptimized = optimized;
    }
    
    public Thermostat getThermostat() {
        for (SmartDevice device : devices) {
            if (device instanceof Thermostat) {
                return (Thermostat) device;
            }
        }
        return null;
    }
    
    public AirQualitySensor getAirQualitySensor() {
        for (SmartDevice device : devices) {
            if (device instanceof AirQualitySensor) {
                return (AirQualitySensor) device;
            }
        }
        return null;
    }
    
    public String getClimateStatus() {
        Thermostat thermostat = getThermostat();
        AirQualitySensor airSensor = getAirQualitySensor();
        
        String tempStatus = (thermostat != null && thermostat.isOn()) ? 
            String.format("Current: %.1f°C", thermostat.getCurrentTemperature()) : "No thermostat";
        
        String airQualityStatus = (airSensor != null && airSensor.isOn()) ?
            String.format("AQI: %.0f/100", airSensor.getAirQualityIndex()) : "No air sensor";
            
        return String.format("Room: %s | %s | %s | Preferred: %.1f°C", 
            name, tempStatus, airQualityStatus, preferredTemperature);
    }
    
    public void applyPreferredTemperature() {
        Thermostat thermostat = getThermostat();
        if (thermostat != null && thermostat.isOn()) {
            thermostat.setTemperature(preferredTemperature);
            System.out.println("Applied " + name + "'s preferred temperature: " + preferredTemperature + "°C");
        }
    }
    
    private void autoDetectClimateZone() {
        String roomNameLower = name.toLowerCase();
        if (roomNameLower.contains("bedroom") || roomNameLower.contains("sleep")) {
            climateZoneType = "BEDROOM";
            preferredTemperature = 20.0;
        } else if (roomNameLower.contains("bathroom") || roomNameLower.contains("toilet")) {
            climateZoneType = "BATHROOM";
            preferredTemperature = 24.0;
        } else if (roomNameLower.contains("kitchen") || roomNameLower.contains("cooking")) {
            climateZoneType = "KITCHEN";
            preferredTemperature = 21.0;
        } else if (roomNameLower.contains("prayer") || roomNameLower.contains("mosque")) {
            climateZoneType = "PRAYER";
            preferredTemperature = 22.0;
        } else if (roomNameLower.contains("living") || roomNameLower.contains("lounge")) {
            climateZoneType = "LIVING";
            preferredTemperature = 22.0;
        }
    }
    
    private void applyZoneDefaults(String zoneType) {
        switch (zoneType.toUpperCase()) {
            case "BEDROOM": preferredTemperature = 20.0; break;
            case "KITCHEN": preferredTemperature = 21.0; break;
            case "BATHROOM": preferredTemperature = 24.0; break;
            case "PRAYER": preferredTemperature = 22.0; break;
            default: preferredTemperature = 22.0;
        }
    }
    
    public boolean hasClimateDevices() {
        return getThermostat() != null || getAirQualitySensor() != null;
    }
    
    public List<SmartDevice> getClimateDevices() {
        List<SmartDevice> climateDevices = new ArrayList<>();
        for (SmartDevice device : devices) {
            if (device instanceof Thermostat || device instanceof AirQualitySensor) {
                climateDevices.add(device);
            }
        }
        return climateDevices;
    }
    
    public String getName() {
        return name;
    }
    
    public List<SmartDevice> getDevices() {
        return new ArrayList<>(devices);
    }
    
    @Override
    public String toString() {
        return String.format("Room: %s | Zone: %s | Preferred Temp: %.1f°C", 
            name, climateZoneType, preferredTemperature);
    }
}