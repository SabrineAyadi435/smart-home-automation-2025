package com.controller;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.devices.SmartDevice;
import com.enums.AirQuality;
import com.enums.SystemStatus;
import com.exceptions.DeviceNotFoundException;
import com.exceptions.InvalidOperationException;
import com.home.Home;
import com.interfaces.EnergyConsumer;
import com.room.Room;

public class HomeController {
    private Home home;
    private SecurityController securityController;
    private Time currentTime;
    private float currentTemperature; //the atmospheric temperature
    private AirQuality airQuality; // outside air quality
    private Date currentDate;

    private boolean isNight;

    // --- Ramadan Mode Fields ---
    private boolean isRamadanModeActive = false; // New flag for Ramadan Mode
    private final Map<String, Time> athanTimes;
    private final Map<String, Time> ramadanAthanTimes; // Specific times for Ramadan
    // ---------------------------
    private static final long ONE_MINUTE_IN_MILLIS = TimeUnit.MINUTES.toMillis(1);

    public HomeController(Home home) {
        this.home = home;
        this.securityController = new SecurityController();

        this.athanTimes = new HashMap<>();
        // Populate dummy Athan times (using hours/minutes for simplicity)
        athanTimes.put("Fajr", Time.valueOf("05:30:00"));
        athanTimes.put("Dhuhr", Time.valueOf("12:30:00"));
        athanTimes.put("Asr", Time.valueOf("16:00:00"));
        athanTimes.put("Maghrib", Time.valueOf("18:15:00"));
        athanTimes.put("Isha", Time.valueOf("20:00:00"));

        // 2. Initialize Ramadan Athan Times
        this.ramadanAthanTimes = new HashMap<>();
        // Fajr (Suhoor ends at Fajr, so notify 30 mins before for Suhoor)
        ramadanAthanTimes.put("Suhoor", Time.valueOf("04:30:00")); 
        // Maghrib (Iftar time)
        ramadanAthanTimes.put("Iftar (Maghrib)", Time.valueOf("18:30:00")); 
        // Isha (Taraweeh starts after Isha, 30 mins buffer)
        ramadanAthanTimes.put("Taraweeh", Time.valueOf("20:45:00")); 
        // We can add the regular prayer times as well, if they are different in Ramadan
        ramadanAthanTimes.put("Fajr", Time.valueOf("05:00:00"));
        ramadanAthanTimes.put("Dhuhr", Time.valueOf("12:45:00"));
        ramadanAthanTimes.put("Asr", Time.valueOf("16:15:00"));
    }

    public void listAllDevices() {
        System.out.println("Devices in " + home.getName() + ":");
        for (SmartDevice device : home.getAllDevices()) {
            System.out.println("  " + device + " ");
            System.out.println(" " +  device.getStatus());
        }
    }

    public void listAllRooms() {
        System.out.println("Rooms in " + home.getName() + ":");
        for (Room room : home.getRooms()) {
            System.out.println("  " + room.getName());
        }
    }

    public void turnOnDevice(String deviceId) {
        try {
            SmartDevice device = home.findDeviceById(deviceId);
            device.turnOn();
        } catch (DeviceNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void turnOffDevice(String deviceId) {
        try {
            SmartDevice device = home.findDeviceById(deviceId);
            device.turnOff();
        } catch (DeviceNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void turnOnAllDevices() {
        for (SmartDevice device : home.getAllDevices()) {
            device.turnOn();
        }
    }

    public void turnOffAllDevices() {
        for (SmartDevice device : home.getAllDevices()) {
            device.turnOff();
        }
    }

    public void displayEnergyConsumption() {
        double totalEnergy = 0.0;
        System.out.println("Energy Consumption Report:");
        for (SmartDevice device : home.getAllDevices()) {
            if (device instanceof EnergyConsumer energyConsumer) {
                double consumption = energyConsumer.getEnergyConsumption();
                System.out.printf("  %s: %.2f kWh\n", device.getName(), consumption);
                totalEnergy += consumption;
            }
        }

        // Add security system energy consumption
        double securityEnergy = securityController.getTotalEnergyConsumption();
        if (securityEnergy > 0) {
            System.out.printf("  Security System: %.2f kWh\n", securityEnergy);
            totalEnergy += securityEnergy;
        }

        System.out.printf("Total Energy Consumption: %.2f kWh\n", totalEnergy);
    }

    // Security System Integration Methods

    public void armSecuritySystem() {
        try {
            securityController.armSystem();
            System.out.println("✓ Security system armed");
        } catch (InvalidOperationException e) {
            System.err.println("Cannot arm system: " + e.getMessage());
        }
    }

    public void disarmSecuritySystem() {
        securityController.disarmSystem();
        System.out.println("✓ Security system disarmed");
    }

    public void setNightMode() {
        securityController.setNightMode();
        System.out.println("✓ Night mode activated");
    }

    public void setAwayMode() {
        securityController.setAwayMode();
        System.out.println("✓ Away mode activated");
    }

    public void addSecurityDevice(SmartDevice device) {
        securityController.addSecurityDevice(device);
    }

    public void handleSecurityEvent(String sensorId) {
        try {
            securityController.handleTriggeredSensor(sensorId);
        } catch (DeviceNotFoundException | InvalidOperationException e) {
            System.err.println("Security event error: " + e.getMessage());
        }
    }

    public void displaySecurityStatus() {
        System.out.println("\n=== Security System Status ===");
        System.out.println("System Status: " + securityController.getSystemStatus());
        System.out.println("Home State: " + securityController.getHomeState());
        System.out.println("Security Devices: " + securityController.getDeviceCount());
        System.out.println("\nRecent Security Log:");
        securityController.getSystemLog(5).forEach(log -> System.out.println("  " + log));
    }

    public void displayAllSecurityDevices() {
        System.out.println("\n=== Security Devices ===");
        securityController.getAllDeviceStatuses().forEach((id, status) -> System.out.println("  " + status));
    }

    public SecurityController getSecurityController() {
        return securityController;
    }

    public SystemStatus getSecurityStatus() {
        return securityController.getSystemStatus();
    }


    // Athan Notification by time

    public Time getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Time currentTime) {
        this.currentTime = currentTime;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public void setSecurityController(SecurityController securityController) {
        this.securityController = securityController;
    }
    

    public void changeAllroomsTemperature(double temperature){
    for (Room room : home.getRooms()) {
            room.setTemperature(temperature);       
        }
        
    }

    public void AthanNotification(){
        if (this.currentTime == null) {
            System.err.println("Athan Notification Error: Current time is not set.");
            return;
        }

        Map<String, Time> currentSchedule;
        String modeDescriptor = "";
        
        if (isRamadanModeActive) {
            currentSchedule = ramadanAthanTimes;
            modeDescriptor = " (Ramadan Schedule)";
        } else {
            currentSchedule = athanTimes;
        }

        long currentTimeMillis = this.currentTime.getTime();

        for (Map.Entry<String, Time> entry : currentSchedule.entrySet()) {
            String athanName = entry.getKey();
            Time athanTime = entry.getValue();

            long athanTimeMillis = athanTime.getTime();

            // Check if current time is within 1 minute of the scheduled time
            if (Math.abs(currentTimeMillis - athanTimeMillis) <= ONE_MINUTE_IN_MILLIS) {
                System.out.println("----------------------------------------------");
                System.out.println("🔔 **Notification: Time for " + athanName + modeDescriptor + "!**");
                System.out.println("Current Time: " + this.currentTime.toString() + " | Scheduled Time: " + athanTime.toString());
                
                // Add specific actions based on the Ramadan event
                if (isRamadanModeActive) {
                    performRamadanAction(athanName);
                }
                
                System.out.println("----------------------------------------------");
            }
        }
    }
    
    /**
     * Helper method to perform specific actions for Ramadan events.
     */
    private void performRamadanAction(String eventName) {
        switch (eventName) {
            case "Suhoor":
                System.out.println("Wake-up lights activated. Preparing the kitchen devices.");
                // Add logic to turn on specific lights or appliances
                break;
            case "Iftar (Maghrib)":
                System.out.println("Iftar time! The fast has been broken. Turning on main room lights.");
                // Add logic to turn on lights, maybe play a specific sound
                break;
            case "Taraweeh":
                System.out.println("Taraweeh prayer starting soon. Setting home to Quiet Mode.");
                // Add logic to set securityController.setQuietMode();
                break;
            default:
                // Standard prayer time action
                break;
        }
    }

    /**
     * Activates the specific settings and notifications for Ramadan.
     */
    public void activateRamadanMode() {
        this.isRamadanModeActive = true;
        System.out.println("🌙 **Ramadan Mode Activated!**");
        // Optionally, add actions like setting lights to a dim/warm color,
        // adjusting HVAC setpoints, etc.
    }

    /**
     * Deactivates the Ramadan Mode, returning to standard operations.
     */
    public void deactivateRamadanMode() {
        this.isRamadanModeActive = false;
        System.out.println("🌙 **Ramadan Mode Deactivated.**");
    }

    public boolean isRamadanModeActive() {
        return isRamadanModeActive;
    }

    public boolean isIsNight() {
        setIsNight();
        return isNight;
    }

    public void setIsNight() {
        if (this.currentTime == null) {
            this.isNight = false; // Cannot determine night without time
            System.err.println("Warning: Current time is not set. Cannot run setIsNight.");
            return;
        }

        // 1. Create a Calendar instance to correctly interpret the Time object
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.currentTime);

        // 2. Extract the hour (24-hour format)
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

        // 3. Apply the intended night logic (20:00 to 05:59)
        // Night: 8 PM (20) and later, OR 5 AM (5) and earlier.
        if (currentHour >= 20 || currentHour < 6) {
            this.isNight = true;
            System.out.println("Current hour (" + currentHour + "): Setting isNight to TRUE.");
        } else {
            this.isNight = false;
            System.out.println("Current hour (" + currentHour + "): Setting isNight to FALSE.");
        }
    }

}