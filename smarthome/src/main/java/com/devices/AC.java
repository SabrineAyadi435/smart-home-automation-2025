package com.devices;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;
import com.interfaces.Schedulable;

public class AC extends SmartDevice implements Controllable, EnergyConsumer, Schedulable {
    private double targetTemperature;
    private double currentTemperature;
    private Map<LocalTime, Runnable> scheduledTasks;
    private Timer timer;

    public AC(String deviceId, String name, double targetTemperature) {
        super(deviceId, name, EnergyMode.NORMAL);
        this.targetTemperature = targetTemperature;
        this.currentTemperature = 20.0;
        this.scheduledTasks = new HashMap<>();
        this.isOn = false;
        this.timer = new Timer(true); // daemon timer
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " turned ON - Target: " + targetTemperature + "°C");
        startTemperatureRegulation();
    }

    @Override
    public void turnOff() {
        this.isOn = false;
        System.out.println(name + " turned OFF");
        timer.purge(); // stop heating/cooling
    }

    @Override
    public String getStatus() {
        updateCurrentTemperatureFromRoom();
        return isOn ? String.format("ON (Target: %.1f°C, Current: %.1f°C)", 
            targetTemperature, currentTemperature) : "OFF";
    }

    public void setTargetTemperature(double temperature) {
        if (temperature < 10 || temperature > 35) {
            throw new IllegalArgumentException("Temperature must be between 10 and 35°C");
        }
        this.targetTemperature = temperature;
        System.out.println(name + " target temperature set to " + temperature + "°C");
    }
    
    public double getTargetTemperature() {
        return targetTemperature;
    }
    
    private String fanSpeed = "MEDIUM"; // LOW, MEDIUM, HIGH, AUTO
    
    public void setFanSpeed(String speed) {
        if (!speed.equals("LOW") && !speed.equals("MEDIUM") && !speed.equals("HIGH") && !speed.equals("AUTO")) {
            throw new IllegalArgumentException("Fan speed must be LOW, MEDIUM, HIGH, or AUTO");
        }
        this.fanSpeed = speed;
        System.out.println(name + " fan speed set to " + speed);
    }
    
    public String getFanSpeed() {
        return fanSpeed;
    }

    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("ON")) {
            turnOn();
        } else if (command.equalsIgnoreCase("OFF")) {
            turnOff();
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    @Override
    public double getEnergyConsumption() {
        updateCurrentTemperatureFromRoom();
        return isOn ? Math.abs(targetTemperature - currentTemperature) * 2.5 : 0.0;
    }

    @Override
    public void scheduleTask(LocalTime time, Runnable task) {
        scheduledTasks.put(time, task);
        System.out.println("Task scheduled for " + time);
    }

    @Override
    public void cancelScheduledTasks() {
        scheduledTasks.clear();
        System.out.println("All scheduled tasks cancelled");
    }

    private void updateCurrentTemperatureFromRoom() {
        if (room != null) {
            this.currentTemperature = room.getTemperature();
        }
    }

    /**
     * Starts automatic heating/cooling towards target temperature
     */
    private void startTemperatureRegulation() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isOn || room == null) return;

                double roomTemp = room.getTemperature();
                double diff = (targetTemperature - roomTemp);

                if (Math.abs(diff) < 0.1) return; // already at target

                double changeRate = 0.2; // °C per tick
                if (diff > 0) {
                    room.setTemperature((roomTemp + changeRate)); // heat
                } else {
                    room.setTemperature((roomTemp - changeRate)); // deheat/cool
                }

                currentTemperature = room.getTemperature();
                System.out.printf("%s adjusted room temperature: %.2f°C%n", name, currentTemperature);
            }
        }, 0, 1000); // every 1 second
    }
}
