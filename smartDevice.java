
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


abstract class SmartDevice {
    protected final String deviceId;
    protected String deviceName;
    protected boolean isActive;
    protected LocalDateTime lastUpdated;
    protected static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public SmartDevice(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.isActive = false;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public abstract String getStatus();
    public abstract String getDeviceType();
    
    public String getDeviceId() { return deviceId; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String name) { this.deviceName = name; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    
    protected void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
