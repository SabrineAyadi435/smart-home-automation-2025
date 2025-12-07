package com.controller;

import java.sql.Time;

import com.devices.SmartDevice;
import com.enums.AirQuality;
import com.enums.SystemStatus;
import com.exceptions.DeviceNotFoundException;
import com.exceptions.InvalidOperationException;
import com.home.Home;
import com.interfaces.EnergyConsumer;

public class HomeController {
    private Home home;
    private SecurityController securityController;
    private Time currentTime;
    private float currentTemperature;
    private AirQuality airQuality;

    public HomeController(Home home) {
        this.home = home;
        this.securityController = new SecurityController();
    }

    public void listAllDevices() {
        System.out.println("Devices in " + home.getName() + ":");
        for (SmartDevice device : home.getAllDevices()) {
            System.out.println("  " + device);
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
            if (device instanceof EnergyConsumer) {
                double consumption = ((EnergyConsumer) device).getEnergyConsumption();
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
}