
package com.devices;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;
import com.interfaces.Schedulable;

public class SecurityCamera extends SmartDevice implements Controllable, Schedulable, EnergyConsumer {
    private boolean isRecording;
    private boolean isOn;
    private String resolution;
    private boolean nightVisionEnabled;
    private int fieldOfView;
    private double standbyConsumption = 0.5; // kWh
    private double recordingConsumption = 5.0; // kWh
    private String schedulePattern;
    private String liveFeedUrl;
    private java.time.LocalDateTime lastUpdated;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // scheduling support
    private final java.util.Timer scheduler = new java.util.Timer(true);
    private final java.util.List<java.util.TimerTask> scheduledTasks = new java.util.ArrayList<>();

    public SecurityCamera(String deviceId, String name, String resolution, int fieldOfView, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.resolution = resolution;
        this.fieldOfView = fieldOfView;
        this.nightVisionEnabled = false;
        this.isRecording = false;
        this.liveFeedUrl = "rtsp://camera-" + deviceId + "/live";
        this.lastUpdated = LocalDateTime.now();
        this.isOn = false;
    }

    @Override
    public void turnOn() {
        if (isOn && isRecording) {
            System.err.println("Camera is already recording");
            return;
        }
        this.isRecording = true;
        updateTimestamp();
        logEvent("Camera started recording");
    }

    @Override
    public void turnOff() {
        if (!isOn) {
            System.err.println("Camera is already off");
            return;
        }
        this.isRecording = false;
        this.isOn = false;
        updateTimestamp();
        logEvent("Camera stopped recording");
    }

    @Override
    public boolean isOn() {
        return isOn && isRecording;
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
        return "Live feed from " + name + " at " + liveFeedUrl + 
               " | Resolution: " + resolution + 
               " | Night Vision: " + (nightVisionEnabled ? "ON" : "OFF");
    }

    @Override
    public String getStatus() {
        return String.format("Camera[%s] - Recording: %s | Resolution: %s | Night Vision: %s | FOV: %d°",
                name, isRecording ? "YES" : "NO", resolution, 
                nightVisionEnabled ? "ON" : "OFF", fieldOfView);
    }

    public double calculateEnergyConsumption() {
        double baseConsumption = isRecording ? recordingConsumption : standbyConsumption;
        return energyMode == EnergyMode.ECO ? baseConsumption * 0.7 : baseConsumption;
    }


    public double getEnergyConsumption() {
        return calculateEnergyConsumption();
    }

    @Override
    public void setEnergyMode(EnergyMode mode) {
        super.setEnergyMode(mode);
        this.energyMode = mode;
        if (this.energyMode == EnergyMode.ECO) {
            resolution = "720p"; // Lower resolution in energy saving mode
        }
        logEvent("Energy mode set to " + mode + " (energySaving=" + energyMode + ")");
    }

    public void schedule(String schedulePattern) {
        this.schedulePattern = schedulePattern;
        logEvent("Schedule set: " + schedulePattern);
    }

    public String getSchedule() {
        return schedulePattern != null ? schedulePattern : "No schedule set";
    }

    public boolean isScheduledActive() {
        // Simplified schedule checking - in real implementation, use Cron or similar
        return schedulePattern != null && schedulePattern.contains("active");
    }

    public String getDeviceType() {
        return "SECURITY_CAMERA";
    }

    private void logEvent(String event) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + 
                          " - Camera[" + deviceId + "]: " + event);
    }

    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    @Override
    public void executeCommand(String command) {
        if (command == null) return;
        String cmd = command.trim().toUpperCase();
        try {
            if (cmd.equals("ON") || cmd.equals("START") || cmd.equals("RECORD")) {
                turnOn();
            } else if (cmd.equals("OFF") || cmd.equals("STOP")) {
                turnOff();
            } else if (cmd.equals("TOGGLE_NIGHT" )|| cmd.equals("NIGHT")) {
                toggleNightVision();
            } else if (cmd.startsWith("SET_RESOLUTION") || cmd.startsWith("RESOLUTION")) {
                String[] parts = cmd.replace(':', ' ').split("\\s+");
                if (parts.length >= 2) {
                    this.resolution = parts[1];
                    logEvent("Resolution set to " + this.resolution);
                }
            } else if (cmd.equals("STATUS")) {
                System.out.println(getStatus());
            } else {
                System.out.println("Unknown command for SecurityCamera: " + command);
            }
        } catch (Exception e) {
            System.err.println("Command failed: " + e.getMessage());
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    @Override
    public void scheduleTask(LocalTime time, Runnable task) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime runAt = now.withHour(time.getHour()).withMinute(time.getMinute()).withSecond(0).withNano(0);
        if (runAt.isBefore(now)) {
            runAt = runAt.plusDays(1);
        }
        Date runDate = java.util.Date.from(runAt.atZone(java.time.ZoneId.systemDefault()).toInstant());
        java.util.TimerTask timerTask = new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    System.err.println("Scheduled task error: " + e.getMessage());
                }
            }
        };
        scheduledTasks.add(timerTask);
        scheduler.schedule(timerTask, runDate);
        logEvent("Scheduled task at " + time.toString());
    }

    @Override
    public void cancelScheduledTasks() {
        for (java.util.TimerTask t : scheduledTasks) {
            t.cancel();
        }
        scheduledTasks.clear();
        logEvent("All scheduled tasks cancelled");
    }




}