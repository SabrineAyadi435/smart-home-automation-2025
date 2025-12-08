
package com.devices;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.enums.EnergyMode;
import com.exceptions.InvalidOperationException;
import com.exceptions.SecurityBreachException;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;
import com.interfaces.Schedulable;

public class DoorLock extends SmartDevice implements Controllable, Schedulable, EnergyConsumer {
    private boolean isLocked;
    private int autoLockDelay; // minutes
    private List<String> accessLog;
    private String schedulePattern;
    private boolean isOn;
    private java.time.LocalDateTime lastUpdated;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // scheduling support
    private final java.util.Timer scheduler = new java.util.Timer(true);
    private final java.util.List<java.util.TimerTask> scheduledTasks = new java.util.ArrayList<>();
    // energy consumption
    private boolean energySavingMode = false;
    private double standbyConsumption = 0.02; // watts when idle/locked
    private double activeConsumption = 1.0; // watts when operating/unlocked
    
    public DoorLock(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.isLocked = true; // Default to locked for security
        this.autoLockDelay = 5; // 5 minutes default
        this.accessLog = new ArrayList<>();
        this.lastUpdated = java.time.LocalDateTime.now();
        this.isOn = false;
    }
    
    @Override
    public void turnOn() {
        lock();
    }
    
    @Override
    public void turnOff() {
        unlock();
    }
    
    @Override
    public boolean isOn() {
        return isOn;
    }
    
    public void lock() {
        if (!isLocked) {
            isLocked = true;
            updateTimestamp();
            logAccess("Door locked manually");
        }
    }
    
    public void unlock() throws SecurityBreachException {
        if (isLocked) {
            isLocked = false;
            updateTimestamp();
            logAccess("Door unlocked");
            
            // If system is armed and door is unlocked, might be a breach
            if (isOn) {
                logEvent("Door unlocked while system active - potential breach!");
            }
        }
    }
    
    public void toggleLock() throws SecurityBreachException {
        if (isLocked) {
            unlock();
        } else {
            lock();
        }
    }
    
    public void setAutoLockDelay(int minutes) throws InvalidOperationException {
        if (minutes < 0 || minutes > 60) {
            throw new InvalidOperationException("Auto-lock delay must be between 0 and 60 minutes");
        }
        this.autoLockDelay = minutes;
        updateTimestamp();
    }
    
    public List<String> getAccessLog() {
        return new ArrayList<>(accessLog);
    }
    
    public List<String> getRecentAccessLog(int count) {
        int start = Math.max(0, accessLog.size() - count);
        return new ArrayList<>(accessLog.subList(start, accessLog.size()));
    }
    
    private void logAccess(String action) {
        String entry = LocalDateTime.now().format(FORMATTER) + " - " + action;
        accessLog.add(entry);
        if (accessLog.size() > 1000) { // Keep only last 1000 entries
            accessLog.remove(0);
        }
    }
    
    @Override
    public String getStatus() {
        return String.format("DoorLock[%s] - Locked: %s | Auto-lock: %d min | Log Entries: %d",
                name, isLocked ? "YES" : "NO", autoLockDelay, accessLog.size());
    }
    
    public String getDeviceType() {
        return "DOOR_LOCK";
    }
    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
        logEvent("Schedule set: " + schedulePattern);
    }
    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "No schedule";
    }
    public boolean isScheduledActive() {
        // Check if auto-lock should be active based on time
        if (schedulePattern != null && schedulePattern.contains("auto-lock")) {
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            return hour >= 23 || hour < 6; // Auto-lock at night
        }
        return false;
    }

    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + 
                          " - DoorLock[" + deviceId + "]: " + event);
    }

    @Override
    public void executeCommand(String command) {
        if (command == null) return;
        String cmd = command.trim().toUpperCase();
        try {
            if (cmd.equals("LOCK") || cmd.equals("ON")) {
                lock();
                System.out.println("Door locked");
            } else if (cmd.equals("UNLOCK") || cmd.equals("OFF")) {
                unlock();
                System.out.println("Door unlocked");
            } else if (cmd.equals("TOGGLE")) {
                toggleLock();
                System.out.println("Toggled lock");
            } else if (cmd.startsWith("SET_AUTOLOCK") || cmd.startsWith("AUTOLOCK")) {
                String[] parts = cmd.replace(':', ' ').split("\\s+");
                if (parts.length >= 2) {
                    int minutes = Integer.parseInt(parts[1]);
                    setAutoLockDelay(minutes);
                    System.out.println("Auto-lock delay set to " + minutes + " minutes");
                }
            } else if (cmd.equals("STATUS")) {
                System.out.println(getStatus());
            } else {
                System.out.println("Unknown command for DoorLock: " + command);
            }
        } catch (SecurityBreachException | InvalidOperationException | NumberFormatException e) {
            System.err.println("Command failed: " + e.getMessage());
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    @Override
    public void scheduleTask(LocalTime time, Runnable task) {
        // Schedule the task to run at the next occurrence of the provided LocalTime
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime runAt = now.withHour(time.getHour()).withMinute(time.getMinute()).withSecond(0).withNano(0);
        if (runAt.isBefore(now)) {
            runAt = runAt.plusDays(1);
        }
        Date runDate = java.util.Date.from(runAt.atZone(java.time.ZoneId.systemDefault()).toInstant());
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
        double base = isLocked ? standbyConsumption : activeConsumption;
        return energySavingMode ? base * 0.8 : base;
    }

    @Override
    public void setEnergyMode(com.enums.EnergyMode mode) {
        super.setEnergyMode(mode);
        this.energySavingMode = (mode == com.enums.EnergyMode.ECO);
        // adjust behavior in ECO mode if desired (e.g., increase auto-lock delay)
        if (this.energySavingMode && this.autoLockDelay < 1) {
            this.autoLockDelay = 1;
        }
        logEvent("Energy mode set to " + mode + " (energySaving=" + energySavingMode + ")");
    }
}
