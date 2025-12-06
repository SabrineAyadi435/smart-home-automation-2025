package com.smarthome.model;

import com.smarthome.interfaces.Controllable;
import com.smarthome.interfaces.EnergyConsumer;

public class SmartTV extends SmartDevice implements Controllable, EnergyConsumer {
    private int volume;
    private int channel;
    private String energyMode;
    private boolean isMuted;  // Add this field
    private int previousVolume; // To restore volume after unmute
    
    public SmartTV(String deviceId, String name) {
        super(deviceId, name);
        this.volume = 50;
        this.channel = 1;
        this.energyMode = "NORMAL";
        this.isMuted = false;
        this.previousVolume = 50;
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
        return isOn ? String.format("ON (Channel: %d, Volume: %d%s)", channel, volume, isMuted ? " - MUTED" : "") : "OFF";
    }
    
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }
        this.volume = volume;
        if (volume > 0) {
            this.isMuted = false; // Automatically unmute when volume is set above 0
        }
    }
    
    public void setChannel(int channel) {
        this.channel = channel;
    }
    
    // ADD THESE MUTE METHODS:
    public void mute() {
        if (!isMuted && isOn) {
            this.previousVolume = this.volume;
            this.volume = 0;
            this.isMuted = true;
            System.out.println("TV " + getName() + " is now muted");
        }
    }
    
    public void unmute() {
        if (isMuted && isOn) {
            this.volume = this.previousVolume;
            this.isMuted = false;
            System.out.println("TV " + getName() + " is now unmuted. Volume: " + volume);
        }
    }
    
    public boolean isMuted() {
        return isMuted;
    }
    
    public void executeMuteCommand(boolean mute) {
        if (mute) {
            mute();
        } else {
            unmute();
        }
    }
    
    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            turnOn();
        } else if (command.equalsIgnoreCase("OFF")) {
            turnOff();
        } else if (command.equalsIgnoreCase("MUTE")) {
            mute();
        } else if (command.equalsIgnoreCase("UNMUTE")) {
            unmute();
        }
    }
    
    @Override
    public boolean isControllable() {
        return true;
    }
    
    @Override
    public double getEnergyConsumption() {
        return isOn ? 15.0 : 0.5;
    }
    
    @Override
    public void setEnergyMode(String mode) {
        this.energyMode = mode;
    }
}