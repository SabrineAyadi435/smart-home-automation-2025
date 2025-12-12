package com.devices;

import com.enums.EnergyMode;
import com.interfaces.Controllable;
import com.interfaces.EnergyConsumer;

/**
 * SmartToaster - A smart toaster with browning level control.
 */
public class SmartToaster extends SmartDevice implements Controllable, EnergyConsumer {

    private int browningLevel; // 1-7
    private boolean toasting;
    private int slotCount;

    public SmartToaster(String deviceId, String name, EnergyMode energyMode) {
        super(deviceId, name, energyMode);
        this.browningLevel = 4; // Default medium browning
        this.toasting = false;
        this.slotCount = 2; // Standard 2-slot toaster
        this.powerConsumption = 800.0; // 800W typical toaster
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
            this.toasting = false;
            System.out.println(this.name + " is now OFF.");
        } else {
            System.out.println(this.name + " is already OFF.");
        }
    }

    @Override
    public String getStatus() {
        String baseStatus = this.isOn ? "ON" : "OFF";
        String toastingStatus = this.toasting ? " (TOASTING)" : "";
        return String.format("%s%s - Browning: %d/7 - Slots: %d - Power: %.1f W",
                baseStatus, toastingStatus, this.browningLevel, this.slotCount, getEnergyConsumption());
    }

    @Override
    public double getEnergyConsumption() {
        if (!this.isOn)
            return 0.5; // Standby
        if (!this.toasting)
            return 2.0; // Idle but on

        double baseConsumption = switch (this.energyMode) {
            case HIGH -> 1000.0;
            case ECO -> 600.0;
            default -> 800.0;
        };

        // Scale by browning level
        return baseConsumption * (browningLevel / 7.0);
    }

    @Override
    public void executeCommand(String command) {
        String cmd = command.toLowerCase().trim();

        if (cmd.equals("toast") || cmd.equals("start")) {
            if (this.isOn) {
                this.toasting = true;
                System.out.println(this.name + ": Toasting started.");
            } else {
                System.out.println(this.name + " must be ON to start toasting.");
            }
        } else if (cmd.equals("stop") || cmd.equals("cancel")) {
            this.toasting = false;
            System.out.println(this.name + ": Toasting cancelled.");
        } else if (cmd.startsWith("browning ")) {
            try {
                int level = Integer.parseInt(cmd.substring(9));
                setBrowningLevel(level);
            } catch (NumberFormatException e) {
                System.out.println("Invalid browning level for " + this.name);
            }
        } else {
            System.out.println("Unknown command for " + this.name + ": " + command);
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    public int getBrowningLevel() {
        return browningLevel;
    }

    public void setBrowningLevel(int level) {
        if (level < 1 || level > 7) {
            throw new IllegalArgumentException("Browning level must be between 1 and 7");
        }
        this.browningLevel = level;
        System.out.println(this.name + " browning level set to " + level);
    }

    public boolean isToasting() {
        return toasting;
    }

    public void startToasting() {
        if (this.isOn) {
            this.toasting = true;
            System.out.println(this.name + " started toasting.");
        }
    }

    public void stopToasting() {
        this.toasting = false;
        System.out.println(this.name + " stopped toasting.");
    }

    public int getSlotCount() {
        return slotCount;
    }
}
