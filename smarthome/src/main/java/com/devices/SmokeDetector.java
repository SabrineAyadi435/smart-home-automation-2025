
package com.devices;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.enums.EnergyMode;
import com.exceptions.InvalidOperationException;
import com.exceptions.SecurityBreachException;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;
import com.interfaces.Schedulable;

public class SmokeDetector extends SmartDevice implements Controllable, Schedulable, EnergyConsumer {
    private int sensitivity; // 1-10
    private boolean smokeDetected;
    private double batteryLevel;
    private String location;
    private double baseConsumption = 0.2;
    private double alarmConsumption = 2.0;
    private String schedulePattern;
    private LocalDateTime lastBatteryCheck;
    private boolean isOn;
    private java.time.LocalDateTime lastUpdated;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // scheduling support
    private final java.util.Timer scheduler = new java.util.Timer(true);
    private final java.util.List<java.util.TimerTask> scheduledTasks = new java.util.ArrayList<>();
    
    public SmokeDetector(String deviceId, String name, String location, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.location = location;
        this.sensitivity = 7;
        this.smokeDetected = false;
        this.batteryLevel = 100.0;
        this.lastBatteryCheck = LocalDateTime.now();
        this.isOn = false;
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        updateTimestamp();
    }
    
    public void turnOff() {
        if (smokeDetected) {
            System.err.println("Cannot turn off while smoke is detected!");
            return;
        }
        this.isOn = false;
        updateTimestamp();
    }
    
    @Override
    public boolean isOn() {
        return isOn;
    }
    
    public void detectSmoke(boolean smokePresent) throws SecurityBreachException {
        if (!isOn) return;
        
        if (smokePresent && !smokeDetected) {
            smokeDetected = true;
            updateTimestamp();
            triggerSmokeAlarm();
            
            throw new SecurityBreachException(
                "Smoke detected in " + location,
                location
            );
        } else if (!smokePresent && smokeDetected) {
            smokeDetected = false;
            updateTimestamp();
            logEvent("Smoke cleared");
        }
    }
    
    public void triggerSmokeAlarm() {
        System.out.println("🔥 SMOKE DETECTED in " + location + "!");
        logEvent("Smoke alarm triggered");
        // In real implementation, connect to fire department and sound alarm
    }
    
    public void checkBattery() {
        LocalDateTime now = LocalDateTime.now();
        if (lastBatteryCheck.plusDays(1).isBefore(now)) {
            batteryLevel -= 0.1; // Simulate daily battery drain
            lastBatteryCheck = now;
            
            if (batteryLevel < 20) {
                System.out.println("⚠️ Low battery in " + name + " (" + batteryLevel + "%)");
            }
        }
    }
    
    public double calculateEnergyConsumption() {
        double consumption = smokeDetected ? alarmConsumption : baseConsumption;
        return energyMode == EnergyMode.ECO ? consumption * 0.6 : energyMode == EnergyMode.HIGH ? consumption * 2 : consumption;
    }
    
    public double getEnergyConsumptionRate() {
        return calculateEnergyConsumption();
    }
    
    
    @Override
    public String getStatus() {
        checkBattery();
        return String.format("SmokeDetector[%s] - Location: %s | Smoke: %s | Sensitivity: %d | Battery: %.1f%%",
                name, location, smokeDetected ? "DETECTED" : "CLEAR", 
                sensitivity, batteryLevel);
    }
    
    public String getDeviceType() {
        return "SMOKE_DETECTOR";
    }
    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
    }
    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "24/7 monitoring";
    }
    public boolean isScheduledActive() {
        return true; // Smoke detectors should always be active
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(FORMATTER) +
                          " - SmokeDetector[" + deviceId + "]: " + event);
    }

    @Override
    public void executeCommand(String command) {
        if (command == null) return;
        String cmd = command.trim().toUpperCase();
        try {
            if (cmd.equals("ON") || cmd.equals("START")) {
                turnOn();
                System.out.println("SmokeDetector turned on");
            } else if (cmd.equals("OFF") || cmd.equals("STOP")) {
                turnOff();
                System.out.println("SmokeDetector turned off");
            } else if (cmd.startsWith("SET_SENSITIVITY") || cmd.startsWith("SENSITIVITY")) {
                String[] parts = cmd.replace(':', ' ').split("\\s+");
                if (parts.length >= 2) {
                    int lvl = Integer.parseInt(parts[1]);
                    setSensitivity(lvl);
                    System.out.println("Sensitivity set to " + lvl);
                }
            } else if (cmd.equals("STATUS")) {
                System.out.println(getStatus());
            } else {
                System.out.println("Unknown command for SmokeDetector: " + command);
            }
        } catch (InvalidOperationException | NumberFormatException e) {
            System.err.println("Command failed: " + e.getMessage());
        }
    }

    public void setSensitivity(int level) throws InvalidOperationException {
        if (level < 1 || level > 10) {
            throw new InvalidOperationException("Sensitivity must be between 1 and 10");
        }
        this.sensitivity = level;
        updateTimestamp();
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    @Override
    public void scheduleTask(LocalTime time, Runnable task) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime runAt = now.withHour(time.getHour()).withMinute(time.getMinute()).withSecond(0).withNano(0);
        if (runAt.isBefore(now)) {
            runAt = runAt.plusDays(1);
        }
        java.util.Date runDate = java.util.Date.from(runAt.atZone(java.time.ZoneId.systemDefault()).toInstant());
        java.util.TimerTask timerTask = new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    System.err.println("Scheduled task error: " + e.getMessage());
                }
            }
        };
        scheduledTasks.add(timerTask);
        scheduler.schedule(timerTask, runDate);
        logEvent("Scheduled task at " + time.toString());
    }

    @Override
    public void cancelScheduledTasks() {
        for (java.util.TimerTask t : scheduledTasks) {
            t.cancel();
        }
        scheduledTasks.clear();
        logEvent("All scheduled tasks cancelled");
    }

    @Override
    public double getEnergyConsumption() {
        return calculateEnergyConsumption();
    }

    @Override
    public void setEnergyMode(EnergyMode mode) {
        super.setEnergyMode(mode);
        this.energyMode = mode;
        logEvent("Energy mode set to " + mode);

        
    }

    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getLastReading() {
        return smokeDetected ? "Smoke Detected" : "Clear";
    }
    
    public LocalDateTime getLastUpdateTime() {
        return lastUpdated;
    }
    
    public double getBatteryLevel() {
        return batteryLevel;
    }
}