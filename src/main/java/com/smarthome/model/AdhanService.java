package com.smarthome.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AdhanService {
    private Map<String, LocalTime> prayerTimes;
    private LocalTime simulatedCurrentTime;
    private boolean timeAccelerationEnabled = false;
    private int accelerationFactor = 1;
    
    public AdhanService(String city, String country) {
        this.prayerTimes = new HashMap<>();
        // Set demo prayer times
        prayerTimes.put("Fajr", LocalTime.of(6, 0));
        prayerTimes.put("Dhuhr", LocalTime.of(12, 0));  
        prayerTimes.put("Asr", LocalTime.of(15, 30));
        prayerTimes.put("Maghrib", LocalTime.of(18, 0));
        prayerTimes.put("Isha", LocalTime.of(19, 30));
        
        this.simulatedCurrentTime = LocalTime.now();
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Simulation Adhan Service started for " + city + ", " + country);
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Initial prayer times:");
        prayerTimes.forEach((prayer, time) -> System.out.println("   " + prayer + ": " + time));
    }
    
    public void setSimulatedTime(LocalTime time) {
        this.simulatedCurrentTime = time;
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Simulated time set to: " + time.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
    
    public void advanceTime(int minutes) {
        this.simulatedCurrentTime = this.simulatedCurrentTime.plusMinutes(minutes);
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Time advanced to: " + 
            simulatedCurrentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
    
    public void enableTimeAcceleration(int factor) {
        this.timeAccelerationEnabled = true;
        this.accelerationFactor = factor;
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Time acceleration enabled: " + factor + "x");
    }
    
    public void updateTime() {
        if (timeAccelerationEnabled) {
            this.simulatedCurrentTime = this.simulatedCurrentTime.plusMinutes(accelerationFactor);
        }
        // Don't update to real time in simulation mode
    }
    
    public boolean isAdhanTime() {
        // Don't call updateTime() here - we want to check the exact time we set
        System.out.println("   Checking Adhan at: " + simulatedCurrentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        
        for (Map.Entry<String, LocalTime> entry : prayerTimes.entrySet()) {
            LocalTime prayerTime = entry.getValue();
            // Check if current simulated time is within 5 minutes of prayer time
            if (simulatedCurrentTime.isAfter(prayerTime.minusSeconds(30)) && 
                simulatedCurrentTime.isBefore(prayerTime.plusMinutes(5))) {
                // CHANGED: Replaced special character with a hyphen
                System.out.println("- Adhan detected for " + entry.getKey() + " at " + prayerTime);
                return true;
            }
        }
        return false;
    }
    
    public String getNextPrayer() {
        String nextPrayer = null;
        LocalTime nextTime = null;
        
        for (Map.Entry<String, LocalTime> entry : prayerTimes.entrySet()) {
            LocalTime prayerTime = entry.getValue();
            if (prayerTime.isAfter(simulatedCurrentTime)) {
                if (nextTime == null || prayerTime.isBefore(nextTime)) {
                    nextTime = prayerTime;
                    nextPrayer = entry.getKey();
                }
            }
        }
        
        return nextPrayer != null ? 
            nextPrayer + " at " + nextTime.format(DateTimeFormatter.ofPattern("HH:mm")) : 
            "All prayers completed for today";
    }
    
    public String getCurrentSimulatedTime() {
        return simulatedCurrentTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    public Map<String, LocalTime> getPrayerTimes() {
        return new HashMap<>(prayerTimes);
    }
    
    // For GUI integration
    public void fastForwardToNextPrayer() {
        String nextPrayer = getNextPrayer();
        if (!nextPrayer.contains("completed")) {
            String[] parts = nextPrayer.split(" at ");
            LocalTime nextTime = LocalTime.parse(parts[1]);
            setSimulatedTime(nextTime.minusMinutes(1)); // Set to 1 minute before Adhan
            // CHANGED: Replaced special character with a hyphen
            System.out.println("- Fast-forwarded to 1 minute before " + parts[0]);
        }
    }
    
    public void setPrayerTime(String prayer, LocalTime time) {
        prayerTimes.put(prayer, time);
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Set " + prayer + " to " + time);
    }
    
    // Debug method to see what's happening
    public void debugPrayerTimes() {
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- DEBUG - Current time: " + getCurrentSimulatedTime());
        prayerTimes.forEach((prayer, time) -> {
            boolean isActive = simulatedCurrentTime.isAfter(time.minusSeconds(30)) && 
                              simulatedCurrentTime.isBefore(time.plusMinutes(5));
            System.out.println("   " + prayer + " at " + time + " - " + (isActive ? "ACTIVE" : "inactive"));
        });
    }
}