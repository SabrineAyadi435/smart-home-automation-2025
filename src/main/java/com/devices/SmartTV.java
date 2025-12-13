package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

public class SmartTV extends SmartDevice implements Controllable, EnergyConsumer {
    private int volume;
    private int channel;
    private EnergyMode energyMode;
    
    public SmartTV(String deviceId, String name) {
        super(deviceId, name, EnergyMode.NORMAL);
        this.volume = 50;
        this.channel = 1;
        this.energyMode = EnergyMode.NORMAL;
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
        return isOn ? String.format("ON (Channel: %d, Volume: %d)", channel, volume) : "OFF";
    }
    
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }
        this.volume = volume;
    }
    
    public void setChannel(int channel) {
        this.channel = channel;
    }
    
    public int getVolume() {
        return volume;
    }
    
    public int getChannel() {
        return channel;
    }
    
    private String inputSource = "HDMI1"; // HDMI1, HDMI2, HDMI3, USB, TV
    
    public void setInputSource(String source) {
        if (!source.equals("HDMI1") && !source.equals("HDMI2") && !source.equals("HDMI3") && 
            !source.equals("USB") && !source.equals("TV")) {
            throw new IllegalArgumentException("Input source must be HDMI1, HDMI2, HDMI3, USB, or TV");
        }
        this.inputSource = source;
        System.out.println(name + " input source set to " + source);
    }
    
    public String getInputSource() {
        return inputSource;
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
        double base = isOn ? 15.0 : 0.0;
        return energyMode == EnergyMode.ECO ? base * 0.8 : energyMode == EnergyMode.HIGH ? base * 2 : base;
    }
    
    @Override
    public void setEnergyMode(EnergyMode mode) {
        this.energyMode = mode;
    }

   
}