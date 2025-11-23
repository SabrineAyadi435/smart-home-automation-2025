package com.smarthome.model;

import com.smarthome.interfaces.Controllable;

public class SmartLock extends SmartDevice implements Controllable {
    private boolean locked;

    public SmartLock(String deviceId, String name) {
        super(deviceId, name);
        this.locked = true;
        this.isOn = true;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " activated");
    }

    @Override
    public void turnOff() {
        this.isOn = false;
        System.out.println(name + " deactivated");
    }

    @Override
    public String getStatus() {
        return isOn ? (locked ? "LOCKED" : "UNLOCKED") : "INACTIVE";
    }

    public void lock() {
        this.locked = true;
        System.out.println(name + " locked");
    }

    public void unlock() {
        this.locked = false;
        System.out.println(name + " unlocked");
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public void executeCommand(String command) {
        if (command.equalsIgnoreCase("LOCK")) {
            lock();
        } else if (command.equalsIgnoreCase("UNLOCK")) {
            unlock();
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }
}
