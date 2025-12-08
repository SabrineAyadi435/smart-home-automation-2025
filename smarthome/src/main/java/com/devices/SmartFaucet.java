package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.WaterConsumer;

public class SmartFaucet extends SmartDevice implements Controllable, WaterConsumer {

    private double currentWaterFlowLitersPerMinute; // Example state variable
    private double totalWaterConsumedLiters;       // Example state variable
    private boolean isControllable;                // Example state variable

    // Constructor to initialize the SmartFaucet (calls SmartDevice constructor)
    public SmartFaucet(String deviceId, String name, EnergyMode energyMode, boolean isControllable) {
        super(deviceId, name, energyMode);
        this.isControllable = isControllable;
        this.currentWaterFlowLitersPerMinute = 0.0;
        this.totalWaterConsumedLiters = 0.0;
        this.powerConsumption = 5.0; // Example: Power for sensor/valve control (Watts)
    }

    @Override
    public void turnOn() {
        if (!this.isOn) {
            this.isOn = true;
            this.currentWaterFlowLitersPerMinute = 5.0; // Example flow rate
            System.out.println(this.name + " is now ON. Water is flowing.");
        } else {
            System.out.println(this.name + " is already ON.");
        }
    }

    @Override
    public void turnOff() {
        if (this.isOn) {
            // Assume consumption is tracked continuously and added to total
            // For simplicity here, we'll just reset current flow and turn off
            this.isOn = false;
            this.currentWaterFlowLitersPerMinute = 0.0;
            System.out.println(this.name + " is now OFF. Water flow stopped.");
        } else {
            System.out.println(this.name + " is already OFF.");
        }
    }

    @Override
    public String getStatus() {
        String baseStatus = this.isOn ? "ON" : "OFF";
        return String.format("%s (Flow: %.2f L/min, Total Water: %.2f L, Power: %.2f W)",
                baseStatus,
                this.currentWaterFlowLitersPerMinute,
                this.totalWaterConsumedLiters,
                getEnergyConsumption());
    }

    @Override
    public double getEnergyConsumption() {
        // Return power consumption only if the faucet is 'on' or in a standby state
        return this.isOn ? this.powerConsumption : 0.5; // Example: 5W when ON, 0.5W standby
    }

    @Override
    public void executeCommand(String command) {
        if (isControllable) {
            switch (command.toLowerCase()) {
                case "flow high":
                    this.currentWaterFlowLitersPerMinute = 10.0;
                    System.out.println(this.name + " flow set to HIGH.");
                    break;
                case "flow low":
                    this.currentWaterFlowLitersPerMinute = 2.0;
                    System.out.println(this.name + " flow set to LOW.");
                    break;
                case "pause":
                    turnOff(); // A pause command could be a temporary turnOff
                    break;
                default:
                    System.out.println("Unknown command for " + this.name + ": " + command);
            }
        } else {
            System.out.println(this.name + " is not externally controllable.");
        }
    }

    @Override
    public boolean isControllable() {
        return this.isControllable;
    }

    @Override
    public double getTotalwaterConsumption() {
        return this.totalWaterConsumedLiters;
    }

    @Override
    public double getCurrentwaterConsumption() {
        // Assume current water consumption is the flow rate per unit time (e.g., L/min)
        return this.currentWaterFlowLitersPerMinute;
    }

    // Example method to simulate tracking water consumption (could be called by a timer/event)
    public void trackWaterConsumption(double timeInMinutes) {
        if (this.isOn) {
            double consumed = this.currentWaterFlowLitersPerMinute * timeInMinutes;
            this.totalWaterConsumedLiters += consumed;
        }
    }
}