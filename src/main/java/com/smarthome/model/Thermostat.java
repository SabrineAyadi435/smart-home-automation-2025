package com.smarthome.model;

import com.smarthome.interfaces.Controllable;
import com.smarthome.interfaces.EnergyConsumer;
import com.smarthome.interfaces.Schedulable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Thermostat extends SmartDevice implements Controllable, EnergyConsumer, Schedulable {
    private double targetTemperature;
    private double currentTemperature;
    private String energyMode;
    private Map<LocalTime, Runnable> scheduledTasks;
    
    public Thermostat(String deviceId, String name, double targetTemperature) {
        super(deviceId, name);
        this.targetTemperature = targetTemperature;
        this.currentTemperature = 20.0;
        this.energyMode = "NORMAL";
        this.scheduledTasks = new HashMap<>();
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " turned ON - Target: " + targetTemperature + "°C");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        System.out.println(name + " turned OFF");
    }
    
    @Override
    public String getStatus() {
        return isOn ? String.format("ON (Target: %.1f°C, Current: %.1f°C)", 
            targetTemperature, currentTemperature) : "OFF";
    }
    
    public void setTargetTemperature(double temperature) {
        if (temperature < 10 || temperature > 35) {
            throw new IllegalArgumentException("Temperature must be between 10 and 35°C");
        }
        this.targetTemperature = temperature;
        System.out.println(name + " target temperature set to " + temperature + "°C");
    }
    
    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            turnOn();
        } else if (command.equalsIgnoreCase("OFF")) {
            turnOff();
        }
    }
    
    @Override
    public boolean isControllable() {
        return true;
    }
    
    @Override
    public double getEnergyConsumption() {
        return isOn ? Math.abs(targetTemperature - currentTemperature) * 2.5 : 0.0;
    }
    
    @Override
    public void setEnergyMode(String mode) {
        this.energyMode = mode;
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
}
