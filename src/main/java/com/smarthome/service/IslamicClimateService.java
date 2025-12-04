package com.smarthome.service;

import com.smarthome.model.Thermostat;
import com.smarthome.model.Room;
import com.smarthome.model.Home;
import java.time.LocalTime;
import java.util.*;

public class IslamicClimateService {
    private ClimateService climateService;
    private Home home;
    private boolean prayerTimeAutomation;
    private Map<String, LocalTime> prayerTimes;
    private List<String> prayerRooms;
    
    public IslamicClimateService(ClimateService climateService, Home home) {
        this.climateService = climateService;
        this.home = home;
        this.prayerTimeAutomation = false;
        this.prayerTimes = new HashMap<>();
        this.prayerRooms = new ArrayList<>();
        initializeDefaultPrayerTimes();
    }
    
    private void initializeDefaultPrayerTimes() {
        prayerTimes.put("FAJR", LocalTime.of(5, 30));
        prayerTimes.put("DHUHR", LocalTime.of(12, 30));
        prayerTimes.put("ASR", LocalTime.of(15, 30));
        prayerTimes.put("MAGHRIB", LocalTime.of(18, 30));
        prayerTimes.put("ISHA", LocalTime.of(20, 0));
    }
    
    public void setPrayerTime(String prayerName, LocalTime time) {
        prayerTimes.put(prayerName.toUpperCase(), time);
        System.out.println("🕋 " + prayerName + " prayer time set to " + time);
    }
    
    public void enablePrayerTimeAutomation(boolean enable) {
        this.prayerTimeAutomation = enable;
        if (enable) {
            System.out.println("🕌 Prayer time automation ENABLED");
            System.out.println("Home will prepare for Salah 10 minutes before each prayer");
        } else {
            System.out.println("Prayer time automation DISABLED");
        }
    }
    
    public void prepareForPrayer(String prayerName) {
        System.out.println("🕌 Preparing home for " + prayerName + " prayer...");
        optimizePrayerRoomClimate();
        enhanceAirQualityForPrayer();
        System.out.println("✅ Home ready for " + prayerName + " prayer");
    }
    
    private void optimizePrayerRoomClimate() {
        for (Room room : home.getRooms()) {
            if (room.getClimateZoneType().equals("PRAYER") || 
                room.getName().toLowerCase().contains("prayer")) {
                Thermostat thermostat = room.getThermostat();
                if (thermostat != null && thermostat.isOn()) {
                    thermostat.setTemperature(22.0);
                    thermostat.setMode("AUTO");
                    thermostat.setEcoMode(false);
                    System.out.println("🌡️  Prayer room optimized to comfortable temperature");
                }
            }
        }
    }
    
    private void enhanceAirQualityForPrayer() {
        climateService.getAirQualitySensors().forEach(sensor -> {
            if (sensor.isOn() && sensor.getAirQualityIndex() < 80) {
                System.out.println("💨 Enhancing air purification for prayer comfort");
            }
        });
    }
    
    public void autoPrepareForNextPrayer() {
        LocalTime now = LocalTime.now();
        String nextPrayer = findNextPrayer(now);
        if (nextPrayer != null) {
            LocalTime prayerTime = prayerTimes.get(nextPrayer);
            LocalTime preparationTime = prayerTime.minusMinutes(10);
            if (now.isAfter(preparationTime) && now.isBefore(prayerTime)) {
                prepareForPrayer(nextPrayer);
            }
        }
    }
    
    private String findNextPrayer(LocalTime currentTime) {
        for (Map.Entry<String, LocalTime> entry : prayerTimes.entrySet()) {
            if (currentTime.isBefore(entry.getValue())) {
                return entry.getKey();
            }
        }
        return "FAJR";
    }
    
    public void setPrayerRoom(String roomName) {
        if (!prayerRooms.contains(roomName)) {
            prayerRooms.add(roomName);
            System.out.println("🕌 " + roomName + " designated as prayer room");
        }
    }
    
    public String getIslamicClimateStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== ISLAMIC CLIMATE STATUS ===\n");
        status.append("Prayer Time Automation: ").append(prayerTimeAutomation ? "ENABLED" : "DISABLED").append("\n");
        status.append("Designated Prayer Rooms: ").append(prayerRooms.size()).append("\n");
        status.append("\n--- Prayer Times ---\n");
        prayerTimes.forEach((prayer, time) -> 
            status.append(prayer).append(": ").append(time).append("\n"));
        return status.toString();
    }
    
    public Map<String, LocalTime> getPrayerTimes() {
        return new HashMap<>(prayerTimes);
    }
    
    public boolean isPrayerTimeAutomationEnabled() {
        return prayerTimeAutomation;
    }
}