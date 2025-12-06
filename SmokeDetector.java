
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
class SmokeDetector extends SmartDevice implements Controllable, Schedulable, EnergyConsumer {
    private int sensitivity; // 1-10
    private boolean smokeDetected;
    private double batteryLevel;
    private String location;
    private double baseConsumption = 0.2;
    private double alarmConsumption = 2.0;
    private String schedulePattern;
    private boolean energySavingMode = false;
    private LocalDateTime lastBatteryCheck;
    
    public SmokeDetector(String deviceId, String name, String location) {
        super(deviceId, name);
        this.location = location;
        this.sensitivity = 7;
        this.smokeDetected = false;
        this.batteryLevel = 100.0;
        this.lastBatteryCheck = LocalDateTime.now();
    }
    
    @Override
    public void turnOn() {
        this.isActive = true;
        updateTimestamp();
    }
    
    @Override
    public void turnOff() throws InvalidOperationException {
        if (smokeDetected) {
            throw new InvalidOperationException("Cannot turn off while smoke is detected!");
        }
        this.isActive = false;
        updateTimestamp();
    }
    
    @Override
    public boolean isOn() {
        return isActive;
    }
    
    public void detectSmoke(boolean smokePresent) throws SecurityBreachException {
        if (!isActive) return;
        
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
                System.out.println("⚠️ Low battery in " + deviceName + " (" + batteryLevel + "%)");
            }
        }
    }
    
    @Override
    public double calculateEnergyConsumption() {
        double consumption = smokeDetected ? alarmConsumption : baseConsumption;
        return energySavingMode ? consumption * 0.6 : consumption;
    }
    
    @Override
    public double getEnergyConsumptionRate() {
        return calculateEnergyConsumption();
    }
    
    @Override
    public void setEnergySavingMode(boolean enable) {
        this.energySavingMode = enable;
        if (enable && sensitivity > 5) {
            sensitivity = 5; // Reduce sensitivity in energy saving mode
        }
    }
    
    @Override
    public String getStatus() {
        checkBattery();
        return String.format("SmokeDetector[%s] - Location: %s | Smoke: %s | Sensitivity: %d | Battery: %.1f%%",
                deviceName, location, smokeDetected ? "DETECTED" : "CLEAR", 
                sensitivity, batteryLevel);
    }
    
    @Override
    public String getDeviceType() {
        return "SMOKE_DETECTOR";
    }
    
    @Override
    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
    }
    
    @Override
    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "24/7 monitoring";
    }
    
    @Override
    public boolean isScheduledActive() {
        return true; // Smoke detectors should always be active
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(formatter) + 
                          " - SmokeDetector[" + deviceId + "]: " + event);
    }
}
