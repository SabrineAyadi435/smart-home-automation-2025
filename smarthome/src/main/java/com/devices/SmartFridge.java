package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

// SmartFridge explicitly implements Controllable AND EnergyConsumer
public class SmartFridge extends SmartDevice implements Controllable, EnergyConsumer {

    private double currentTemperatureCelsius;
    private boolean doorOpen;
    private boolean defrosting;
    private int temperatureSetting;

    // Constructor
    public SmartFridge(String deviceId, String name, EnergyMode energyMode, int initialTempSetting) {
        super(deviceId, name, energyMode);
        this.currentTemperatureCelsius = 4.0;
        this.doorOpen = false;
        this.defrosting = false;
        this.temperatureSetting = initialTempSetting;
        this.powerConsumption = 150.0;
    }

    @Override
    public void turnOn() {
        if (!this.isOn) {
            this.isOn = true;
            System.out.println(this.name + " is now ON. Compressor starting...");
        } else {
            System.out.println(this.name + " is already ON.");
        }
    }

    @Override
    public void turnOff() {
        if (this.isOn) {
            this.isOn = false;
            this.defrosting = false;
            System.out.println(this.name + " is now OFF. Compressor stopped.");
        } else {
            System.out.println(this.name + " is already OFF.");
        }
    }

    @Override
    public String getStatus() {
        String baseStatus = this.isOn ? "ON" : "OFF";
        String modeStatus = this.defrosting ? " (DEFROSTING)" : "";
        String doorStatus = this.doorOpen ? " | DOOR OPEN" : "";

        return String.format("%s%s - Temp: %.1f°C (Setting: %d) - Power: %.1f W%s",
                baseStatus,
                modeStatus,
                this.currentTemperatureCelsius,
                this.temperatureSetting,
                getEnergyConsumption(),
                doorStatus);
    }

    @Override
    public double getEnergyConsumption() {
        // Power consumption depends on state (e.g., higher when cooling, lower when
        // idle)
        double baseConsumption = 0.0;

        if (this.isOn) {
            switch (this.energyMode) {
                case HIGH:
                    baseConsumption = 200.0; // High Cooling
                    break;
                case ECO:
                    baseConsumption = 50.0; // Standby/Efficient
                    break;
                default: // NORMAL
                    baseConsumption = 150.0;
            }

            // Add extra consumption if door is open or if it's actively defrosting
            if (this.doorOpen) {
                baseConsumption += 50.0;
            }
            if (this.defrosting) {
                baseConsumption = 300.0;
            }

        } else {
            baseConsumption = 1.0; // Standby power when OFF
        }

        return baseConsumption;
    }

    @Override
    public void executeCommand(String command) {
        // Commands for a fridge
        switch (command.toLowerCase()) {
            case "defrost start":
                if (this.isOn && !this.defrosting) {
                    this.defrosting = true;
                    System.out.println(this.name + ": Starting manual defrost cycle.");
                } else if (!this.isOn) {
                    System.out.println(this.name + " must be ON to start defrost.");
                }
                break;
            case "defrost stop":
                if (this.defrosting) {
                    this.defrosting = false;
                    System.out.println(this.name + ": Defrost cycle stopped.");
                }
                break;
            case "temp up":
                if (this.temperatureSetting < 5) {
                    this.temperatureSetting++;
                    System.out.println(this.name + ": Temperature setting increased to " + this.temperatureSetting);
                }
                break;
            case "temp down":
                if (this.temperatureSetting > 1) {
                    this.temperatureSetting--;
                    System.out.println(this.name + ": Temperature setting decreased to " + this.temperatureSetting);
                }
                break;
            case "open door":
                this.doorOpen = true;
                System.out.println(this.name + ": Door is open.");
                break;
            case "close door":
                this.doorOpen = false;
                System.out.println(this.name + ": Door is closed.");
                break;
            default:
                System.out.println("Unknown command for " + this.name + ": " + command);
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    // Setter method to simulate internal temperature changes
    public void setCurrentTemperatureCelsius(double currentTemperatureCelsius) {
        this.currentTemperatureCelsius = currentTemperatureCelsius;
    }

    // Getter for the temperature setting
    public int getTemperatureSetting() {
        return temperatureSetting;
    }

}