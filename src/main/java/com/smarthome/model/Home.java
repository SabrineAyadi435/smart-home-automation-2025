package com.smarthome.model;


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
