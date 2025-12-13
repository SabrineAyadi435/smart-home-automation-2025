package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

/**
 * SmartMicrowave - A smart microwave with power levels and timer.
 */
public class SmartMicrowave extends SmartDevice implements Controllable, EnergyConsumer {

    private int powerLevel; // 1-10
    private int timerSeconds;
    private boolean cooking;

    public SmartMicrowave(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.powerLevel = 5; // Default medium power
        this.timerSeconds = 0;
        this.cooking = false;
        this.powerConsumption = 1000.0; // 1000W typical microwave
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
            this.isOn = false;
            this.cooking = false;
            this.timerSeconds = 0;
            System.out.println(this.name + " is now OFF.");
        } else {
            System.out.println(this.name + " is already OFF.");
        }
    }

    @Override
    public String getStatus() {
        String baseStatus = this.isOn ? "ON" : "OFF";
        String cookingStatus = this.cooking ? " (COOKING)" : "";
        return String.format("%s%s - Power Level: %d/10 - Timer: %ds - Power: %.1f W",
                baseStatus, cookingStatus, this.powerLevel, this.timerSeconds, getEnergyConsumption());
    }

    @Override
    public double getEnergyConsumption() {
        if (!this.isOn)
            return 1.0; // Standby
        if (!this.cooking)
            return 5.0; // Idle but on

        double baseConsumption = switch (this.energyMode) {
            case HIGH -> 1200.0;
            case ECO -> 600.0;
            default -> 1000.0;
        };

        // Scale by power level
        return baseConsumption * (powerLevel / 10.0);
    }

    @Override
    public void executeCommand(String command) {
        String cmd = command.toLowerCase().trim();

        if (cmd.equals("start") || cmd.equals("cook")) {
            if (this.isOn) {
                this.cooking = true;
                System.out.println(this.name + ": Microwave started cooking.");
            } else {
                System.out.println(this.name + " must be ON to start cooking.");
            }
        } else if (cmd.equals("stop")) {
            this.cooking = false;
            System.out.println(this.name + ": Cooking stopped.");
        } else if (cmd.startsWith("power ")) {
            try {
                int level = Integer.parseInt(cmd.substring(6));
                setPowerLevel(level);
            } catch (NumberFormatException e) {
                System.out.println("Invalid power level for " + this.name);
            }
        } else if (cmd.startsWith("timer ")) {
            try {
                int seconds = Integer.parseInt(cmd.substring(6));
                setTimer(seconds);
            } catch (NumberFormatException e) {
                System.out.println("Invalid timer value for " + this.name);
            }
        } else {
            System.out.println("Unknown command for " + this.name + ": " + command);
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        if (powerLevel < 1 || powerLevel > 10) {
            throw new IllegalArgumentException("Power level must be between 1 and 10");
        }
        this.powerLevel = powerLevel;
        System.out.println(this.name + " power level set to " + powerLevel);
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    public void setTimer(int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Timer cannot be negative");
        }
        this.timerSeconds = seconds;
        System.out.println(this.name + " timer set to " + seconds + " seconds");
    }

    public boolean isCooking() {
        return cooking;
    }

    public void startCooking() {
        if (this.isOn) {
            this.cooking = true;
            System.out.println(this.name + " started cooking.");
        }
    }

    public void stopCooking() {
        this.cooking = false;
        System.out.println(this.name + " stopped cooking.");
    }
}
