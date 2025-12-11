# Backend Architecture

This document provides an overview of the backend architecture for the Smart Home Automation project. The backend is designed to be modular, extensible, and interface-driven.

## Core Components

### Devices Package (`com.devices`)
The `com.devices` package contains the core logic for all smart devices in the system. All devices extend the abstract base class `SmartDevice`.

**Key Classes:**
- **SmartDevice**: The abstract base class for all devices. Handles common properties like ID, name, and connection status.
- **Light**: Represents smart lighting.
- **Thermostat**: Controls temperature (likely related to `AC` or `ClimateController`).
- **DoorLock**: Manages door security.
- **SecurityCamera**: Handles video feeds and recording status.
- **MotionSensor**: Detects motion events.
- **SmartPlug**: Monitors energy usage of attached appliances.
- **SmartTV**, **SmartFridge**, **SmartMirror**, **SmartFaucet**, **Speaker**: Various other smart home appliances.
- **SmokeDetector**, **AlarmSiren**: Safety and security devices.

### Automation Package (`com.automation`)
The `com.automation` package manages the intelligence of the smart home.

- **AutomationEngine**: The core service that evaluates rules and triggers actions.
- **Rule**: Defines a condition and an action (e.g., "If motion detected, turn on lights").

### Interfaces Package (`com.interfaces`)
Interfaces define the capabilities of devices, allowing for polymorphic behavior.

- **Controllable**: For devices that can be remotely controlled (on/off, settings).
- **EnergyConsumer**: For devices that consume electricity. Allows the system to track total energy usage.
- **Schedulable**: For devices that can operate on a schedule.
- **AirQualityMonitor**: For devices that report air quality metrics.
- **WaterConsumer**: For devices that track water usage (e.g., `SmartFaucet`).

## Architecture Principles

1.  **Inheritance**: All devices share a common lineage from `SmartDevice`, ensuring they can be managed in a unified list.
2.  **Interfaces**: Capabilities are added via interfaces. For example, a `SmartTV` might implement `Controllable` and `EnergyConsumer`.
3.  **Event-Driven**: The automation engine listens for state changes (events) from devices to trigger rules.
