package com.devices;

import com.enums.EnergyMode;

public abstract class SmartDevice {
    protected String deviceId;
    protected String name;
    protected boolean isOn;
    protected EnergyMode energyMode;
    
    public SmartDevice(String deviceId, String name, EnergyMode energyMode) {
        this.deviceId = deviceId;
        this.name = name;
        this.isOn = false;
        this.energyMode = energyMode;
    }
    
    public abstract void turnOn();
    public abstract void turnOff();
    public abstract String getStatus();
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isOn() {
        return isOn;
    }

    public EnergyMode getEnergyMode() {
        return energyMode;
    }
    
    public void setEnergyMode(EnergyMode mode) {
        this.energyMode = mode;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", deviceId, name, getStatus());
    }
}