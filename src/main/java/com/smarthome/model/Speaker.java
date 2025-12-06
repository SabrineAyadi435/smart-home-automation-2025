package com.smarthome.model;

import com.smarthome.interfaces.Controllable;
import com.smarthome.interfaces.EnergyConsumer;

public class Speaker extends SmartDevice implements Controllable, EnergyConsumer {
    private int volume;
    private boolean playing;
    private String energyMode;
    
    public Speaker(String deviceId, String name) {
        super(deviceId, name);
        this.volume = 50;
        this.playing = false;
        this.energyMode = "NORMAL";
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

    public void playAdhan() {
        if (!isOn) {
            turnOn();
        }
        setVolume(90);
        this.playing = true;
        System.out.println("[INFO] " + name + " is now playing Adhan loudly (Volume: " + this.volume + "%)");
    }
    
    // --- CORRECTED METHOD ---
    public void stopAdhan() {
        // Instead of turning off, we just stop playing and return to standby.
        this.playing = false;
        // We can also reset the volume to a normal level.
        setVolume(50); 
        System.out.println("[INFO] " + name + " finished playing Adhan and is now on standby.");
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
    
    @Override
    public void setEnergyMode(String mode) {
        this.energyMode = mode;
    }

    // --- METHOD FOR EDUCATIONAL CONTENT ---
    /**
     * Makes the speaker "speak" the given text.
     * @param text The text to be spoken.
     */
    public void speak(String text) {
        if (!isOn) {
            turnOn(); // Turn on the speaker if it's off
        }
        // Set a comfortable volume for spoken content
        setVolume(60); 
        System.out.println("[INFO] " + name + " is speaking: \"" + text + "\"");
    }
}