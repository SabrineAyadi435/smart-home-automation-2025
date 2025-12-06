
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class SecurityCamera extends SmartDevice implements Controllable, Schedulable, EnergyConsumer {
    private boolean isRecording;
    private String resolution;
    private boolean nightVisionEnabled;
    private int fieldOfView;
    private double standbyConsumption = 0.5; // kWh
    private double recordingConsumption = 5.0; // kWh
    private String schedulePattern;
    private boolean energySavingMode = false;
    private String liveFeedUrl;
    
    public SecurityCamera(String deviceId, String name, String resolution, int fieldOfView) {
        super(deviceId, name);
        this.resolution = resolution;
        this.fieldOfView = fieldOfView;
        this.nightVisionEnabled = false;
        this.isRecording = false;
        this.liveFeedUrl = "rtsp://camera-" + deviceId + "/live";
    }
    
    @Override
    public void turnOn() throws InvalidOperationException {
        if (isActive && isRecording) {
            throw new InvalidOperationException("Camera is already recording");
        }
        this.isActive = true;
        this.isRecording = true;
        updateTimestamp();
        logEvent("Camera started recording");
    }
    
    @Override
    public void turnOff() throws InvalidOperationException {
        if (!isActive) {
            throw new InvalidOperationException("Camera is already off");
        }
        this.isRecording = false;
        this.isActive = false;
        updateTimestamp();
        logEvent("Camera stopped recording");
    }
    
    @Override
    public boolean isOn() {
        return isActive && isRecording;
    }
    
    public void toggleNightVision() {
        this.nightVisionEnabled = !nightVisionEnabled;
        updateTimestamp();
        logEvent("Night vision " + (nightVisionEnabled ? "enabled" : "disabled"));
    }
    
    public String getLiveFeed() {
        if (!isRecording) {
            return "Camera is not recording";
        }
        return "Live feed from " + deviceName + " at " + liveFeedUrl + 
               " | Resolution: " + resolution + 
               " | Night Vision: " + (nightVisionEnabled ? "ON" : "OFF");
    }
    
    @Override
    public String getStatus() {
        return String.format("Camera[%s] - Recording: %s | Resolution: %s | Night Vision: %s | FOV: %d°",
                deviceName, isRecording ? "YES" : "NO", resolution, 
                nightVisionEnabled ? "ON" : "OFF", fieldOfView);
    }
    
    @Override
    public double calculateEnergyConsumption() {
        double baseConsumption = isRecording ? recordingConsumption : standbyConsumption;
        return energySavingMode ? baseConsumption * 0.7 : baseConsumption;
    }
    
    @Override
    public double getEnergyConsumptionRate() {
        return calculateEnergyConsumption();
    }
    
    @Override
    public void setEnergySavingMode(boolean enable) {
        this.energySavingMode = enable;
        if (enable) {
            resolution = "720p"; // Lower resolution in energy saving mode
        }
    }
    
    @Override
    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
        logEvent("Schedule set: " + schedulePattern);
    }
    
    @Override
    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "No schedule set";
    }
    
    @Override
    public boolean isScheduledActive() {
        // Simplified schedule checking - in real implementation, use Cron or similar
        return schedulePattern != null && schedulePattern.contains("active");
    }
    
    @Override
    public String getDeviceType() {
        return "SECURITY_CAMERA";
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(formatter) + 
                          " - Camera[" + deviceId + "]: " + event);
    }
}
