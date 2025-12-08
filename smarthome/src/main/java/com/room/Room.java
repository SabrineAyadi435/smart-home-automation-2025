package com.room;

import java.util.ArrayList;
import java.util.List;

import com.devices.SmartDevice;
import com.enums.AirQuality;
import com.exceptions.DeviceNotFoundException;

/**
 * Room holds devices and computes:
 * - currentEnergyConsumption: instantaneous power (in watts, W)
 * - totalEnergyConsumption: accumulated energy (in watt-hours, Wh)
 *
 * Notes:
 * - It assumes SmartDevice#getPowerConsumption() returns power in watts (W).
 * - If SmartDevice has an isOn() method, it will be consulted (via reflection).
 *   If not present, the code will sum getPowerConsumption() directly (so device
 *   implementations should return 0 when powered off OR provide an isOn()).
 */
public class Room {
    private String name;
    private List<SmartDevice> devices;
    private double temperature;
    private AirQuality airQuality;
    /** instantaneous power in watts (W) */
    private float currentEnergyConsumption;
    /** accumulated energy in watt-hours (Wh) */
    private float totalEnergyConsumption;

    private double currentwaterConsumption = 13.0;
    private double totalwaterConsumption = 100.0;
    
    public Room(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
        this.currentEnergyConsumption = 0f;
        this.totalEnergyConsumption = 0f;
        this.temperature = 20f;
        this.airQuality = AirQuality.GOOD;

    }
    
    public void addDevice(SmartDevice device) {
        devices.add(device);
        device.setRoom(this);
        System.out.println("Device " + device.getName() + " added to " + name);
    }
    
    public void removeDevice(String deviceId) throws DeviceNotFoundException {
        SmartDevice device = findDeviceById(deviceId);
        if (device != null) {
            devices.remove(device);
            System.out.println("Device " + device.getName() + " removed from " + name);
        } else {
            throw new DeviceNotFoundException("Device with ID " + deviceId + " not found in " + name);
        }
    }
    
    public SmartDevice findDeviceById(String deviceId) {
        return devices.stream()
            .filter(d -> d.getDeviceId().equals(deviceId))
            .findFirst()
            .orElse(null);
    }
    
    public List<SmartDevice> findDevicesByType(Class<? extends SmartDevice> type) {
        List<SmartDevice> result = new ArrayList<>();
        for (SmartDevice device : devices) {
            if (type.isInstance(device)) {
                result.add(device);
            }
        }
        return result;
    }
    
    public String getName() {
        return name;
    }
    
    public List<SmartDevice> getDevices() {
        return new ArrayList<>(devices);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double  temperature) {
        this.temperature = temperature;
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        this.airQuality = airQuality; // can be used by thermostates to change the room temperature
    }

    /**
     * Return instantaneous power draw in W.
     */
    public synchronized float getCurrentEnergyConsumption() {
        return currentEnergyConsumption;
    }

    /**
     * Recalculate the room's current total instantaneous power (W).
     * This resets currentEnergyConsumption to the sum of all device.getPowerConsumption()
     * but will attempt to respect a device's isOn() if present.
     */
    public synchronized void recalculateCurrentEnergyConsumption() {
        float sum = 0f;

        for (SmartDevice device : devices) {
            if (device.isOn()) {
                // assume getPowerConsumption returns watts (W)
                sum += device.getEnergyConsumption(); // device.getPowerConsumption();
            }
        }

        this.currentEnergyConsumption = sum;
    }

    /**
     * Accumulate energy into totalEnergyConsumption using the current instantaneous power.
     * @param durationSeconds duration during which the current power was sustained (seconds)
     *
     * Calculation:
     *   energy (Wh) = power (W) * time (h) = power * (seconds / 3600)
     */
    public synchronized void accumulateEnergyUsageSeconds(long durationSeconds) {
        if (durationSeconds <= 0) return;
        // ensure current is up-to-date before accumulating
        recalculateCurrentEnergyConsumption();
        float energyWh = this.currentEnergyConsumption * (durationSeconds / 3600.0f);
        this.totalEnergyConsumption += energyWh;
    }

    /**
     * Convenience: accumulate energy using minutes.
     * @param durationMinutes minutes
     */
    public synchronized void accumulateEnergyUsageMinutes(float durationMinutes) {
        if (durationMinutes <= 0f) return;
        recalculateCurrentEnergyConsumption();
        float energyWh = this.currentEnergyConsumption * (durationMinutes / 60.0f);
        this.totalEnergyConsumption += energyWh;
    }

    /**
     * Add an explicit energy amount (in Wh) to the total.
     */
    public synchronized void addToTotalEnergyConsumptionWh(float energyWh) {
        if (energyWh <= 0f) return;
        this.totalEnergyConsumption += energyWh;
    }

    /**
     * Return accumulated energy in Wh.
     */
    public synchronized float getTotalEnergyConsumptionWh() {
        return totalEnergyConsumption;
    }

    /**
     * Return accumulated energy in kWh.
     */
    public synchronized float getTotalEnergyConsumptionKWh() {
        return totalEnergyConsumption / 1000.0f;
    }

    /**
     * Reset total energy counter (useful for billing cycles / daily reset).
     */
    public synchronized void resetTotalEnergyConsumption() {
        this.totalEnergyConsumption = 0f;
    }

    public void setTotalEnergyConsumption(float totalEnergyConsumption) {
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public double getTotalwaterConsumption() {
        return totalwaterConsumption;
    }

    public void setTotalwaterConsumption(double totalwaterConsumption) {
        this.totalwaterConsumption = totalwaterConsumption;
    }

    public double getCurrentwaterConsumption() {
        return currentwaterConsumption;
    }

    public void setCurrentwaterConsumption(double currentwaterConsumption) {
        this.currentwaterConsumption = currentwaterConsumption;
    }

}
