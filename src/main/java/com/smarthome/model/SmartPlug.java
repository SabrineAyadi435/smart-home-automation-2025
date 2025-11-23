package com.smarthome.model;

import com.smarthome.interfaces.Controllable;
import com.smarthome.interfaces.EnergyConsumer;

public class SmartPlug extends SmartDevice implements Controllable, EnergyConsumer {
    private double powerUsage;
    private String energyMode;
    
    public SmartPlug(String deviceId, String name) {
        super(deviceId, name);
        this.powerUsage = 0.0;
        this.energyMode = "NORMAL";
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        this.powerUsage = 5.0;
        System.out.println(name + " turned ON");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        this.powerUsage = 0.0;
        System.out.println(name + " turned OFF");
    }
    
    @Override
    public String getStatus() {
        return isOn ? String.format("ON (%.1fW)", powerUsage) : "OFF";
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
        return isOn ? powerUsage : 0.0;
    }
    
    @Override
    public void setEnergyMode(String mode) {
        this.energyMode = mode;
    }
}
