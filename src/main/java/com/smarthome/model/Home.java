package com.smarthome.model;

import com.smarthome.exceptions.DeviceNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Home {
    private String name;
    private List<Room> rooms;
    
    public Home(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
    }
    
    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("Room " + room.getName() + " added to home");
    }
    
    public void removeRoom(String roomName) {
        rooms.removeIf(r -> r.getName().equals(roomName));
    }
    
    public SmartDevice findDeviceById(String deviceId) throws DeviceNotFoundException {
        for (Room room : rooms) {
            SmartDevice device = room.findDeviceById(deviceId);
            if (device != null) {
                return device;
            }
        }
        throw new DeviceNotFoundException("Device with ID " + deviceId + " not found in home");
    }
    
    public List<SmartDevice> getAllDevices() {
        List<SmartDevice> allDevices = new ArrayList<>();
        for (Room room : rooms) {
            allDevices.addAll(room.getDevices());
        }
        return allDevices;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }
}
