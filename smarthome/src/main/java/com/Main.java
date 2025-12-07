package com;

import com.controller.HomeController;
import com.devices.*;
import com.exceptions.SecurityBreachException;
import com.home.Home;
import com.room.Room;

public class Main {
    public static void main(String[] args) {
        System.out.println("🏠 SMART HOME AUTOMATION SYSTEM\n");
        
        try {
            // Create home and rooms
            Home myHome = new Home("My Smart Home");
            Room livingRoom = new Room("Living Room");
            Room bedroom = new Room("Bedroom");
            Room kitchen = new Room("Kitchen");
            
            myHome.addRoom(livingRoom);
            myHome.addRoom(bedroom);
            myHome.addRoom(kitchen);
            
            // Create home controller
            HomeController controller = new HomeController(myHome);
            
            // Add regular smart devices
            Light livingRoomLight = new Light("light-001", "Living Room Light", 100, com.enums.EnergyMode.NORMAL);
            Thermostat mainThermostat = new Thermostat("thermo-001", "Main Thermostat", 22.0);
            SmartTV tv = new SmartTV("tv-001", "Living Room TV");
            
            livingRoom.addDevice(livingRoomLight);
            livingRoom.addDevice(mainThermostat);
            livingRoom.addDevice(tv);
            
            // Add security devices
            SecurityCamera frontCamera = new SecurityCamera("cam-001", "Front Door Camera", "1080p", 90, com.enums.EnergyMode.NORMAL);
            MotionSensor livingRoomSensor = new MotionSensor("motion-001", "Living Room Motion", 10, com.enums.EnergyMode.NORMAL);
            DoorWindowSensor frontDoorSensor = new DoorWindowSensor("door-001", "Front Door", "Main Entrance", com.enums.EnergyMode.NORMAL);
            DoorLock frontDoorLock = new DoorLock("lock-001", "Front Door Lock", com.enums.EnergyMode.NORMAL);
            SmokeDetector kitchenSmokeDetector = new SmokeDetector("smoke-001", "Kitchen Smoke Detector", "Kitchen", com.enums.EnergyMode.NORMAL);
            
            controller.addSecurityDevice(frontCamera);
            controller.addSecurityDevice(livingRoomSensor);
            controller.addSecurityDevice(frontDoorSensor);
            controller.addSecurityDevice(frontDoorLock);
            controller.addSecurityDevice(kitchenSmokeDetector);
            
            // Set schedules
            frontCamera.schedule("record 20:00-06:00");
            frontDoorLock.schedule("auto-lock 23:00-06:00");
            
            System.out.println("=== Initial Setup Complete ===\n");
            
            // Display all devices
            controller.listAllDevices();
            controller.displayAllSecurityDevices();
            
            // Scenario 1: Leaving Home
            System.out.println("\n=== Scenario 1: Leaving Home ===");
            controller.setAwayMode();
            controller.turnOffAllDevices(); // Turn off lights, TV, etc.
            System.out.println("All non-essential devices turned off");
            
            // Scenario 2: Motion Detection While Away (Intruder)
            System.out.println("\n=== Scenario 2: Intruder Detection ===");
            try {
                livingRoomSensor.checkMotion();
            } catch (SecurityBreachException e) {
                System.out.println("⚠️  Security breach detected!");
                controller.handleSecurityEvent("motion-001");
            }
            
            // Scenario 3: Coming Home
            System.out.println("\n=== Scenario 3: Coming Home ===");
            controller.disarmSecuritySystem();
            frontDoorLock.unlock();
            controller.turnOnDevice("light-001");
            mainThermostat.setTargetTemperature(22.0);
            System.out.println("Welcome home! Lights on, comfortable temperature set");
            
            // Scenario 4: Night Mode
            System.out.println("\n=== Scenario 4: Bedtime - Night Mode ===");
            controller.setNightMode();
            controller.turnOffDevice("tv-001");
            controller.turnOffDevice("light-001");
            System.out.println("Good night! Security sensors active");
            
            // Scenario 5: Fire Emergency
            System.out.println("\n=== Scenario 5: Fire Emergency Simulation ===");
            try {
                kitchenSmokeDetector.detectSmoke(true);
            } catch (SecurityBreachException e) {
                System.out.println("🔥 FIRE DETECTED!");
                controller.handleSecurityEvent("smoke-001");
            }
            
            // Final Status
            System.out.println("\n=== Final System Status ===");
            controller.displaySecurityStatus();
            controller.displayEnergyConsumption();
            
        } catch (Exception e) {
            System.err.println("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
