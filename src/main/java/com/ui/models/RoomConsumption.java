package com.ui.models;

/**
 * Model class for displaying room consumption data in TableView.
 * Represents energy and water consumption metrics for a single room.
 */
public class RoomConsumption {
    private String roomName;
    private double currentEnergy;  // in watts (W)
    private double totalEnergy;    // in kilowatt-hours (kWh)
    private double currentWater;   // in liters per minute (L/min)
    private double totalWater;     // in liters (L)
    
    public RoomConsumption(String roomName, double currentEnergy, double totalEnergy, 
                          double currentWater, double totalWater) {
        this.roomName = roomName;
        this.currentEnergy = currentEnergy;
        this.totalEnergy = totalEnergy;
        this.currentWater = currentWater;
        this.totalWater = totalWater;
    }
    
    // Getters and setters
    public String getRoomName() {
        return roomName;
    }
    
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
    public double getCurrentEnergy() {
        return currentEnergy;
    }
    
    public void setCurrentEnergy(double currentEnergy) {
        this.currentEnergy = currentEnergy;
    }
    
    public double getTotalEnergy() {
        return totalEnergy;
    }
    
    public void setTotalEnergy(double totalEnergy) {
        this.totalEnergy = totalEnergy;
    }
    
    public double getCurrentWater() {
        return currentWater;
    }
    
    public void setCurrentWater(double currentWater) {
        this.currentWater = currentWater;
    }
    
    public double getTotalWater() {
        return totalWater;
    }
    
    public void setTotalWater(double totalWater) {
        this.totalWater = totalWater;
    }
    
    @Override
    public String toString() {
        return String.format("RoomConsumption{room='%s', currentE=%.2fW, totalE=%.2fkWh, currentW=%.2fL/min, totalW=%.2fL}",
                roomName, currentEnergy, totalEnergy, currentWater, totalWater);
    }
}
