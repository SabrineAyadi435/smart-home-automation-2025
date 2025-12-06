
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


class AlarmSiren extends SmartDevice implements Controllable, EnergyConsumer {
    private int volume; // 1-10
    private boolean isSounding;
    private int durationSeconds;
    private double standbyConsumption = 0.1;
    private double activeConsumption = 50.0;
    private boolean energySavingMode = false;
    
    public AlarmSiren(String deviceId, String name) {
        super(deviceId, name);
        this.volume = 8;
        this.isSounding = false;
        this.durationSeconds = 30;
    }
    
    @Override
    public void turnOn() throws InvalidOperationException {
        if (isSounding) {
            throw new InvalidOperationException("Alarm is already sounding");
        }
        this.isActive = true;
        this.isSounding = true;
        updateTimestamp();
        startAlarmSequence();
    }
    
    @Override
    public void turnOff() {
        this.isSounding = false;
        this.isActive = false;
        updateTimestamp();
        stopAlarmSequence();
    }
    
    @Override
    public boolean isOn() {
        return isSounding;
    }
    
    public void triggerAlarm() throws InvalidOperationException {
        turnOn();
        logEvent("Alarm triggered at volume " + volume);
    }
    
    public void stopAlarm() {
        turnOff();
        logEvent("Alarm stopped");
    }
    
    public void setVolume(int level) throws InvalidOperationException {
        if (level < 1 || level > 10) {
            throw new InvalidOperationException("Volume must be between 1 and 10");
        }
        this.volume = level;
        updateTimestamp();
    }
    
    public void setDuration(int seconds) throws InvalidOperationException {
        if (seconds < 10 || seconds > 300) {
            throw new InvalidOperationException("Duration must be between 10 and 300 seconds");
        }
        this.durationSeconds = seconds;
    }
    
    private void startAlarmSequence() {
        System.out.println("🚨 ALARM SOUNDING at volume " + volume + " for " + durationSeconds + " seconds!");
        // In real implementation, this would trigger hardware alarm
    }
    
    private void stopAlarmSequence() {
        System.out.println("Alarm silenced");
    }
    
    @Override
    public String getStatus() {
        return String.format("AlarmSiren[%s] - Sounding: %s | Volume: %d | Duration: %ds",
                deviceName, isSounding ? "YES" : "NO", volume, durationSeconds);
    }
    
    @Override
    public double calculateEnergyConsumption() {
        double baseConsumption = isSounding ? activeConsumption : standbyConsumption;
        return energySavingMode ? baseConsumption * 0.8 : baseConsumption;
    }
    
    @Override
    public double getEnergyConsumptionRate() {
        return calculateEnergyConsumption();
    }
    
    @Override
    public void setEnergySavingMode(boolean enable) {
        this.energySavingMode = enable;
        if (enable && volume > 6) {
            volume = 6; // Reduce volume in energy saving mode
        }
    }
    
    @Override
    public String getDeviceType() {
        return "ALARM_SIREN";
    }
    
    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(formatter) + 
                          " - AlarmSiren[" + deviceId + "]: " + event);
    }
}
