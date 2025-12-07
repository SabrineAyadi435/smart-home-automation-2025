package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

public class Speaker extends SmartDevice implements Controllable, EnergyConsumer {
    private int volume;
    private boolean playing;
    
    public Speaker(String deviceId, String name , EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.volume = 50;
        this.playing = false;
        this.energyMode = energyMode;
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " turned ON");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        this.playing = false;
        System.out.println(name + " turned OFF");
    }
    
    @Override
    public String getStatus() {
        if (!isOn) return "OFF";
        return playing ? "PLAYING (Vol: " + volume + "%)" : "STANDBY (Vol: " + volume + "%)";
    }
    
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }
        this.volume = volume;
        System.out.println(name + " volume set to " + volume + "%");
    }
    
    public void play() {
        if (isOn) {
            this.playing = true;
            System.out.println(name + " started playing");
        }
    }
    
    public void pause() {
        this.playing = false;
        System.out.println(name + " paused");
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            turnOn();
        } else if (command.equalsIgnoreCase("OFF")) {
            turnOff();
        } else if (command.equalsIgnoreCase("PLAY")) {
            play();
        } else if (command.equalsIgnoreCase("PAUSE")) {
            pause();
        }
    }
    
    @Override
    public boolean isControllable() {
        return true;
    }
    
    @Override
    public double getEnergyConsumption() {
        return isOn ? (playing ? volume * 0.08 : 2.0) : 0.0;
    }
    
}