package com.smarthome.model;

import com.smarthome.interfaces.Controllable;
import com.smarthome.interfaces.AirQualityMonitor;

public class AirQualitySensor extends SmartDevice implements Controllable, AirQualityMonitor {
    private double co2Level;
    private double humidity;
    private double airQualityIndex;
    private boolean alertsEnabled;
    private double co2Threshold;
    private String location;
    
    public AirQualitySensor(String deviceId, String name) {
        super(deviceId, name);
        
        this.co2Level = 450.0;
        this.humidity = 45.0;
        this.airQualityIndex = 85.0;
        this.alertsEnabled = true;
        this.co2Threshold = 1000.0;
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " air quality sensor activated");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        System.out.println(name + " air quality sensor deactivated");
    }
    
    @Override
    public String getStatus() {
        if (!isOn) return "OFF";
        return String.format("ON | CO2: %.0f ppm | Humidity: %.1f%% | AQI: %.0f/100", 
            co2Level, humidity, airQualityIndex);
    }
    
    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            turnOn();
        } else if (command.equalsIgnoreCase("OFF")) {
            turnOff();
        } else if (command.startsWith("SET_THRESHOLD:")) {
            try {
                double threshold = Double.parseDouble(command.split(":")[1]);
                setAirQualityAlert(threshold);
            } catch (Exception e) {
                System.out.println("Invalid threshold command");
            }
        }
    }
    
    @Override
    public boolean isControllable() {
        return true;
    }
    
    @Override
    public double getAirQualityIndex() {
        if (!isOn) return 0.0;
        updateReadings();
        return airQualityIndex;
    }
    
    @Override
    public double getCO2Level() {
        if (!isOn) return 0.0;
        updateReadings();
        return co2Level;
    }
    
    @Override
    public double getHumidity() {
        if (!isOn) return 0.0;
        updateReadings();
        return humidity;
    }
    
    @Override
    public boolean isAirQualityGood() {
        return getAirQualityIndex() >= 70.0 && co2Level <= co2Threshold;
    }
    
    @Override
    public void setAirQualityAlert(double threshold) {
        this.co2Threshold = threshold;
        System.out.println(name + " CO2 alert threshold set to " + threshold + " ppm");
    }
    
    @Override
    public String getLocation() {
        return location;
    }
    
    @Override
    public boolean needsVentilation() {
        return co2Level > 800;
    }
    
    private void updateReadings() {
        if (!isOn) return;
        co2Level += (Math.random() * 50) - 20;
        humidity += (Math.random() * 5) - 2.5;
        co2Level = Math.max(350, Math.min(2000, co2Level));
        humidity = Math.max(20, Math.min(80, humidity));
        calculateAirQualityIndex();
        checkAlerts();
    }
    
    private void calculateAirQualityIndex() {
        double co2Score = Math.max(0, 100 - ((co2Level - 350) / 16.5));
        double humidityScore = 100 - Math.abs(humidity - 45) * 2;
        airQualityIndex = (co2Score * 0.7) + (humidityScore * 0.3);
        airQualityIndex = Math.max(0, Math.min(100, airQualityIndex));
    }
    
    private void checkAlerts() {
        if (!alertsEnabled) return;
        if (co2Level > co2Threshold) {
            System.out.println("⚠️ ALERT: " + name + " - High CO2: " + String.format("%.0f", co2Level) + " ppm");
        }
        if (airQualityIndex < 40) {
            System.out.println("🌫️ ALERT: " + name + " - Poor air quality: " + String.format("%.0f", airQualityIndex) + "/100");
        }
    }
    
    public void enableAlerts(boolean enabled) {
        this.alertsEnabled = enabled;
    }
}