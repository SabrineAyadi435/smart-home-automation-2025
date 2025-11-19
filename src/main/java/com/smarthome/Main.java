package com.smarthome;

import com.smarthome.controller.HomeController;
import com.smarthome.model.*;
import com.smarthome.automation.AutomationEngine;
import com.smarthome.automation.Rule;

public class Main {
    public static void main(String[] args) {
        // Create home and controller
        Home myHome = new Home("My Smart Home");
        HomeController controller = new HomeController(myHome);
        
        // Create rooms
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        
        // Create devices
        Light livingRoomLight = new Light("LR-Light-01", "Living Room Light", 100);
        Thermostat thermostat = new Thermostat("TH-01", "Main Thermostat", 22.0);
        SmartTV tv = new SmartTV("TV-01", "Living Room TV");
        MotionSensor sensor = new MotionSensor("MS-01", "Entry Sensor");
        
        // Add devices to rooms
        livingRoom.addDevice(livingRoomLight);
        livingRoom.addDevice(tv);
        livingRoom.addDevice(sensor);
        bedroom.addDevice(thermostat);
        
        // Add rooms to home
        myHome.addRoom(livingRoom);
        myHome.addRoom(bedroom);
        
        // Display all devices
        System.out.println("=== Smart Home Status ===");
        controller.listAllDevices();
        
        // Control devices
        System.out.println("\n=== Controlling Devices ===");
        controller.turnOnDevice("LR-Light-01");
        controller.turnOnDevice("TH-01");
        
        // Set specific properties
        livingRoomLight.setBrightness(75);
        thermostat.setTargetTemperature(24.0);
        
        System.out.println("\n=== Updated Status ===");
        controller.listAllDevices();
        
        // Automation example
        System.out.println("\n=== Automation Rules ===");
        AutomationEngine engine = new AutomationEngine();
        Rule motionRule = new Rule("Motion Light Rule", 
            () -> sensor.isMotionDetected(),
            () -> livingRoomLight.turnOn()
        );
        engine.addRule(motionRule);
        
        // Simulate motion detection
        sensor.detectMotion();
        engine.evaluateRules();
        
        System.out.println("\n=== Energy Consumption ===");
        controller.displayEnergyConsumption();
    }
}
