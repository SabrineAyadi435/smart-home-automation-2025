package com.smarthome.model;

public class MotionSensor extends SmartDevice {
    private boolean motionDetected;
    
    public MotionSensor(String deviceId, String name) {
        super(deviceId, name);
        this.motionDetected = false;
        this.isOn = true; // Sensors are always on
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " activated");
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        System.out.println(name + " deactivated");
    }
    
    @Override
    public String getStatus() {
        return isOn ? (motionDetected ? "ACTIVE - Motion Detected" : "ACTIVE - No Motion") : "INACTIVE";
    }
    
    public void detectMotion() {
        this.motionDetected = true;
        System.out.println(name + " detected motion!");
    }
    
    public void clearMotion() {
        this.motionDetected = false;
    }
    
    public boolean isMotionDetected() {
        return motionDetected;
    }
}
