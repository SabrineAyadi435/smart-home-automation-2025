
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class SmartHomeSecurity {
    public static void main(String[] args) throws SecurityBreachException {
        System.out.println("🏠 SMART HOME SECURITY SYSTEM INITIALIZATION\n");
        
        try {
            // Create security controller
            SecurityController security = new SecurityController();
            
            // Create and add security devices
            SecurityCamera frontCamera = new SecurityCamera("cam-001", "Front Door Camera", "1080p", 90);
            MotionSensor livingRoomSensor = new MotionSensor("motion-001", "Living Room Motion", 10);
            DoorWindowSensor frontDoorSensor = new DoorWindowSensor("door-001", "Front Door", "Main Entrance");
            DoorLock frontDoorLock = new DoorLock("lock-001", "Front Door Lock");
            SmokeDetector kitchenSmokeDetector = new SmokeDetector("smoke-001", "Kitchen Smoke Detector", "Kitchen");
            
            // Add devices to controller
            security.addSecurityDevice(frontCamera);
            security.addSecurityDevice(livingRoomSensor);
            security.addSecurityDevice(frontDoorSensor);
            security.addSecurityDevice(frontDoorLock);
            security.addSecurityDevice(kitchenSmokeDetector);
            
            // Set schedules
            frontCamera.schedule("record 20:00-06:00");
            frontDoorLock.schedule("auto-lock 23:00-06:00");
            
            System.out.println("\n=== Initial Status ===");
            security.getAllDeviceStatuses().forEach((id, status) -> 
                System.out.println(status));
            
            // Test scenarios
            System.out.println("\n=== Scenario 1: Arming System ===");
            security.armSystem();
            System.out.println("System Status: " + security.getSystemStatus());
            
            System.out.println("\n=== Scenario 2: Motion Detection (Intruder Alert) ===");
            try {
                livingRoomSensor.checkMotion();
            } catch (SecurityBreachException e) {
                System.out.println("Security breach handled: " + e.getMessage());
                security.handleTriggeredSensor("motion-001");
            }
            
            System.out.println("\n=== Scenario 3: Coming Home ===");
            security.disarmSystem();
            frontDoorLock.unlock();
            System.out.println("System disarmed automatically when door unlocked");
            
            System.out.println("\n=== Scenario 4: Night Mode ===");
            security.setNightMode();
            
            System.out.println("\n=== Scenario 5: Door Opened at Night ===");
            try {
                frontDoorSensor.simulateOpen();
            } catch (SecurityBreachException e) {
                System.out.println("Night security breach: " + e.getMessage());
                security.handleTriggeredSensor("door-001");
            }
            
            System.out.println("\n=== Final Status ===");
            security.getAllDeviceStatuses().forEach((id, status) -> 
                System.out.println(status));
            
            System.out.println("\n=== System Log (Last 5 entries) ===");
            security.getSystemLog(5).forEach(System.out::println);
            
            System.out.println("\n=== Energy Consumption Report ===");
            System.out.printf("Total energy consumption: %.2f kWh\n", 
                            security.getTotalEnergyConsumption());
            
        } catch (Exception e) {
            System.err.println("Error in security system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
