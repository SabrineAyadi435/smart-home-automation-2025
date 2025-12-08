
package com.devices;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.enums.EnergyMode;
import com.exceptions.InvalidOperationException;
import com.exceptions.SecurityBreachException;
import com.interfaces.Controllable;
import com.interfaces.Schedulable;

public class MotionSensor extends SmartDevice implements Controllable, Schedulable {
    private int sensitivity; // 1-10
    private boolean isTriggered;
    private int detectionRange; // meters
    private String schedulePattern;
    private int triggerCount = 0;
    private LocalDateTime lastTriggerTime;
    private boolean isOn = false;
    private java.time.LocalDateTime lastUpdated;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // scheduling support
    private final java.util.Timer scheduler = new java.util.Timer(true);
    private final java.util.List<java.util.TimerTask> scheduledTasks = new java.util.ArrayList<>();
    
    private final double powerConsumption = 10.0;

    public MotionSensor(String deviceId, String name, int detectionRange, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.detectionRange = detectionRange;
        this.sensitivity = 5;
        this.isTriggered = false;
        this.lastUpdated = LocalDateTime.now();
        this.isOn = false;
    }

    // Backwards-compatible constructor (older code created MotionSensor without range)
    public MotionSensor(String deviceId, String name, EnergyMode energyMode) {
        this(deviceId, name, 10, energyMode); // default range 10 meters
    }
    
    @Override
    public void turnOn() {
        this.isOn = true;
        updateTimestamp();
    }
    
    @Override
    public void turnOff() {
        this.isOn = false;
        this.isTriggered = false;
        updateTimestamp();
    }
    
    @Override
    public boolean isOn() {
        return isOn;
    }
    
    public void setSensitivity(int level) throws InvalidOperationException {
        if (level < 1 || level > 10) {
            throw new InvalidOperationException("Sensitivity must be between 1 and 10");
        }
        this.sensitivity = level;
        updateTimestamp();
    }
    
    public boolean checkMotion() throws SecurityBreachException {
        if (!isOn) return false;
        
        // Simulate motion detection - in real system, this would read from hardware
        Random rand = new Random();
        boolean motionDetected = rand.nextInt(100) < (sensitivity * 8);
        
        if (motionDetected) {
            isTriggered = true;
            triggerCount++;
            lastTriggerTime = LocalDateTime.now();
            updateTimestamp();

            logEvent("Motion detected! Sensitivity: " + sensitivity);

            if (isOn) {
                throw new SecurityBreachException(
                    "Motion detected in " + name,
                    "MotionSensor-" + deviceId
                );
            }
        }
        return motionDetected;
    }
    
    public void resetTrigger() {
        this.isTriggered = false;
        updateTimestamp();
    }
    
    @Override
    public String getStatus() {
        return String.format("MotionSensor[%s] - Triggered: %s | Sensitivity: %d | Range: %dm | Triggers: %d",
            name, isTriggered ? "YES" : "NO", sensitivity,
            detectionRange, triggerCount);
    }
    
    public String getDeviceType() {
        return "MOTION_SENSOR";
    }
    
    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
    }
    
    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "No schedule";
    }
    
    public boolean isScheduledActive() {
        // Check if current time matches schedule
        return schedulePattern != null;
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(FORMATTER) +
                          " - MotionSensor[" + deviceId + "]: " + event);
    }

    @Override
    public void executeCommand(String command) {
        if (command == null) return;
        String cmd = command.trim().toUpperCase();
        try {
            if (cmd.equals("ON") || cmd.equals("START")) {
                turnOn();
                System.out.println("MotionSensor turned on");
            } else if (cmd.equals("OFF") || cmd.equals("STOP")) {
                turnOff();
                System.out.println("MotionSensor turned off");
            } else if (cmd.startsWith("SET_SENSITIVITY") || cmd.startsWith("SENSITIVITY")) {
                String[] parts = cmd.replace(':', ' ').split("\\s+");
                if (parts.length >= 2) {
                    int lvl = Integer.parseInt(parts[1]);
                    setSensitivity(lvl);
                    System.out.println("Sensitivity set to " + lvl);
                }
            } else if (cmd.equals("RESET")) {
                resetTrigger();
                System.out.println("Trigger reset");
            } else if (cmd.equals("STATUS")) {
                System.out.println(getStatus());
            } else {
                System.out.println("Unknown command for MotionSensor: " + command);
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

    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    @Override
    public double getEnergyConsumption() {
        if (isOn) {
            return powerConsumption;
        }
        return 0;
    }
    
    public String getLastReading() {
        return isTriggered ? "Motion Detected" : "No Motion";
    }
    
    public LocalDateTime getLastUpdateTime() {
        return lastTriggerTime != null ? lastTriggerTime : lastUpdated;
    }

}
