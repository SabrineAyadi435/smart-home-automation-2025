# Smart Home Security System Integration

## Overview

The security system has been successfully integrated into your smart home automation project. It provides comprehensive security monitoring with automated responses to various events.

## Architecture

### SecurityController

Located at: `com.controller.SecurityController`

The SecurityController manages all security-related devices and implements automation rules for home security.

**Key Features:**

- Multiple security modes (DISARMED, ARMED, NIGHT_MODE, AWAY_MODE)
- Automated threat detection and response
- Event logging and monitoring
- Energy consumption tracking for security devices

### HomeController Integration

Located at: `com.controller.HomeController`

The HomeController now includes a SecurityController instance, providing unified control over both general home automation and security systems.

## Security Modes

### 1. DISARMED

- All sensors inactive (except smoke detectors)
- No automated responses
- Use when home and active

### 2. ARMED

- All sensors active
- Motion detection triggers intruder alert
- Cameras start recording automatically
- Alarm sounds on breach

### 3. NIGHT_MODE

- Motion and door/window sensors active
- Door/window opening between 00:00-06:00 triggers alarm
- Lights turn on automatically on breach
- Suitable for sleeping hours

### 4. AWAY_MODE

- All sensors at maximum sensitivity
- Any motion triggers full alarm response
- All cameras recording
- Use when leaving home

## Automation Rules Implemented

### Rule 1: Intruder Detection

**Trigger:** Motion detected while system is ARMED or in AWAY_MODE
**Response:**

- Trigger main alarm siren
- Start recording on all security cameras
- Log security breach event
- Notify owner (simulated)

### Rule 2: Night Security Breach

**Trigger:** Door/window opened between 00:00-06:00 while system is not DISARMED
**Response:**

- Trigger alarm
- Turn on all lights
- Log breach event

### Rule 3: Fire Emergency

**Trigger:** Smoke detected by any smoke detector
**Response:**

- Trigger alarm at maximum volume
- Unlock all doors for evacuation
- Notify fire department (simulated)
- Override all other security modes

### Rule 4: Battery Monitoring

**Trigger:** Periodic check (automated)
**Response:**

- Check smoke detector battery levels
- Alert if battery below 20%

### Rule 5: Auto-Lock

**Trigger:** Scheduled time (e.g., 23:00-06:00)
**Response:**

- Automatically lock all doors
- Can be configured per door

## Security Devices

### AlarmSiren

- Volume control (1-10)
- Duration settings
- Energy-efficient standby mode

### SecurityCamera

- Recording control
- Night vision toggle
- Scheduled recording
- Live feed access

### MotionSensor

- Adjustable sensitivity (1-10)
- Detection range configuration
- Trigger counting

### DoorWindowSensor

- Open/closed state monitoring
- Location tracking
- Access logging

### DoorLock

- Remote lock/unlock
- Auto-lock scheduling
- Access log tracking

### SmokeDetector

- Smoke detection
- Battery monitoring
- Always-on for safety

## Usage Examples

### Basic Setup

```java
// Create home and controller
Home myHome = new Home("My Smart Home");
HomeController controller = new HomeController(myHome);

// Add security devices
SecurityCamera camera = new SecurityCamera("cam-001", "Front Camera", "1080p", 90, EnergyMode.NORMAL);
MotionSensor sensor = new MotionSensor("motion-001", "Living Room", 10, EnergyMode.NORMAL);
DoorLock lock = new DoorLock("lock-001", "Front Door", EnergyMode.NORMAL);

controller.addSecurityDevice(camera);
controller.addSecurityDevice(sensor);
controller.addSecurityDevice(lock);
```

### Arming the System

```java
// Arm when leaving home
controller.setAwayMode();

// Or use night mode when sleeping
controller.setNightMode();

// Disarm when returning
controller.disarmSecuritySystem();
```

### Handling Security Events

```java
try {
    sensor.checkMotion();
} catch (SecurityBreachException e) {
    // Automatically handled by controller
    controller.handleSecurityEvent("motion-001");
}
```

### Monitoring

```java
// View security status
controller.displaySecurityStatus();

// View all security devices
controller.displayAllSecurityDevices();

// Check energy consumption
controller.displayEnergyConsumption();
```

## Running the Demo

Execute the Main class to see all scenarios in action:

```bash
cd smart-home-automation-2025/smarthome
mvn compile
mvn exec:java -Dexec.mainClass="com.Main"
```

The demo runs through 5 scenarios:

1. Leaving home (Away Mode)
2. Intruder detection
3. Coming home (Auto-disarm)
4. Bedtime (Night Mode)
5. Fire emergency

## Energy Efficiency

All security devices implement energy-saving modes:

- Cameras reduce resolution in ECO mode
- Sensors reduce power consumption when inactive
- Alarms use minimal standby power
- Total energy consumption tracked and reported

## Future Enhancements

Potential additions:

- Mobile app integration for remote monitoring
- SMS/Email notifications
- Integration with emergency services
- AI-powered threat detection
- Facial recognition for cameras
- Voice control integration
- Geofencing for automatic arm/disarm
