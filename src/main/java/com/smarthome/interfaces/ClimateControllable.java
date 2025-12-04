package com.smarthome.interfaces;

public interface ClimateControllable extends Controllable {
    void setTemperature(double temperature);
    double getCurrentTemperature();
    double getTargetTemperature();
    void setMode(String mode);
    String getMode();
    double getEnergyConsumption();
    void setEcoMode(boolean enabled);
    boolean isEcoModeActive();
    double getMinTemperature();
    double getMaxTemperature();
    boolean isActive();
    String getLocation();
}