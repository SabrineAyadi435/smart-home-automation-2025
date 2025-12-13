package com.devices;

import com.enums.EnergyMode;
import com.interfaces.EnergyConsumer;
import com.room.Room;

public abstract class SmartDevice implements EnergyConsumer {
    protected String deviceId;
    protected String name;
    protected boolean isOn;
    protected EnergyMode energyMode;
    protected double powerConsumption;
    protected Room room;
    
    
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
    
    @Override
    public void setEnergyMode(EnergyMode mode) {
        this.energyMode = mode;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", deviceId, name, getStatus());
    }

    public void setRoom(Room room) {     // <-- ADDED
        this.room = room;
    }

    public Room getRoom() {              // <-- ADDED
        return room;
    }

}