package com.interfaces;

import com.enums.EnergyMode;

public interface EnergyConsumer {
    double getEnergyConsumption();
    void setEnergyMode(EnergyMode mode);
}