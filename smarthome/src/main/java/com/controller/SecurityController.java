package com.controller;

import java.time.LocalDateTime;
import java.util.*;

import com.devices.*;
import com.enums.HomeState;
import com.enums.SystemStatus;
import com.exceptions.DeviceNotFoundException;
import com.exceptions.InvalidOperationException;
import com.interfaces.EnergyConsumer;

public class SecurityController {
    private final Map<String, SmartDevice> securityDevices;
    private SystemStatus systemStatus;
    private HomeState homeState;
    private final List<String> systemLog;
    private final AlarmSiren mainAlarm;
    private static final int MAX_LOG_ENTRIES = 1000;

    public SecurityController() {
        this.securityDevices = new HashMap<>();
        this.systemStatus = SystemStatus.DISARMED;
        this.homeState = HomeState.HOME;
        this.systemLog = new ArrayList<>();
        this.mainAlarm = new AlarmSiren("alarm-main", "Main Alarm Siren", com.enums.EnergyMode.NORMAL);
        addSecurityDevice(mainAlarm);
        logSystemEvent("Security system initialized");
    }

    public void armSystem() throws InvalidOperationException {
        // Check if any doors/windows are open before arming
        for (SmartDevice device : securityDevices.values()) {
            if (device instanceof DoorWindowSensor) {
                DoorWindowSensor sensor = (DoorWindowSensor) device;
                if (sensor.isOn() && sensor.checkStatus()) {
                    throw new InvalidOperationException(
                        "Cannot arm system: " + sensor.getName() + " is open");
                }
            }
        }
        systemStatus = SystemStatus.ARMED;
        activateAllSensors();
        logSystemEvent("System ARMED");
    }

    public void disarmSystem() {
        systemStatus = SystemStatus.DISARMED;
        deactivateAllSensors();
        mainAlarm.stopAlarm();
        logSystemEvent("System DISARMED");
    }

    public void setNightMode() {
        systemStatus = SystemStatus.NIGHT_MODE;
        // Activate only certain sensors in night mode
        for (SmartDevice device : securityDevices.values()) {
            if (device instanceof MotionSensor || device instanceof DoorWindowSensor) {
                device.turnOn();
            }
        }
        logSystemEvent("NIGHT MODE activated");
    }

    public void setAwayMode() {
        systemStatus = SystemStatus.AWAY_MODE;
        homeState = HomeState.AWAY;
        activateAllSensors();
        logSystemEvent("AWAY MODE activated - All sensors active");
    }

    public void addSecurityDevice(SmartDevice device) {
        securityDevices.put(device.getDeviceId(), device);
        String deviceType = device.getClass().getSimpleName();
        logSystemEvent("Added device: " + deviceType + " - " + device.getName());
    }

    public void removeSecurityDevice(String deviceId) throws DeviceNotFoundException {
        if (!securityDevices.containsKey(deviceId)) {
            throw new DeviceNotFoundException("Device not found: " + deviceId);
        }
        SmartDevice device = securityDevices.remove(deviceId);
        String deviceType = device.getClass().getSimpleName();
        logSystemEvent("Removed device: " + deviceType + " - " + device.getName());
    }

    public String getDeviceStatus(String deviceId) throws DeviceNotFoundException {
        SmartDevice device = securityDevices.get(deviceId);
        if (device == null) {
            throw new DeviceNotFoundException("Device not found: " + deviceId);
        }
        return device.getStatus();
    }

    public void handleTriggeredSensor(String sensorId) throws DeviceNotFoundException, InvalidOperationException {
        SmartDevice device = securityDevices.get(sensorId);
        if (device == null) {
            throw new DeviceNotFoundException("Sensor not found: " + sensorId);
        }

        // Handle based on device type
        if (device instanceof MotionSensor) {
            handleMotionTrigger((MotionSensor) device);
        } else if (device instanceof DoorWindowSensor) {
            handleDoorWindowTrigger((DoorWindowSensor) device);
        } else if (device instanceof SmokeDetector) {
            handleSmokeTrigger((SmokeDetector) device);
        }
    }

    private void handleMotionTrigger(MotionSensor sensor) throws InvalidOperationException {
        if (systemStatus == SystemStatus.ARMED || systemStatus == SystemStatus.AWAY_MODE) {
            // Intruder Alert - Rule 1
            mainAlarm.triggerAlarm();
            
            // Start recording on all cameras
            for (SmartDevice dev : securityDevices.values()) {
                if (dev instanceof SecurityCamera) {
                    ((SecurityCamera) dev).turnOn();
                }
            }
            
            // Notify owner (in real system, send SMS/Email)
            System.out.println("🚨 INTRUDER ALERT! Motion detected at " + LocalDateTime.now());
            logSystemEvent("INTRUDER ALERT - Motion detected in " + sensor.getName());
        }
    }

    private void handleDoorWindowTrigger(DoorWindowSensor sensor) throws InvalidOperationException {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        
        // Rule: IF doorContact == OPENED AND time BETWEEN 00:00-06:00 AND homeState != DISARMED
        if (hour >= 0 && hour < 6 && systemStatus != SystemStatus.DISARMED) {
            mainAlarm.triggerAlarm();
            // Turn on lights (simulated - would integrate with lighting system)
            System.out.println("💡 Turning on all lights (security breach)");
            logSystemEvent("Night security breach - Door/Window opened");
        }
    }

    private void handleSmokeTrigger(SmokeDetector detector) throws InvalidOperationException {
        // Fire emergency - override all other modes
        mainAlarm.triggerAlarm();
        mainAlarm.setVolume(10); // Maximum volume for fire
        
        // Unlock all doors for evacuation
        for (SmartDevice dev : securityDevices.values()) {
            if (dev instanceof DoorLock) {
                ((DoorLock) dev).turnOff(); // Unlock
            }
        }
        
        // Notify fire department (simulated)
        System.out.println("🔥 FIRE ALERT! Notifying fire department - " + detector.getName());
        logSystemEvent("FIRE ALERT - Smoke detected in " + detector.getName());
    }

    public void processAutomationRules() {
        // Check battery levels periodically
        for (SmartDevice device : securityDevices.values()) {
            if (device instanceof SmokeDetector) {
                ((SmokeDetector) device).checkBattery();
            }
        }
        
        // Auto-lock doors based on schedule
        for (SmartDevice device : securityDevices.values()) {
            if (device instanceof DoorLock && ((DoorLock) device).isScheduledActive()) {
                ((DoorLock) device).lock();
            }
        }
    }

    public Map<String, String> getAllDeviceStatuses() {
        Map<String, String> statuses = new LinkedHashMap<>();
        for (SmartDevice device : securityDevices.values()) {
            statuses.put(device.getDeviceId(), device.getStatus());
        }
        return statuses;
    }

    public double getTotalEnergyConsumption() {
        double total = 0;
        for (SmartDevice device : securityDevices.values()) {
            if (device instanceof EnergyConsumer) {
                total += ((EnergyConsumer) device).getEnergyConsumption();
            }
        }
        return total;
    }

    public List<String> getSystemLog(int count) {
        int start = Math.max(0, systemLog.size() - count);
        return new ArrayList<>(systemLog.subList(start, systemLog.size()));
    }

    private void activateAllSensors() {
        for (SmartDevice device : securityDevices.values()) {
            device.turnOn();
        }
    }

    private void deactivateAllSensors() {
        for (SmartDevice device : securityDevices.values()) {
            if (!(device instanceof SmokeDetector)) {
                // Don't turn off smoke detectors!
                device.turnOff();
            }
        }
    }

    private void logSystemEvent(String event) {
        String logEntry = LocalDateTime.now() + " - " + systemStatus + " - " + event;
        systemLog.add(logEntry);
        
        // Keep log size manageable
        if (systemLog.size() > MAX_LOG_ENTRIES) {
            systemLog.remove(0);
        }
    }

    // Getters
    public SystemStatus getSystemStatus() { return systemStatus; }
    public HomeState getHomeState() { return homeState; }
    public int getDeviceCount() { return securityDevices.size(); }
    
    public void setHomeState(HomeState state) { 
        this.homeState = state; 
        logSystemEvent("Home state changed to: " + state);
    }
}
