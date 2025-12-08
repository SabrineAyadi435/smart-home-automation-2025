package com.home;


import java.util.ArrayList;
import java.util.List;

import com.devices.SmartDevice;
import com.exceptions.DeviceNotFoundException;
import com.room.Room;

public class Home {
    private String name;
    private List<Room> rooms;
    private double totalEnergyConsumption = 0.0;
    private double currentEnergyConsumption = 0.0;
    private double totalwaterConsumption = 0.0;
    private double currentwaterConsumption = 0.0; //for bathrooms and kitchens

    
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

    public double getTotalEnergyConsumption() {
        setTotalEnergyConsumption();
        return totalEnergyConsumption;
    }

    public void setTotalEnergyConsumption() {
        double sum = 0;
        for (Room room : rooms) {
            sum += room.getTotalEnergyConsumptionKWh();
        }
        this.totalEnergyConsumption = sum;
    }

    public double getCurrentEnergyConsumption() {
        setCurrentEnergyConsumption();
        return currentEnergyConsumption;
    }

    public void setCurrentEnergyConsumption() {
        double sum = 0;
        for (Room room : rooms) {
            sum += room.getCurrentEnergyConsumption();
        }
        this.currentEnergyConsumption = sum;
    }

    public double getTotalwaterConsumption() {
        setTotalwaterConsumption();
        return totalwaterConsumption;
    }

    public void setTotalwaterConsumption() {
        double sum = 0;
        for (Room room : rooms) {
            if (room.getName().equals("Bathroom") || room.getName().equals("Kitchen")) {
                sum += room.getTotalwaterConsumption();
            }
        }
        this.totalwaterConsumption = sum;}

    public double getCurrentwaterConsumption() {
        setCurrentwaterConsumption();
        return currentwaterConsumption;
    }

    public void setCurrentwaterConsumption() {
        double sum = 0;
        for (Room room : rooms) {
            if (room.getName().equals("Bathroom") || room.getName().equals("Kitchen")) {
                sum += room.getCurrentwaterConsumption();
            }
        }
        this.currentwaterConsumption = sum;
    }

    public void wuduTime(){
        System.out.println("\n💧 **Wudu Time** 💧");
        this.totalwaterConsumption += 0.5;


    }
}