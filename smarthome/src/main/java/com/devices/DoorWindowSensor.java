
package com.devices;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.enums.EnergyMode;
import com.exceptions.SecurityBreachException;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;


public class DoorWindowSensor extends SmartDevice implements Controllable, EnergyConsumer {
    private boolean isOpen;
    private String location;
    private int openCount = 0;
    private boolean isOn;
    private java.time.LocalDateTime lastUpdated;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // energy
    private double standbyConsumption = 0.01;
    private double activeConsumption = 0.5;
    
    public DoorWindowSensor(String deviceId, String name, String location, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.location = location;
        this.isOpen = false;
        this.lastUpdated = java.time.LocalDateTime.now();
        this.isOn = false;
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        updateTimestamp();
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        updateTimestamp();
    }
    
    @Override
    public boolean isOn() {
        return isOn;
    }
    
    public boolean checkStatus() throws SecurityBreachException {
        if (!isOn) return isOpen;
        
        // Simulate status check - in real system, read from magnetic sensor
        Random rand = new Random();
        boolean statusChanged = rand.nextInt(100) < 10; // 10% chance of change
        
        if (statusChanged) {
            isOpen = !isOpen;
            if (isOpen) openCount++;
            updateTimestamp();
            
            logEvent((isOpen ? "Opened" : "Closed"));
            
            if (isOpen && isOn) {
                throw new SecurityBreachException(
                    "Door/Window opened in " + location, 
                    location
                );
            }
        }
        return isOpen;
    }
    
    public void simulateOpen() throws SecurityBreachException {
        if (!isOpen) {
            isOpen = true;
            openCount++;
            updateTimestamp();
            
            if (isOn) {
                throw new SecurityBreachException(
                    "Door/Window forced open in " + location, 
                    location
                );
            }
        }
    }
    
    public void simulateClose() {
        if (isOpen) {
            isOpen = false;
            updateTimestamp();
        }
    }
    
    @Override
    public String getStatus() {
        return String.format("DoorWindowSensor[%s] - Location: %s | State: %s | Open Count: %d",
                name, location, isOpen ? "OPEN" : "CLOSED", openCount);
    }
    
    public String getDeviceType() {
        return "DOOR_WINDOW_SENSOR";
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + 
                          " - DoorWindowSensor[" + deviceId + "]: " + event);
    }

    @Override
    public void executeCommand(String command) {
        if (command == null) return;
        String cmd = command.trim().toUpperCase();
        try {
            if (cmd.equals("ON") || cmd.equals("ACTIVATE") ) {
                turnOn();
                System.out.println("DoorWindowSensor activated");
            } else if (cmd.equals("OFF") || cmd.equals("DEACTIVATE")) {
                turnOff();
                System.out.println("DoorWindowSensor deactivated");
            } else if (cmd.equals("STATUS")) {
                System.out.println(getStatus());
            } else if (cmd.equals("OPEN")) {
                try { simulateOpen(); } catch (SecurityBreachException e) { System.err.println("Security breach: " + e.getMessage()); }
            } else if (cmd.equals("CLOSE")) {
                simulateClose();
            } else {
                System.out.println("Unknown command for DoorWindowSensor: " + command);
            }
        } catch (Exception e) {
            System.err.println("Command failed: " + e.getMessage());
        }
    }


    @Override
    public boolean isControllable() {
        return true;
    }


    @Override
    public double getEnergyConsumption() {
        double base = isOpen ? activeConsumption : standbyConsumption;
        return energyMode == EnergyMode.ECO ? base * 0.8 : base;
    }

    private void updateTimestamp() {
        this.lastUpdated = java.time.LocalDateTime.now();
    }

    @Override
    public void setEnergyMode(EnergyMode mode) {
        super.setEnergyMode(mode);
        this.energyMode = mode;
    }
    
    public String getLastReading() {
        return isOpen ? "Open" : "Closed";
    }
    
    public LocalDateTime getLastUpdateTime() {
        return lastUpdated;
    }
}