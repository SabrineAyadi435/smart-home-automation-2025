package com.smarthome.model;

import com.smarthome.exceptions.DeviceNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private List<SmartDevice> devices;
    
    public Room(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
    }
    
    public void addDevice(SmartDevice device) {
        devices.add(device);
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
}
