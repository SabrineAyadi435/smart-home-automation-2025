package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

public class SmartPlug extends SmartDevice implements Controllable, EnergyConsumer {
    private double powerUsage;
    
    public SmartPlug(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.powerUsage = 0.0;
        this.energyMode = energyMode;
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
    

}