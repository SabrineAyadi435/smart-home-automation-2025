
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
class DoorWindowSensor extends SmartDevice implements Controllable {
    private boolean isOpen;
    private String location;
    private int openCount = 0;
    
    public DoorWindowSensor(String deviceId, String name, String location) {
        super(deviceId, name);
        this.location = location;
        this.isOpen = false;
    }
    
    @Override
    public void turnOn() {
        this.isActive = true;
        updateTimestamp();
    }
    
    @Override
    public void turnOff() {
        this.isActive = false;
        updateTimestamp();
    }
    
    @Override
    public boolean isOn() {
        return isActive;
    }
    
    public boolean checkStatus() throws SecurityBreachException {
        if (!isActive) return isOpen;
        
        // Simulate status check - in real system, read from magnetic sensor
        Random rand = new Random();
        boolean statusChanged = rand.nextInt(100) < 10; // 10% chance of change
        
        if (statusChanged) {
            isOpen = !isOpen;
            if (isOpen) openCount++;
            updateTimestamp();
            
            logEvent((isOpen ? "Opened" : "Closed"));
            
            if (isOpen && isActive) {
                throw new SecurityBreachException(
                    "Door/Window opened in " + location, 
                    location
                );
            }
        }
        return isOpen;
    }
    
    public void simulateOpen() throws SecurityBreachException {
        if (!isOpen) {
            isOpen = true;
            openCount++;
            updateTimestamp();
            
            if (isActive) {
                throw new SecurityBreachException(
                    "Door/Window forced open in " + location, 
                    location
                );
            }
        }
    }
    
    public void simulateClose() {
        if (isOpen) {
            isOpen = false;
            updateTimestamp();
        }
    }
    
    @Override
    public String getStatus() {
        return String.format("DoorWindowSensor[%s] - Location: %s | State: %s | Open Count: %d",
                deviceName, location, isOpen ? "OPEN" : "CLOSED", openCount);
    }
    
    @Override
    public String getDeviceType() {
        return "DOOR_WINDOW_SENSOR";
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(formatter) + 
                          " - DoorWindowSensor[" + deviceId + "]: " + event);
    }
}
