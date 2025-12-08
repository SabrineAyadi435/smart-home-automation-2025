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
    private float currentTemperature; // the atmospheric temperature
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
            System.out.println(" " + device.getStatus());
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

    public Home getHome() {
        return home;
    }

    public void changeAllroomsTemperature(double temperature) {
        for (Room room : home.getRooms()) {
            room.setTemperature(temperature);
        }

    }

    // Track notified events to prevent spamming
    private java.util.Set<String> notifiedEvents = new java.util.HashSet<>();
    private int lastDayOfYear = -1;

    /**
     * Checks for scheduled events based on the simulated time.
     * Should be called periodically (e.g. every tick or every second).
     */
    public void checkScheduledEvents(java.time.LocalDateTime simulatedTime) {
        // Update internal current time
        this.currentTime = Time.valueOf(simulatedTime.toLocalTime());
        this.currentDate = java.sql.Date.valueOf(simulatedTime.toLocalDate());

        // Check if day changed to reset notified events
        int currentDay = simulatedTime.getDayOfYear();
        if (currentDay != lastDayOfYear) {
            notifiedEvents.clear();
            lastDayOfYear = currentDay;
            System.out.println("New day: " + simulatedTime.toLocalDate() + ". Resetting scheduled events.");
        }

        // Check Athan times
        AthanNotification();

        // Update Night Mode
        setIsNight();
    }

    public void AthanNotification() {
        if (this.currentTime == null) {
            return;
        }

        Map<String, Time> currentSchedule;
        String modeDescriptor = "";

        if (isRamadanModeActive) {
            currentSchedule = ramadanAthanTimes;
            modeDescriptor = " (Ramadan)";
        } else {
            currentSchedule = athanTimes;
        }

        // long currentTimeMillis = this.currentTime.getTime(); // Original line, no
        // longer needed
        java.time.LocalTime currentLocalTime = this.currentTime.toLocalTime();

        for (Map.Entry<String, Time> entry : currentSchedule.entrySet()) {
            String athanName = entry.getKey();
            Time athanTime = entry.getValue();
            java.time.LocalTime scheduledLocalTime = athanTime.toLocalTime();

            // Create a unique key for this event today
            String eventKey = athanName + "-" + lastDayOfYear;

            if (notifiedEvents.contains(eventKey)) {
                continue; // Already notified today
            }

            // long athanTimeMillis = athanTime.getTime(); // Original line, no longer
            // needed

            // Check if current time is AFTER or EQUAL to scheduled time
            // And within a reasonable window (e.g. 15 minutes) to ensure we don't miss it
            // at high speeds
            // but also don't trigger it hours late if we just started the app
            if ((currentLocalTime.equals(scheduledLocalTime) || currentLocalTime.isAfter(scheduledLocalTime)) &&
                    currentLocalTime.isBefore(scheduledLocalTime.plusMinutes(15))) {

                String message = "Time for " + athanName + modeDescriptor + "!";
                String detail = "It is now "
                        + currentLocalTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

                // Send Toast Notification
                com.ui.utils.NotificationManager.getInstance().addNotification(
                        "Prayer Time",
                        message + "\n" + detail,
                        com.ui.models.NotificationType.INFO);

                System.out.println("----------------------------------------------");
                System.out.println("🔔 **Notification: " + message + "**");
                System.out.println(
                        "Current Time: " + this.currentTime.toString() + " | Scheduled: " + athanTime.toString());

                // Add specific actions based on the Ramadan event
                if (isRamadanModeActive) {
                    performRamadanAction(athanName);
                }

                System.out.println("----------------------------------------------");

                // Mark as notified
                notifiedEvents.add(eventKey);
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
                com.ui.utils.NotificationManager.getInstance().addNotification(
                        "Ramadan Routine",
                        "Suhoor time! Wake-up lights activated.",
                        com.ui.models.NotificationType.INFO);
                break;
            case "Iftar (Maghrib)":
                System.out.println("Iftar time! The fast has been broken. Turning on main room lights.");
                com.ui.utils.NotificationManager.getInstance().addNotification(
                        "Ramadan Routine",
                        "Iftar time! Turning on lights.",
                        com.ui.models.NotificationType.INFO);
                break;
            case "Taraweeh":
                System.out.println("Taraweeh prayer starting soon. Setting home to Quiet Mode.");
                com.ui.utils.NotificationManager.getInstance().addNotification(
                        "Ramadan Routine",
                        "Taraweeh starting. Quiet Mode set.",
                        com.ui.models.NotificationType.INFO);
                break;
            default:
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

    /**
     * Updates energy and water consumption based on elapsed simulated time.
     * 
     * @param simulatedSecondsElapsed The number of simulated seconds that have
     *                                passed.
     */
    public void updateConsumption(double simulatedSecondsElapsed) {
        if (home == null)
            return;

        for (Room room : home.getRooms()) {
            // 1. Update Energy Consumption
            room.accumulateEnergyUsageSeconds((long) simulatedSecondsElapsed);

            // 2. Update Water Consumption
            double roomTotalWater = 0;
            double roomCurrentWater = 0;

            for (SmartDevice device : room.getDevices()) {
                if (device instanceof com.devices.SmartFaucet) {
                    com.devices.SmartFaucet faucet = (com.devices.SmartFaucet) device;
                    // Track consumption for this time step (convert seconds to minutes)
                    faucet.trackWaterConsumption(simulatedSecondsElapsed / 60.0);

                    roomTotalWater += faucet.getTotalwaterConsumption();
                    roomCurrentWater += faucet.getCurrentwaterConsumption();
                }
            }

            // Update room water stats
            // Only update if we found water consumers, otherwise keep existing (or 0)
            // Actually, we should set it to the calculated sum to be accurate
            if (room.getName().equals("Bathroom") || room.getName().equals("Kitchen")) {
                room.setTotalwaterConsumption(roomTotalWater);
                room.setCurrentwaterConsumption(roomCurrentWater);
            }
        }

        // Update Home totals
        home.setTotalEnergyConsumption();
        home.setCurrentEnergyConsumption();
        home.setTotalwaterConsumption();
        home.setCurrentwaterConsumption();
    }

}