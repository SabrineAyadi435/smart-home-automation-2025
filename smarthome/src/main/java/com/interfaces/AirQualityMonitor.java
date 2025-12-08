package com.interfaces;

public interface AirQualityMonitor {
    double getAirQualityIndex();
    double getCO2Level();
    double getHumidity();
    boolean isAirQualityGood();
    void setAirQualityAlert(double threshold);
    String getLocation();
    boolean needsVentilation();
}