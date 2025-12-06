
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class MotionSensor extends SmartDevice implements Controllable, Schedulable {
    private int sensitivity; // 1-10
    private boolean isTriggered;
    private int detectionRange; // meters
    private String schedulePattern;
    private int triggerCount = 0;
    private LocalDateTime lastTriggerTime;
    
    public MotionSensor(String deviceId, String name, int detectionRange) {
        super(deviceId, name);
        this.detectionRange = detectionRange;
        this.sensitivity = 5;
        this.isTriggered = false;
    }
    
    @Override
    public void turnOn() {
        this.isActive = true;
        updateTimestamp();
    }
    
    @Override
    public void turnOff() {
        this.isActive = false;
        this.isTriggered = false;
        updateTimestamp();
    }
    
    @Override
    public boolean isOn() {
        return isActive;
    }
    
    public void setSensitivity(int level) throws InvalidOperationException {
        if (level < 1 || level > 10) {
            throw new InvalidOperationException("Sensitivity must be between 1 and 10");
        }
        this.sensitivity = level;
        updateTimestamp();
    }
    
    public boolean checkMotion() throws SecurityBreachException {
        if (!isActive) return false;
        
        // Simulate motion detection - in real system, this would read from hardware
        Random rand = new Random();
        boolean motionDetected = rand.nextInt(100) < (sensitivity * 8);
        
        if (motionDetected) {
            isTriggered = true;
            triggerCount++;
            lastTriggerTime = LocalDateTime.now();
            updateTimestamp();
            
            logEvent("Motion detected! Sensitivity: " + sensitivity);
            
            if (isActive) {
                throw new SecurityBreachException(
                    "Motion detected in " + deviceName, 
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
                deviceName, isTriggered ? "YES" : "NO", sensitivity, 
                detectionRange, triggerCount);
    }
    
    @Override
    public String getDeviceType() {
        return "MOTION_SENSOR";
    }
    
    @Override
    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
    }
    
    @Override
    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "No schedule";
    }
    
    @Override
    public boolean isScheduledActive() {
        // Check if current time matches schedule
        return schedulePattern != null;
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(formatter) + 
                          " - MotionSensor[" + deviceId + "]: " + event);
    }
}

