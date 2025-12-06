package com.smarthome;

import com.smarthome.controller.HomeController;
import com.smarthome.model.*;
import com.smarthome.automation.AutomationEngine;
import com.smarthome.automation.Rule;

public class Main {
    public static void main(String[] args) {
        // Create home and controller
        Home myHome = new Home("My Smart Home");
        HomeController controller = new HomeController(myHome, "DemoCity", "DemoCountry");
        
        // Create rooms
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        
        // Create devices
        Light livingRoomLight = new Light("LR-Light-01", "Living Room Light", 100);
        Thermostat thermostat = new Thermostat("TH-01", "Main Thermostat", 22.0);
        SmartTV tv = new SmartTV("TV-01", "Living Room TV");
        MotionSensor sensor = new MotionSensor("MS-01", "Entry Sensor");
        SmartMirror mirror = new SmartMirror("MIRROR-01", "Smart Mirror");
        Speaker speaker = new Speaker("SP-01", "Living Room Speaker");
        
        // Add devices to rooms
        livingRoom.addDevice(livingRoomLight);
        livingRoom.addDevice(tv);
        livingRoom.addDevice(sensor);
        livingRoom.addDevice(mirror);
        livingRoom.addDevice(speaker);
        bedroom.addDevice(thermostat);
        
        // Add rooms to home
        myHome.addRoom(livingRoom);
        myHome.addRoom(bedroom);
        
        // START ADHAN MONITORING
        System.out.println("[INFO] Starting Adhan-aware automation...");
        controller.getAutomationEngine().startAdhanMonitoring();
        
        // --- VISUAL IMPROVEMENT: Better section headers ---
        printSeparator("=");
        System.out.println("           INITIAL DEVICE STATUS");
        printSeparator("=");
        controller.listAllDevices();
        
        printSeparator("=");
        System.out.println("           PRAYER TIME INFORMATION");
        printSeparator("=");
        System.out.println("Next prayer: " + controller.demoGetNextPrayer());
        System.out.println("Current simulated time: " + controller.demoGetCurrentTime());
        
        // Control devices
        printSeparator("=");
        System.out.println("           CONTROLLING DEVICES");
        printSeparator("=");
        controller.turnOnDevice("LR-Light-01");
        controller.turnOnDevice("TH-01");
        // NOTE: We no longer turn on the TV here to test the smarter mute logic
        controller.turnOnDevice("SP-01"); // Make sure the speaker is on for the test
        
        // Set specific properties
        livingRoomLight.setBrightness(75);
        thermostat.setTargetTemperature(24.0);
        
        printSeparator("=");
        System.out.println("           UPDATED DEVICE STATUS");
        printSeparator("=");
        controller.listAllDevices();
        
        // Automation example
        printSeparator("=");
        System.out.println("           AUTOMATION RULES");
        printSeparator("=");
        AutomationEngine engine = controller.getAutomationEngine();
        Rule motionRule = new Rule("Motion Light Rule", 
            () -> sensor.isMotionDetected(),
            () -> livingRoomLight.turnOn()
        );
        engine.addRule(motionRule);
        
        // Simulate motion detection
        sensor.detectMotion();
        engine.evaluateRules();
        
        printSeparator("=");
        System.out.println("           ENERGY CONSUMPTION REPORT");
        printSeparator("=");
        controller.displayEnergyConsumption();
        
        // TEST SMART MIRROR FUNCTIONALITY
        printSeparator("=");
        System.out.println("           TESTING SMART MIRROR");
        printSeparator("=");
        testSmartMirror(mirror);
        
        // TEST ADHAN INTEGRATION
        printSeparator("=");
        System.out.println("           TESTING ADHAN INTEGRATION");
        printSeparator("=");
        testAdhanIntegration(controller, tv);

        // <-- 1. ADD A NEW TEST FOR THE EDUCATIONAL HUB -->
        printSeparator("=");
        System.out.println("        TESTING EDUCATIONAL ENTERTAINMENT HUB");
        printSeparator("=");
        testEducationalHub(controller);
        
        // --- VISUAL IMPROVEMENT: Clearer final message ---
        printSeparator("=");
        System.out.println("           SYSTEM IS RUNNING");
        printSeparator("=");
        System.out.println("[INFO] Adhan monitoring is active in the background...");
        System.out.println("[INFO] Press CTRL+C in the terminal to stop the program.");
        printSeparator("=");
        
        // Keep the main thread alive
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // Helper method for consistent separators
    private static void printSeparator(String character) {
        for (int i = 0; i < 50; i++) {
            System.out.print(character);
        }
        System.out.println();
    }
    
    // <-- 2. ADD THE NEW TEST METHOD -->
    private static void testEducationalHub(HomeController controller) {
        System.out.println("\n=== EDUCATIONAL HUB TEST ===");
        
        System.out.println("\nSimulating voice command: 'I want to learn about the prophet'");
        controller.handleEducationalCommand("prophet");

        try {
            Thread.sleep(5000); // Pause to let the "speech" finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nSimulating voice command: 'Tell me about the quran'");
        controller.handleEducationalCommand("quran");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nSimulating voice command: 'Tell me about history'");
        controller.handleEducationalCommand("history");
        
        System.out.println("\n=== EDUCATIONAL HUB TEST COMPLETE ===");
    }
    
    // --- SIMPLIFICATION: The test method is now much simpler ---
    private static void testSmartMirror(SmartMirror mirror) {
        System.out.println("\n=== SMART MIRROR TEST ===");
        
        System.out.println("1. Testing basic functionality...");
        mirror.turnOn();
        System.out.println("   Mirror status: " + mirror.getStatus());
        
        System.out.println("2. Setting up Calendar Service...");
        mirror.setCalendarEmail("eya.hafsi@example.com");
        System.out.println("   " + mirror.getCalendarStatus());
        
        System.out.println("3. Testing Islamic calendar...");
        mirror.displayIslamicCalendar();
        
        System.out.println("4. Testing comprehensive briefing (this will sync and show reminders)...");
        mirror.displayBriefing();
        
        System.out.println("5. Testing Quran verse display...");
        mirror.displayQuranVerseOnly();
        
        System.out.println("6. Testing verse refresh...");
        mirror.refreshVerse();
        
        System.out.println("7. Qibla direction for your house:");
        System.out.println("   " + mirror.getQiblaDirection());
        System.out.println("   City: " + mirror.getCity());
        
        System.out.println("8. Testing calendar commands...");
        mirror.executeCommand("SYNC_CALENDAR");
        
        System.out.println("=== SMART MIRROR TEST COMPLETE ===");
    }
    
    private static void testAdhanIntegration(HomeController controller, SmartTV testTV) {
        System.out.println("\n=== ADHAN INTEGRATION TEST ===");
        
        System.out.println("1. Testing AdhanService...");
        System.out.println("   Current time: " + controller.demoGetCurrentTime());
        System.out.println("   Next prayer: " + controller.demoGetNextPrayer());
        
        System.out.println("2. Testing TV mute functionality...");
        if (testTV != null) {
            System.out.println("   Found TV: " + testTV.getName());
            System.out.println("   TV status before: " + testTV.getStatus());
            testTV.mute();
            System.out.println("   TV status after mute: " + testTV.getStatus());
            testTV.unmute();
            System.out.println("   TV status after unmute: " + testTV.getStatus());
        } else {
            System.out.println("   [ERROR] No TV found for testing!");
        }
        
        System.out.println("3. Testing Adhan detection...");
        System.out.println("   Setting time to 1 minute before Fajr (05:59)...");
        controller.demoSetTime(5, 59);
        try {
            Thread.sleep(1000);
            System.out.println("   Current time: " + controller.demoGetCurrentTime());
            boolean isAdhanTime = controller.getAutomationEngine().getAdhanService().isAdhanTime();
            System.out.println("   Is Adhan time? " + (isAdhanTime ? "[SUCCESS] YES" : "[INFO] NO"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("4. Testing automatic TV muting...");
        System.out.println("   Setting time to during Fajr Adhan (06:02)...");
        controller.demoSetTime(6, 2);
        System.out.println("   Waiting for Adhan detection...");
        try {
            Thread.sleep(3000);
            boolean shouldBeAdhanTime = controller.getAutomationEngine().getAdhanService().isAdhanTime();
            System.out.println("   Should Adhan be detected? " + (shouldBeAdhanTime ? "[SUCCESS] YES" : "[INFO] NO"));
            if (testTV != null && testTV.isMuted()) {
                System.out.println("   [SUCCESS] TV automatically muted during Adhan!");
            } else {
                System.out.println("   [INFO] TV was not muted automatically.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("5. Testing automatic unmuting after Adhan...");
        System.out.println("   Setting time to after Fajr Adhan (06:10)...");
        controller.demoSetTime(6, 10);
        System.out.println("   Waiting for Adhan to finish...");
        try {
            Thread.sleep(3000);
            boolean shouldNotBeAdhanTime = !controller.getAutomationEngine().getAdhanService().isAdhanTime();
            System.out.println("   Should Adhan be finished? " + (shouldNotBeAdhanTime ? "[SUCCESS] YES" : "[INFO] NO"));
            if (testTV != null && !testTV.isMuted()) {
                System.out.println("   [SUCCESS] TV automatically unmuted after Adhan!");
            } else {
                System.out.println("   [INFO] TV was not unmuted automatically.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("=== TEST COMPLETE ===");
    }
}