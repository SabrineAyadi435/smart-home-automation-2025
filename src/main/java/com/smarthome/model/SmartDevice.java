package com.smarthome.model;

public abstract class SmartDevice {
    protected String deviceId;
    protected String name;
    protected boolean isOn;
    
    public SmartDevice(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
        this.isOn = false;
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
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", deviceId, name, getStatus());
    }
}
