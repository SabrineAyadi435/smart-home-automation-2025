# Smart Home Automation Simulator

A Java-based object-oriented project simulating a smart home system with various devices, automation rules, energy management, and a JavaFX graphical interface.

## Features

- **Smart Devices**: Light, Thermostat, SmartTV, MotionSensor
- **OOP Principles**: Abstract classes, interfaces, inheritance, polymorphism
- **Automation Engine**: IF-THEN rule system
- **Energy Management**: Real-time energy consumption dashboard
- **JavaFX GUI**: Interactive graphical interface with device controls
- **Exception Handling**: Custom exceptions for error management

## Project Structure

```
src/main/java/com/smarthome/
├── Main.java (Console version)
├── gui/
│   └── SmartHomeGUI.java (JavaFX GUI)
├── model/
│   ├── SmartDevice.java (abstract)
│   ├── Light.java
│   ├── Thermostat.java
│   ├── SmartTV.java
│   ├── MotionSensor.java
│   ├── Room.java
│   └── Home.java
├── interfaces/
│   ├── Controllable.java
│   ├── EnergyConsumer.java
│   └── Schedulable.java
├── controller/
│   └── HomeController.java
├── automation/
│   ├── AutomationEngine.java
│   └── Rule.java
└── exceptions/
    └── DeviceNotFoundException.java
```

## Prerequisites

- **Java JDK 11 or higher** (JavaFX is included in JDK 11+)
- For JDK 11+, you may need to install JavaFX separately

### Installing JavaFX (if needed)

**Option 1: Using JavaFX SDK**
1. Download JavaFX SDK from: https://gluonhq.com/products/javafx/
2. Extract to a location (e.g., `/path/to/javafx-sdk`)

**Option 2: Using Maven (recommended)**
Create a `pom.xml` file in the project root (see below)

## How to Run

### Method 1: Console Version (No GUI)

```bash
# Compile
javac -d bin src/main/java/com/smarthome/**/*.java

# Run
java -cp bin com.smarthome.Main
```

### Method 2: JavaFX GUI Version

**If JavaFX is in your JDK:**
```bash
# Compile
javac -d bin src/main/java/com/smarthome/**/*.java

# Run
java -cp bin com.smarthome.gui.SmartHomeGUI
```

**If using separate JavaFX SDK:**
```bash
# Compile
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
  -d bin src/main/java/com/smarthome/**/*.java

# Run
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
  -cp bin com.smarthome.gui.SmartHomeGUI
```

**Replace `/path/to/javafx-sdk` with your actual JavaFX SDK path**

### Method 3: Using Maven (Recommended)

If you have Maven installed:

```bash
# Compile and run
mvn clean javafx:run
```

## GUI Features

The JavaFX interface includes:
- **Device Control Panel**: View and control all devices by room
- **Energy Dashboard**: Real-time energy consumption monitoring with color-coded alerts
- **Automation Panel**: View and trigger automation rules
- **Interactive Controls**: Sliders for brightness, toggle buttons for on/off
- **Bulk Operations**: Turn all devices on/off with one click

## OOP Concepts Demonstrated

- **Abstraction**: SmartDevice abstract class
- **Inheritance**: All devices extend SmartDevice
- **Polymorphism**: Method overriding in device classes
- **Interfaces**: Controllable, EnergyConsumer, Schedulable
- **Encapsulation**: Private fields with getters/setters
- **Exception Handling**: DeviceNotFoundException
- **Collections**: ArrayList, HashMap for managing devices

## Troubleshooting

### JavaFX Not Found Error
If you get "Error: JavaFX runtime components are missing":
1. Install JavaFX SDK from https://gluonhq.com/products/javafx/
2. Use the `--module-path` and `--add-modules` flags when running
3. Or use Maven with the provided `pom.xml`

### Compilation Errors
- Ensure you're using JDK 11 or higher
- Check that all source files are in the correct package structure
- Verify JavaFX is properly installed

## Example Usage (Console)

```java
// Create home and devices
Home home = new Home("My Smart Home");
Light light = new Light("L1", "Living Room Light", 100);
Thermostat thermostat = new Thermostat("T1", "Main Thermostat", 22.0);

// Add to room
Room livingRoom = new Room("Living Room");
livingRoom.addDevice(light);
home.addRoom(livingRoom);

// Control devices
HomeController controller = new HomeController(home);
controller.turnOnDevice("L1");
controller.displayEnergyConsumption();

// Automation
AutomationEngine engine = new AutomationEngine();
Rule rule = new Rule("Auto Light", 
    () -> sensor.isMotionDetected(),
    () -> light.turnOn()
);
engine.addRule(rule);
```

## License

This project is for educational purposes.
