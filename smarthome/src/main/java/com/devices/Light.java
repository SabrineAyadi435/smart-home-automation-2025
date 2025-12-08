package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

public class Light extends SmartDevice implements Controllable, EnergyConsumer {
    private int brightness;
    private final double powerConsumption = 10.0;

    
    public Light(String deviceId, String name, int brightness, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.brightness = brightness;
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " turned ON");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        System.out.println(name + " turned OFF");
    }
    
    @Override
    public String getStatus() {
        return isOn ? "ON (Brightness: " + brightness + "%)" : "OFF";
    }
    
    public void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("Brightness must be between 0 and 100");
        }
        this.brightness = brightness;
        System.out.println(name + " brightness set to " + brightness + "%");
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
        return isOn ? (brightness * 0.1 * powerConsumption) : 0.0;
    }

    // public double getPowerConsumption() {
    //     return powerConsumption;
    // }
    

}