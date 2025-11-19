package com.smarthome.interfaces;

public interface Controllable {
    void executeCommand(String command);
    boolean isControllable();
}
