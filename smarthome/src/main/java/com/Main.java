package com;

import java.time.LocalTime;

import com.controller.HomeController;
import com.devices.AlarmSiren;
import com.devices.DoorLock;
import com.devices.DoorWindowSensor;
import com.devices.Light;
import com.devices.MotionSensor;
import com.devices.SecurityCamera;
import com.devices.SmartMirror;
import com.devices.SmartTV;
import com.devices.SmokeDetector;
import com.devices.Thermostat;
import com.enums.EnergyMode;
import com.exceptions.SecurityBreachException;
import com.home.Home;
import com.room.Room;


public class Main {
    public static void main(String[] args) {
        // Create home and controller
        Home myHome = new Home("My Smart Home");
        HomeController controller = new HomeController(myHome);
        
        // Create rooms
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        
        // Create devices
        Light livingRoomLight = new Light("LR-Light-01", "Living Room Light", 100, EnergyMode.NORMAL);
        Thermostat thermostat = new Thermostat("TH-01", "Main Thermostat", 22.0);
        SmartTV tv = new SmartTV("TV-01", "Living Room TV");
        MotionSensor sensor = new MotionSensor("MS-01", "Entry Sensor", EnergyMode.NORMAL);
        SmartMirror smartMirror = new SmartMirror("SM-01", "Smart Mirror", EnergyMode.HIGH);
        
        smartMirror.displayIslamicCalendar();
        smartMirror.displayQuranVerseOnly();
        smartMirror.getCalendarStatus();

        // --- MotionSensor quick test ---
        System.out.println("\n--- MotionSensor Test ---");
        sensor.executeCommand("STATUS");
        sensor.executeCommand("ON");
        try {
            boolean detected = sensor.checkMotion();
            System.out.println("Motion detected: " + detected);
        } catch (SecurityBreachException e) {
            System.err.println("Security breach: " + e.getMessage());
        }
        sensor.executeCommand("SET_SENSITIVITY 9");
        sensor.executeCommand("STATUS");
        // Schedule a quick task 1 second from now to demonstrate scheduling
        sensor.scheduleTask(LocalTime.now().plusSeconds(1), () -> System.out.println("Scheduled task executed for MotionSensor"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sensor.cancelScheduledTasks();

        // --- SmokeDetector quick test ---
        System.out.println("\n--- SmokeDetector Test ---");
        SmokeDetector smoke = new SmokeDetector("SD-01", "Kitchen Smoke", "Kitchen", EnergyMode.NORMAL);
        smoke.executeCommand("STATUS");
        smoke.executeCommand("ON");
        try {
            smoke.detectSmoke(true);
        } catch (SecurityBreachException e) {
            System.err.println("Smoke security breach: " + e.getMessage());
        }
        smoke.executeCommand("SET_SENSITIVITY 8");
        smoke.executeCommand("STATUS");
        smoke.scheduleTask(LocalTime.now().plusSeconds(1), () -> System.out.println("Scheduled task executed for SmokeDetector"));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        smoke.cancelScheduledTasks();

        // --- DoorWindowSensor quick test ---
        System.out.println("\n--- DoorWindowSensor Test ---");
        DoorWindowSensor doorSensor = new DoorWindowSensor("DWS-01", "Front Door Sensor", "Front Door", EnergyMode.NORMAL);
        doorSensor.executeCommand("STATUS");
        doorSensor.executeCommand("ON");
        doorSensor.executeCommand("OPEN");
        doorSensor.executeCommand("STATUS");
        doorSensor.executeCommand("CLOSE");
        doorSensor.executeCommand("STATUS");
        System.out.println("Energy consumption: " + doorSensor.getEnergyConsumption());

        // --- AlarmSiren quick test ---
        AlarmSiren siren = new AlarmSiren("AS-01", "Home Siren", EnergyMode.NORMAL);
        System.out.println("\n--- AlarmSiren Test ---");
        siren.executeCommand("STATUS");
        siren.executeCommand("TRIGGER");
        siren.executeCommand("SET_VOLUME 5");
        siren.executeCommand("SET_DURATION 45");
        siren.executeCommand("STATUS");
        siren.executeCommand("STOP");
        System.out.println("Energy consumption: " + siren.getEnergyConsumption());



    //     // Add devices to rooms
    //     livingRoom.addDevice(livingRoomLight);
    //     livingRoom.addDevice(tv);
    //     livingRoom.addDevice(sensor);
    //     bedroom.addDevice(thermostat);
    //     bedroom.addDevice(smartMirror);

        // --- DoorLock quick test ---
        System.out.println("\n--- DoorLock Test ---");
        DoorLock door = new DoorLock("DL-01", "Front Door", EnergyMode.NORMAL);
        door.executeCommand("STATUS");
        door.executeCommand("UNLOCK");
        door.executeCommand("STATUS");
        door.executeCommand("LOCK");
        door.executeCommand("SET_AUTOLOCK 10");
        door.executeCommand("STATUS");
        // Schedule a short task one minute from now (won't necessarily run during a quick run)
        door.scheduleTask(LocalTime.now().plusMinutes(1), () -> System.out.println("Scheduled task executed for DoorLock"));
        
        // --- SecurityCamera quick test ---
        System.out.println("\n--- SecurityCamera Test ---");
        SecurityCamera camera = new SecurityCamera("SC-01", "Front Camera", "1080p", 120, EnergyMode.NORMAL);
        camera.executeCommand("STATUS");
        camera.executeCommand("RECORD");
        camera.executeCommand("STATUS");
        camera.executeCommand("TOGGLE_NIGHT");
        camera.executeCommand("SET_RESOLUTION 480p");
        System.out.println("Energy consumption: " + camera.getEnergyConsumption());
        
    //     // Add rooms to home
    //     myHome.addRoom(livingRoom);
    //     myHome.addRoom(bedroom);
        
    //     // Display all devices
    //     System.out.println("=== Smart Home Status ===");
    //     controller.listAllDevices();
        
    //     // Control devices
    //     System.out.println("\n=== Controlling Devices ===");
    //     controller.turnOnDevice("LR-Light-01");
    //     controller.turnOnDevice("TH-01");
        
    //     // Set specific properties
    //     livingRoomLight.setBrightness(75);
    //     thermostat.setTargetTemperature(24.0);
        
    //     System.out.println("\n=== Updated Status ===");
    //     controller.listAllDevices();
        
    //     // Automation example
    //     System.out.println("\n=== Automation Rules ===");
    //     AutomationEngine engine = new AutomationEngine();
    //     Rule motionRule = new Rule("Motion Light Rule", 
    //         () -> sensor.isMotionDetected(),
    //         () -> livingRoomLight.turnOn()
    //     );
    //     engine.addRule(motionRule);
        
    //     // Simulate motion detection
    //     sensor.detectMotion();
    //     engine.evaluateRules();


    //     // Test Smart Mirror
    //     System.out.println("\n=== Smart Mirror Commands ===");
    //     smartMirror.executeCommand("ON");
    //     smartMirror.executeCommand("OFF");
    //     smartMirror.executeCommand("ON");

        
    //     // smartMirror.executeCommand("TOGGLE");
    //     // smartMirror.executeCommand("TOGGLE");

    //     smartMirror.executeCommand("BRIEFING");
    //     smartMirror.executeCommand("CALENDAR");
    //     smartMirror.executeCommand("QURAN");
    //     smartMirror.executeCommand("REFRESH_VERSE");
    //     smartMirror.executeCommand("SYNC_CALENDAR");
    //     smartMirror.executeCommand("UNKNOWN_COMMAND");
        
    //     System.out.println("\n=== Energy Consumption ===");
    //     controller.displayEnergyConsumption();
    }
}