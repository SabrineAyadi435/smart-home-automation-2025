
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DoorLock extends SmartDevice implements Controllable, Schedulable {
    private boolean isLocked;
    private int autoLockDelay; // minutes
    private List<String> accessLog;
    private String schedulePattern;
    
    public DoorLock(String deviceId, String name) {
        super(deviceId, name);
        this.isLocked = true; // Default to locked for security
        this.autoLockDelay = 5; // 5 minutes default
        this.accessLog = new ArrayList<>();
        logAccess("System initialized - Door locked");
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
        return isLocked;
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
            if (isActive) {
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
        String entry = LocalDateTime.now().format(formatter) + " - " + action;
        accessLog.add(entry);
        if (accessLog.size() > 1000) { // Keep only last 1000 entries
            accessLog.remove(0);
        }
    }
    
    @Override
    public String getStatus() {
        return String.format("DoorLock[%s] - Locked: %s | Auto-lock: %d min | Log Entries: %d",
                deviceName, isLocked ? "YES" : "NO", autoLockDelay, accessLog.size());
    }
    
    @Override
    public String getDeviceType() {
        return "DOOR_LOCK";
    }
    
    @Override
    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
        logEvent("Schedule set: " + schedulePattern);
    }
    
    @Override
    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "No schedule";
    }
    
    @Override
    public boolean isScheduledActive() {
        // Check if auto-lock should be active based on time
        if (schedulePattern != null && schedulePattern.contains("auto-lock")) {
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();
            return hour >= 23 || hour < 6; // Auto-lock at night
        }
        return false;
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(formatter) + 
                          " - DoorLock[" + deviceId + "]: " + event);
    }
}

