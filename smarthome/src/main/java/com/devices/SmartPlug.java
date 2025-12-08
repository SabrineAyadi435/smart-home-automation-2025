package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

public class SmartPlug extends SmartDevice implements Controllable, EnergyConsumer {
    private double powerConsumption = 20.0;
    private boolean isOn;
    
    public SmartPlug(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.isOn = true;
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        this.powerConsumption = 20.0;
        System.out.println(name + " is ON");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        this.powerConsumption = 0.0;
        System.out.println(name + " is OFF");
    }
    
    @Override
    public String getStatus() {
        return isOn ? String.format("ON (%.1fW)", powerConsumption) : "OFF";
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

    public double getEnergyConsumption() {
        return isOn ? powerConsumption : 0.0;
    }
    

}