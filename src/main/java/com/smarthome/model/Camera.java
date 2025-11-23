package com.smarthome.model;

import com.smarthome.interfaces.Controllable;

public class Camera extends SmartDevice implements Controllable {
    private boolean recording;
    private String resolution;
    
    public Camera(String deviceId, String name) {
        super(deviceId, name);
        this.recording = false;
        this.resolution = "1080p";
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " turned ON");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        this.recording = false;
        System.out.println(name + " turned OFF");
    }
    
    @Override
    public String getStatus() {
        if (!isOn) return "OFF";
        return recording ? "RECORDING (" + resolution + ")" : "STANDBY (" + resolution + ")";
    }
    
    public void startRecording() {
        if (isOn) {
            this.recording = true;
            System.out.println(name + " started recording");
        }
    }
    
    public void stopRecording() {
        this.recording = false;
        System.out.println(name + " stopped recording");
    }
    
    public boolean isRecording() {
        return recording;
    }
    
    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            turnOn();
        } else if (command.equalsIgnoreCase("OFF")) {
            turnOff();
        } else if (command.equalsIgnoreCase("RECORD")) {
            startRecording();
        } else if (command.equalsIgnoreCase("STOP")) {
            stopRecording();
        }
    }
    
    @Override
    public boolean isControllable() {
        return true;
    }
}
