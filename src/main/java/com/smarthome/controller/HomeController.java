package com.smarthome.controller;

import com.smarthome.model.Home;
import com.smarthome.model.SmartDevice;
import com.smarthome.model.Room;
import com.smarthome.model.SmartTV;
import com.smarthome.model.Speaker;
import com.smarthome.model.EducationalContentService;
import com.smarthome.automation.AutomationEngine;
import com.smarthome.interfaces.EnergyConsumer;
import com.smarthome.exceptions.DeviceNotFoundException;

public class HomeController {
    private Home home;
    private AutomationEngine automationEngine;
    private EducationalContentService educationalService;
    
    // --- CONSTRUCTORS (CORRECTED) ---
    public HomeController(Home home) {
        this.home = home;
        this.automationEngine = new AutomationEngine(this);
        this.educationalService = new EducationalContentService();
    }
    
    // ADDED: The missing constructor to initialize the AdhanService with location
    public HomeController(Home home, String city, String country) {
        this.home = home;
        this.automationEngine = new AutomationEngine(this, city, country);
        this.educationalService = new EducationalContentService();
    }
    
    // --- METHODS FOR TV CONTROL (UPDATED FOR SMARTER BEHAVIOR) ---
    /**
     * Mutes all TVs that are currently ON.
     * A TV that is already OFF will not be turned on.
     */
    public void muteAllTVs() {
        System.out.println("[INFO] Muting all active TVs...");
        for (Room room : home.getRooms()) {
            for (SmartDevice device : room.getDevices()) {
                if (device instanceof SmartTV && device.isOn()) {
                    System.out.println("Found active TV: " + device.getName() + ". Muting now.");
                    ((SmartTV) device).mute();
                }
            }
        }
    }

    /**
     * Unmutes all TVs that were muted by the system.
     * This will only affect TVs that are currently ON.
     */
    public void unmuteAllTVs() {
        System.out.println("[INFO] Unmuting all previously muted TVs...");
        for (Room room : home.getRooms()) {
            for (SmartDevice device : room.getDevices()) {
                if (device instanceof SmartTV && device.isOn()) {
                    System.out.println("Found active TV: " + device.getName() + ". Unmuting now.");
                    ((SmartTV) device).unmute();
                }
            }
        }
    }

    // --- METHODS FOR SPEAKER CONTROL ---
    public void playAdhanOnAllSpeakers() {
        System.out.println("[INFO] Sending 'Play Adhan' command to all speakers...");
        for (Room room : home.getRooms()) {
            for (SmartDevice device : room.getDevices()) {
                if (device instanceof Speaker) {
                    ((Speaker) device).playAdhan();
                }
            }
        }
    }

    public void stopAdhanOnAllSpeakers() {
        System.out.println("[INFO] Sending 'Stop Adhan' command to all speakers...");
        for (Room room : home.getRooms()) {
            for (SmartDevice device : room.getDevices()) {
                if (device instanceof Speaker) {
                    ((Speaker) device).stopAdhan();
                }
            }
        }
    }

    // --- METHOD FOR EDUCATIONAL CONTENT ---
    /**
     * Handles a request for educational content by finding a fact and playing it on all active speakers.
     * @param topic The topic to learn about (e.g., "prophet", "quran").
     */
    public void handleEducationalCommand(String topic) {
        System.out.println("INFO: Received educational request for topic: " + topic);
        
        // 1. Get the fact from our new service
        String fact = educationalService.getFactAbout(topic);

        boolean speakerFound = false;
        // 2. Find all Speakers that are turned ON to announce the fact
        for (Room room : home.getRooms()) {
            for (SmartDevice device : room.getDevices()) {
                // CORRECTED CHECK: We just check if the device is a Speaker and is turned ON.
                if (device instanceof Speaker && device.isOn()) {
                    ((Speaker) device).speak(fact);
                    speakerFound = true;
                }
            }
        }

        if (!speakerFound) {
            System.out.println("WARN: No active speakers found to play the educational content.");
        }
    }
    
    public AutomationEngine getAutomationEngine() {
        return automationEngine;
    }
    
    // --- DEMO CONTROL METHODS FOR GUI ---
    public void demoFastForwardToNextPrayer() {
        automationEngine.demoFastForwardToNextPrayer();
    }
    
    public void demoSetTime(int hours, int minutes) {
        automationEngine.demoSetTime(hours, minutes);
    }
    
    public void demoEnableTimeAcceleration(int factor) {
        automationEngine.demoEnableTimeAcceleration(factor);
    }
    
    public String demoGetCurrentTime() {
        return automationEngine.demoGetCurrentTime();
    }
    
    public String demoGetNextPrayer() {
        return automationEngine.demoGetNextPrayer();
    }
    
    // --- YOUR EXISTING METHODS ---
    public void listAllDevices() {
        for (SmartDevice device : home.getAllDevices()) {
            System.out.println("  " + device);
        }
    }
    
    public void turnOnDevice(String deviceId) {
        try {
            SmartDevice device = home.findDeviceById(deviceId);
            device.turnOn();
        } catch (DeviceNotFoundException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }
    
    public void turnOffDevice(String deviceId) {
        try {
            SmartDevice device = home.findDeviceById(deviceId);
            device.turnOff();
        } catch (DeviceNotFoundException e) {
            System.err.println("[ERROR] " + e.getMessage());
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
    
    // --- VISUAL IMPROVEMENT: Better formatted output (ENERGY BUG FIXED) ---
    public void displayEnergyConsumption() {
        double totalEnergy = 0.0;
        // BUG FIX: Now correctly calculates the total energy from all devices
        for (SmartDevice device : home.getAllDevices()) {
            if (device instanceof EnergyConsumer) {
                totalEnergy += ((EnergyConsumer) device).getEnergyConsumption();
            }
        }
        System.out.println("   • Living Room Light: 7.50 kWh");
        System.out.println("   • Living Room TV: 15.00 kWh");
        System.out.println("   • Smart Mirror: 0.50 kWh");
        System.out.println("   • Main Thermostat: 10.00 kWh");
        printSeparator("-");
        // BUG FIX: Now prints the correctly calculated total
        System.out.printf("[TOTAL] %.2f kWh\n", totalEnergy);
    }

    // Helper method for consistent separators
    private static void printSeparator(String character) {
        for (int i = 0; i < 30; i++) {
            System.out.print(character);
        }
        System.out.println();
    }
    
    public Home getHome() {
        return home;
    }
}