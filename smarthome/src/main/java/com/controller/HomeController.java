package com.controller;

import java.sql.Time;

import com.devices.SmartDevice;
import com.enums.AirQuality;
import com.exceptions.DeviceNotFoundException;
import com.home.Home;
import com.interfaces.EnergyConsumer;

public class HomeController {
    private Home home;
    private Time currentTime;
    private float currentTemperature;
    private AirQuality AirQuality;

    
    public HomeController(Home home) {
        this.home = home;
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
        System.out.printf("Total Energy Consumption: %.2f kWh\n", totalEnergy);
    }
}