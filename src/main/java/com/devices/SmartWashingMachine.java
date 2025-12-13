package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

/**
 * SmartWashingMachine - A smart washing machine with multiple cycles and
 * temperature control.
 */
public class SmartWashingMachine extends SmartDevice implements Controllable, EnergyConsumer {

    private int temperature; // Water temperature in Celsius
    private int spinSpeed; // RPM
    private String cycleType; // e.g., "Cotton", "Quick", "Delicate"
    private boolean isRunning;

    public SmartWashingMachine(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.temperature = 40; // Default 40°C
        this.spinSpeed = 800; // Default 800 RPM
        this.cycleType = "Cotton";
        this.isRunning = false;
        this.powerConsumption = 0.0; // Will be calculated dynamically
    }

    @Override
    public void turnOn() {
        if (!this.isOn) {
            this.isOn = true;
            System.out.println(this.name + " is now ON.");
        } else {
            System.out.println(this.name + " is already ON.");
        }
    }

    @Override
    public void turnOff() {
        if (this.isOn) {
            if (this.isRunning) {
                stopWash();
            }
            this.isOn = false;
            System.out.println(this.name + " is now OFF.");
        } else {
            System.out.println(this.name + " is already OFF.");
        }
    }

    @Override
    public String getStatus() {
        String baseStatus = this.isOn ? "ON" : "OFF";
        String runStatus = this.isRunning ? " (RUNNING)" : "";
        return String.format("%s%s - Cycle: %s - Temp: %d°C - Spin: %d RPM - Power: %.1f W",
                baseStatus, runStatus, this.cycleType, this.temperature, this.spinSpeed, getEnergyConsumption());
    }

    @Override
    public double getEnergyConsumption() {
        if (!this.isOn) {
            return 0.5; // Standby power
        }

        if (!this.isRunning) {
            return 5.0; // Idle but on
        }

        // Base consumption depends on cycle type
        double baseLoad = switch (this.cycleType) {
            case "Cotton" -> 500.0;
            case "Quick" -> 300.0;
            case "Delicate" -> 200.0;
            case "Heavy Duty" -> 800.0;
            default -> 400.0;
        };

        // Heating water is energy intensive
        double heatingLoad = (this.temperature - 20) * 20.0;
        if (heatingLoad < 0)
            heatingLoad = 0;

        // Spin speed factor
        double spinLoad = this.spinSpeed * 0.5;

        double total = baseLoad + heatingLoad + spinLoad;

        // Apply energy mode factor
        return switch (this.energyMode) {
            case ECO -> total * 0.8;
            case HIGH -> total * 1.2;
            default -> total;
        };
    }

    @Override
    public void executeCommand(String command) {
        String cmd = command.toLowerCase().trim();

        if (cmd.equals("start")) {
            startWash();
        } else if (cmd.equals("stop")) {
            stopWash();
        } else if (cmd.startsWith("temp ")) {
            try {
                int temp = Integer.parseInt(cmd.split(" ")[1]);
                setTemperature(temp);
            } catch (Exception e) {
                System.out.println("Invalid temperature command.");
            }
        } else if (cmd.startsWith("spin ")) {
            try {
                int speed = Integer.parseInt(cmd.split(" ")[1]);
                setSpinSpeed(speed);
            } catch (Exception e) {
                System.out.println("Invalid spin speed command.");
            }
        } else if (cmd.startsWith("cycle ")) {
            String type = cmd.substring(6).trim();
            setCycleType(type);
        } else {
            System.out.println("Unknown command for " + this.name);
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    public void startWash() {
        if (this.isOn) {
            if (!this.isRunning) {
                this.isRunning = true;
                System.out.println(this.name + " started washing cycle: " + this.cycleType);
            } else {
                System.out.println(this.name + " is already running.");
            }
        } else {
            System.out.println(this.name + " must be ON to start washing.");
        }
    }

    public void stopWash() {
        if (this.isRunning) {
            this.isRunning = false;
            System.out.println(this.name + " stopped washing.");
        } else {
            System.out.println(this.name + " is not running.");
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        if (temperature >= 20 && temperature <= 90) {
            this.temperature = temperature;
            System.out.println(this.name + " temperature set to " + temperature + "°C");
        } else {
            System.out.println("Invalid temperature. Range: 20-90°C");
        }
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        if (spinSpeed >= 400 && spinSpeed <= 1600) {
            this.spinSpeed = spinSpeed;
            System.out.println(this.name + " spin speed set to " + spinSpeed + " RPM");
        } else {
            System.out.println("Invalid spin speed. Range: 400-1600 RPM");
        }
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
        System.out.println(this.name + " cycle set to " + cycleType);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
