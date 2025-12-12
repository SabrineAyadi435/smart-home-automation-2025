package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

/**
 * SmartCooker - A smart cooker/stove with multiple burners and temperature
 * control.
 */
public class SmartCooker extends SmartDevice implements Controllable, EnergyConsumer {

    private int temperatureLevel; // 1-10
    private int activeBurners; // Number of active burners
    private int totalBurners;
    private boolean cooking;

    public SmartCooker(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.temperatureLevel = 5; // Default medium heat
        this.activeBurners = 0;
        this.totalBurners = 4; // Standard 4-burner stove
        this.cooking = false;
        this.powerConsumption = 2000.0; // 2000W typical electric cooker
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
            this.activeBurners = 0;
            System.out.println(this.name + " is now OFF. All burners deactivated.");
        } else {
            System.out.println(this.name + " is already OFF.");
        }
    }

    @Override
    public String getStatus() {
        String baseStatus = this.isOn ? "ON" : "OFF";
        String cookingStatus = this.cooking ? " (COOKING)" : "";
        return String.format("%s%s - Heat: %d/10 - Burners: %d/%d active - Power: %.1f W",
                baseStatus, cookingStatus, this.temperatureLevel,
                this.activeBurners, this.totalBurners, getEnergyConsumption());
    }

    @Override
    public double getEnergyConsumption() {
        if (!this.isOn)
            return 1.0; // Standby
        if (this.activeBurners == 0)
            return 5.0; // Idle but on

        double baseConsumption = switch (this.energyMode) {
            case HIGH -> 2500.0;
            case ECO -> 1200.0;
            default -> 2000.0;
        };

        // Scale by temperature level and active burners
        double burnerRatio = (double) activeBurners / totalBurners;
        double tempRatio = temperatureLevel / 10.0;
        return baseConsumption * burnerRatio * tempRatio;
    }

    @Override
    public void executeCommand(String command) {
        String cmd = command.toLowerCase().trim();

        if (cmd.equals("cook") || cmd.equals("start")) {
            if (this.isOn) {
                this.cooking = true;
                if (this.activeBurners == 0) {
                    this.activeBurners = 1; // Enable at least one burner
                }
                System.out.println(this.name + ": Cooking started.");
            } else {
                System.out.println(this.name + " must be ON to start cooking.");
            }
        } else if (cmd.equals("stop")) {
            this.cooking = false;
            System.out.println(this.name + ": Cooking stopped.");
        } else if (cmd.startsWith("heat ") || cmd.startsWith("temp ")) {
            try {
                int level = Integer.parseInt(cmd.split(" ")[1]);
                setTemperatureLevel(level);
            } catch (Exception e) {
                System.out.println("Invalid temperature level for " + this.name);
            }
        } else if (cmd.startsWith("burners ")) {
            try {
                int count = Integer.parseInt(cmd.substring(8));
                setActiveBurners(count);
            } catch (NumberFormatException e) {
                System.out.println("Invalid burner count for " + this.name);
            }
        } else {
            System.out.println("Unknown command for " + this.name + ": " + command);
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    public int getTemperatureLevel() {
        return temperatureLevel;
    }

    public void setTemperatureLevel(int level) {
        if (level < 1 || level > 10) {
            throw new IllegalArgumentException("Temperature level must be between 1 and 10");
        }
        this.temperatureLevel = level;
        System.out.println(this.name + " heat level set to " + level);
    }

    public int getActiveBurners() {
        return activeBurners;
    }

    public void setActiveBurners(int count) {
        if (count < 0 || count > totalBurners) {
            throw new IllegalArgumentException("Active burners must be between 0 and " + totalBurners);
        }
        this.activeBurners = count;
        this.cooking = count > 0;
        System.out.println(this.name + " active burners set to " + count);
    }

    public int getTotalBurners() {
        return totalBurners;
    }

    public boolean isCooking() {
        return cooking;
    }

    public void startCooking() {
        if (this.isOn) {
            this.cooking = true;
            if (this.activeBurners == 0) {
                this.activeBurners = 1;
            }
            System.out.println(this.name + " started cooking.");
        }
    }

    public void stopCooking() {
        this.cooking = false;
        System.out.println(this.name + " stopped cooking.");
    }
}
