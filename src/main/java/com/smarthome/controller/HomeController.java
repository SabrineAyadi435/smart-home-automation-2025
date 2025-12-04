package com.smarthome.controller;

import com.smarthome.model.Home;
import com.smarthome.model.SmartDevice;
import com.smarthome.model.Room;
import com.smarthome.model.Thermostat;
import com.smarthome.model.AirQualitySensor;
import com.smarthome.interfaces.EnergyConsumer;
import com.smarthome.exceptions.DeviceNotFoundException;
import com.smarthome.service.ClimateService;  // Fixed: PascalCase
import com.smarthome.service.IslamicClimateService;
import com.smarthome.service.IslamicWellnessService;
import com.smarthome.service.RamadanModeService;
import com.smarthome.automation.ClimateRule;
import java.util.List;

public class HomeController {
    private Home home;
    
    // Fixed: PascalCase for class types
    private ClimateService climateService;
    private IslamicClimateService islamicClimateService;
    private IslamicWellnessService islamicWellnessService;
    private RamadanModeService ramadanModeService;
    
    public HomeController(Home home) {
        this.home = home;
        
        // Initialize all services with proper error handling
        try {
            this.climateService = new ClimateService(home);
            this.islamicClimateService = new IslamicClimateService(climateService, home);
            this.ramadanModeService = new RamadanModeService(climateService);
            this.islamicWellnessService = new IslamicWellnessService(climateService, islamicClimateService);
        } catch (Exception e) {
            System.err.println("Error initializing services: " + e.getMessage());
            // Initialize with null or fallback
            this.climateService = null;
            this.islamicClimateService = null;
            this.islamicWellnessService = null;
            this.ramadanModeService = null;
        }
    }
    
    // EXISTING METHODS (with added null checks)
    public void listAllDevices() {
        System.out.println("Devices in " + home.getName() + ":");
        for (SmartDevice device : home.getAllDevices()) {
            System.out.println("  " + device);
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
    
    // NEW CLIMATE SERVICE GETTERS (with null checks)
    public ClimateService getClimateService() {
        return climateService;
    }
    
    public IslamicClimateService getIslamicClimateService() {
        return islamicClimateService;
    }
    
    public IslamicWellnessService getIslamicWellnessService() {
        return islamicWellnessService;
    }
    
    public RamadanModeService getRamadanModeService() {
        return ramadanModeService;
    }
    
    // NEW CLIMATE CONTROL METHODS (with null checks)
    
    public void setRoomTemperature(String roomName, double temperature) {
        if (climateService != null) {
            climateService.setRoomTemperature(roomName, temperature);
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public void setWholeHomeTemperature(double temperature) {
        if (climateService != null) {
            climateService.setWholeHomeTemperature(temperature);
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public String getClimateSummary() {
        if (climateService != null) {
            return climateService.getClimateSummary();
        }
        return "Climate service not available";
    }
    
    public void optimizeForComfort() {
        if (climateService != null) {
            climateService.optimizeForComfort();
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public void optimizeForEnergySavings() {
        if (climateService != null) {
            climateService.optimizeForEnergySavings();
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public void checkAirQuality() {
        if (climateService != null) {
            climateService.checkAirQualityStatus();
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public void setHomeMode() {
        if (climateService != null) {
            climateService.setHomeMode();
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public void setAwayMode() {
        if (climateService != null) {
            climateService.setAwayMode();
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public void generateEnergyReport() {
        if (climateService != null) {
            climateService.generateEnergyReport();
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public double getTotalEnergyConsumption() {
        if (climateService != null) {
            return climateService.getTotalEnergyConsumption();
        }
        return 0.0;
    }
    
    // NEW ISLAMIC CLIMATE METHODS (with null checks)
    
    public void enablePrayerTimeAutomation(boolean enable) {
        if (islamicClimateService != null) {
            islamicClimateService.enablePrayerTimeAutomation(enable);
        } else {
            System.out.println("Islamic climate service not available");
        }
    }
    
    public void prepareForPrayer(String prayerName) {
        if (islamicClimateService != null) {
            islamicClimateService.prepareForPrayer(prayerName);
        } else {
            System.out.println("Islamic climate service not available");
        }
    }
    
    public void autoPrepareForNextPrayer() {
        if (islamicClimateService != null) {
            islamicClimateService.autoPrepareForNextPrayer();
        } else {
            System.out.println("Islamic climate service not available");
        }
    }
    
    public void setPrayerRoom(String roomName) {
        if (islamicClimateService != null) {
            islamicClimateService.setPrayerRoom(roomName);
        } else {
            System.out.println("Islamic climate service not available");
        }
    }
    
    // NEW RAMADAN MODE METHODS
    
    public void enableRamadanMode() {
        if (ramadanModeService != null) {
            ramadanModeService.enableRamadanMode();
        } else {
            System.out.println("Ramadan mode service not available");
        }
    }
    
    public void disableRamadanMode() {
        if (ramadanModeService != null) {
            ramadanModeService.disableRamadanMode();
        } else {
            System.out.println("Ramadan mode service not available");
        }
    }
    
    public void prepareForIftar() {
        if (ramadanModeService != null) {
            ramadanModeService.prepareForIftar();
        } else {
            System.out.println("Ramadan mode service not available");
        }
    }
    
    public void prepareForTaraweeh() {
        if (ramadanModeService != null) {
            ramadanModeService.prepareForTaraweeh();
        } else {
            System.out.println("Ramadan mode service not available");
        }
    }
    
    public void prepareForSuhoor() {
        if (ramadanModeService != null) {
            ramadanModeService.prepareForSuhoor();
        } else {
            System.out.println("Ramadan mode service not available");
        }
    }
    
    public void generateRamadanEnergyReport() {
        if (ramadanModeService != null) {
            ramadanModeService.generateRamadanEnergyReport();
        } else {
            System.out.println("Ramadan mode service not available");
        }
    }
    
    // NEW ISLAMIC WELLNESS METHODS
    
    public void prepareForWudu() {
        if (islamicWellnessService != null) {
            islamicWellnessService.prepareForWudu();
        } else {
            System.out.println("Islamic wellness service not available");
        }
    }
    
    public void prepareForTahajjud() {
        if (islamicWellnessService != null) {
            islamicWellnessService.prepareForTahajjud();
        } else {
            System.out.println("Islamic wellness service not available");
        }
    }
    
    public void optimizeForFastingComfort() {
        if (islamicWellnessService != null) {
            islamicWellnessService.optimizeForFastingComfort();
        } else {
            System.out.println("Islamic wellness service not available");
        }
    }
    
    public void enableQuranRecitationMode() {
        if (islamicWellnessService != null) {
            islamicWellnessService.enableQuranRecitationMode();
        } else {
            System.out.println("Islamic wellness service not available");
        }
    }
    
    public void setFamilyGatheringMode() {
        if (islamicWellnessService != null) {
            islamicWellnessService.setFamilyGatheringMode();
        } else {
            System.out.println("Islamic wellness service not available");
        }
    }
    
    public void optimizeForElderlyComfort() {
        if (islamicWellnessService != null) {
            islamicWellnessService.optimizeForElderlyComfort();
        } else {
            System.out.println("Islamic wellness service not available");
        }
    }
    
    // NEW AUTOMATION METHODS
    
    public void executeClimateRules() {
        if (climateService != null) {
            climateService.executeClimateRules();
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public void addClimateRule(ClimateRule rule) {
        if (climateService != null) {
            climateService.addClimateRule(rule);
        } else {
            System.out.println("Climate service not available");
        }
    }
    
    public List<ClimateRule> getClimateRules() {
        if (climateService != null) {
            return climateService.getClimateRules();
        }
        return List.of(); // Return empty list instead of null
    }
    
    // NEW ROOM MANAGEMENT METHODS
    
    public void setRoomPreferredTemperature(String roomName, double temperature) {
        Room room = findRoomByName(roomName);
        if (room != null) {
            room.setPreferredTemperature(temperature);
            room.applyPreferredTemperature();
        } else {
            System.out.println("Room not found: " + roomName);
        }
    }
    
    public void applyAllPreferredTemperatures() {
        System.out.println("Applying preferred temperatures to all rooms...");
        for (Room room : home.getRooms()) {
            room.applyPreferredTemperature();
        }
    }
    
    public void setRoomClimateZone(String roomName, String zoneType) {
        Room room = findRoomByName(roomName);
        if (room != null) {
            room.setClimateZoneType(zoneType);
            System.out.println("Room " + roomName + " set to " + zoneType + " climate zone");
        } else {
            System.out.println("Room not found: " + roomName);
        }
    }
    
    public String getRoomClimateStatus(String roomName) {
        Room room = findRoomByName(roomName);
        if (room != null) {
            return room.getClimateStatus();
        }
        return "Room not found: " + roomName;
    }
    
    // UTILITY METHODS
    
    private Room findRoomByName(String roomName) {
        for (Room room : home.getRooms()) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                return room;
            }
        }
        return null;
    }
    
    public List<Room> getRoomsWithClimateDevices() {
        return home.getRooms().stream()
            .filter(Room::hasClimateDevices)
            .collect(java.util.stream.Collectors.toList());
    }
    
    // INITIALIZATION METHODS
    
    public void initializeDemoClimate() {
        System.out.println("Initializing demo climate setup...");
        
        // Create demo rooms with climate devices
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        Room kitchen = new Room("Kitchen");
        Room bathroom = new Room("Bathroom");
        Room prayerRoom = new Room("Prayer Room");
        
        // Set climate zone types
        prayerRoom.setClimateZoneType("PRAYER");
        bathroom.setClimateZoneType("BATHROOM");
        bedroom.setClimateZoneType("BEDROOM");
        kitchen.setClimateZoneType("KITCHEN");
        livingRoom.setClimateZoneType("LIVING");
        
        // Add climate devices
        Thermostat livingThermostat = new Thermostat("thermo1", "Living Room Thermostat", 22.0);
        Thermostat bedThermostat = new Thermostat("thermo2", "Bedroom Thermostat", 20.0);
        Thermostat kitchenThermostat = new Thermostat("thermo3", "Kitchen Thermostat", 21.0);
        Thermostat bathroomThermostat = new Thermostat("thermo4", "Bathroom Thermostat", 24.0);
        Thermostat prayerThermostat = new Thermostat("thermo5", "Prayer Room Thermostat", 22.0);
        
        AirQualitySensor livingSensor = new AirQualitySensor("air1", "Living Room Air Quality");
        AirQualitySensor bedSensor = new AirQualitySensor("air2", "Bedroom Air Quality");
        AirQualitySensor bathroomSensor = new AirQualitySensor("air3", "Bathroom Air Quality");
        
        // Add devices to rooms
        livingRoom.addDevice(livingThermostat);
        livingRoom.addDevice(livingSensor);
        bedroom.addDevice(bedThermostat);
        bedroom.addDevice(bedSensor);
        kitchen.addDevice(kitchenThermostat);
        bathroom.addDevice(bathroomThermostat);
        bathroom.addDevice(bathroomSensor);
        prayerRoom.addDevice(prayerThermostat);
        
        // Add rooms to home
        home.addRoom(livingRoom);
        home.addRoom(bedroom);
        home.addRoom(kitchen);
        home.addRoom(bathroom);
        home.addRoom(prayerRoom);
        
        // Turn on devices
        livingThermostat.turnOn();
        bedThermostat.turnOn();
        kitchenThermostat.turnOn();
        bathroomThermostat.turnOn();
        prayerThermostat.turnOn();
        livingSensor.turnOn();
        bedSensor.turnOn();
        bathroomSensor.turnOn();
        
        // Set prayer room in Islamic service
        if (islamicClimateService != null) {
            islamicClimateService.setPrayerRoom("Prayer Room");
        }
        
        System.out.println("🕌 Demo climate setup complete with Islamic features!");
    }
    
    // SYSTEM STATUS METHODS
    
    public String getSystemStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== SMART HOME SYSTEM STATUS ===\n");
        status.append("Total Devices: ").append(home.getAllDevices().size()).append("\n");
        status.append("Total Rooms: ").append(home.getRooms().size()).append("\n");
        status.append("Rooms with Climate: ").append(getRoomsWithClimateDevices().size()).append("\n");
        status.append("Total Energy: ").append(String.format("%.2f", getTotalEnergyConsumption())).append(" kWh\n");
        
        status.append("\n--- Islamic Features Status ---\n");
        status.append("Prayer Time Automation: ");
        if (islamicClimateService != null) {
            status.append(islamicClimateService.isPrayerTimeAutomationEnabled() ? "ENABLED" : "DISABLED");
        } else {
            status.append("UNAVAILABLE");
        }
        status.append("\n");
        
        status.append("Ramadan Mode: ");
        if (ramadanModeService != null) {
            status.append(ramadanModeService.isRamadanModeActive() ? "ACTIVE 🌙" : "INACTIVE");
        } else {
            status.append("UNAVAILABLE");
        }
        status.append("\n");
        
        return status.toString();
    }
    
    public void runDailyIslamicOptimization() {
        System.out.println("🕌 Running daily Islamic optimization...");
        if (islamicWellnessService != null) {
            islamicWellnessService.runDailyIslamicWellness();
        }
        if (ramadanModeService != null) {
            ramadanModeService.optimizeFastingHours();
        }
        System.out.println("✅ Daily Islamic optimization complete");
    }
}