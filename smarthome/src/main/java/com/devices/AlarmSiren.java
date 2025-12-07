
package com.devices;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.enums.EnergyMode;
import com.exceptions.InvalidOperationException;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

public class AlarmSiren extends SmartDevice implements Controllable, EnergyConsumer {
    private int volume; // 1-10
    private boolean isSounding;
    private int durationSeconds;
    private double standbyConsumption = 0.1;
    private double activeConsumption = 50.0;
    private boolean isActive;
    private boolean energySavingMode = false;
    private java.time.LocalDateTime lastUpdated;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public AlarmSiren(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.volume = 8;
        this.isSounding = false;
        this.durationSeconds = 30;
        this.energyMode = energyMode;
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public void turnOn() {
        if (isSounding) {
            System.err.println("Alarm is already sounding");
            return;
        }
        this.isActive = true;
        this.isSounding = true;
        updateTimestamp();
        startAlarmSequence();
    }
    
    @Override
    public void turnOff() {
        this.isSounding = false;
        this.isActive = false;
        updateTimestamp();
        stopAlarmSequence();
    }
    
    @Override
    public boolean isOn() {
        return isSounding;
    }
    
    public void triggerAlarm() throws InvalidOperationException {
        turnOn();
        logEvent("Alarm triggered at volume " + volume);
    }
    
    public void stopAlarm() {
        turnOff();
        logEvent("Alarm stopped");
    }
    
    public void setVolume(int level) throws InvalidOperationException {
        if (level < 1 || level > 10) {
            throw new InvalidOperationException("Volume must be between 1 and 10");
        }
        this.volume = level;
        updateTimestamp();
    }
    
    public void setDuration(int seconds) throws InvalidOperationException {
        if (seconds < 10 || seconds > 300) {
            throw new InvalidOperationException("Duration must be between 10 and 300 seconds");
        }
        this.durationSeconds = seconds;
    }
    
    private void startAlarmSequence() {
        System.out.println("🚨 ALARM SOUNDING at volume " + volume + " for " + durationSeconds + " seconds!");
        // In real implementation, this would trigger hardware alarm
    }
    
    private void stopAlarmSequence() {
        System.out.println("Alarm silenced");
    }
    
    @Override
    public String getStatus() {
        return String.format("AlarmSiren[%s] - Sounding: %s | Volume: %d | Duration: %ds",
            name, isSounding ? "YES" : "NO", volume, durationSeconds);
    }
    
    public double calculateEnergyConsumption() {
        double baseConsumption = isSounding ? activeConsumption : standbyConsumption;
        return energySavingMode ? baseConsumption * 0.8 : baseConsumption;
    }
    public double getEnergyConsumptionRate() {
        return calculateEnergyConsumption();
    }
    public void setEnergySavingMode(boolean enable) {
        this.energySavingMode = enable;
        if (enable && volume > 6) {
            volume = 6; // Reduce volume in energy saving mode
        }
    }
    public String getDeviceType() {
        return "ALARM_SIREN";
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + 
                          " - AlarmSiren[" + deviceId + "]: " + event);
    }

    @Override
    public void executeCommand(String command) {
        if (command == null) return;
        String cmd = command.trim().toUpperCase();
        try {
            if (cmd.equals("TRIGGER") || cmd.equals("ON") || cmd.equals("START")) {
                triggerAlarm();
            } else if (cmd.equals("STOP") || cmd.equals("OFF") || cmd.equals("SILENCE")) {
                stopAlarm();
            } else if (cmd.startsWith("SET_VOLUME") || cmd.startsWith("VOLUME")) {
                // support formats: SET_VOLUME 5  or VOLUME:5
                String[] parts = cmd.replace(':', ' ').split("\\s+");
                if (parts.length >= 2) {
                    int lvl = Integer.parseInt(parts[1]);
                    setVolume(lvl);
                    System.out.println("Volume set to " + lvl);
                }
            } else if (cmd.startsWith("SET_DURATION") || cmd.startsWith("DURATION")) {
                String[] parts = cmd.replace(':', ' ').split("\\s+");
                if (parts.length >= 2) {
                    int sec = Integer.parseInt(parts[1]);
                    setDuration(sec);
                    System.out.println("Duration set to " + sec + " seconds");
                }
            } else if (cmd.equals("STATUS")) {
                System.out.println(getStatus());
            } else {
                System.out.println("Unknown command for AlarmSiren: " + command);
            }
        } catch (InvalidOperationException | NumberFormatException e) {
            System.err.println("Command failed: " + e.getMessage());
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    @Override
    public double getEnergyConsumption() {
        return calculateEnergyConsumption();
    }

    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}