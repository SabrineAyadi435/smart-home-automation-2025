# Smart Home Automation Simulator

A Java-based object-oriented project simulating a smart home system with various devices, automation rules, energy management, and an interactive JavaFX floor plan interface.

## ✨ New Features - Floor Plan UI

🏠 **Realistic House Structure** - Actual house with roof, walls, doors, and windows  
🏗️ **Architectural Details** - Triangular roof with chimney, interior walls with doorways  
📱 **8 Device Types** - Light, Thermostat, TV, Plug, Lock, Camera, Sensor, Speaker  
➕ **Dynamic Room/Device Creation** - Add rooms and devices on-the-fly  
🎨 **Customizable Room Colors** - Choose from 10 color palettes  
⚡ **Real-time Energy Monitoring** - Color-coded consumption dashboard  
🎮 **Device-Specific Controls** - Sliders, toggles, and action buttons  
💅 **Modern UI** - Hover effects, tooltips, smooth transitions

## Features

- **Smart Devices**: Light, Thermostat, SmartTV, SmartPlug, SmartLock, Camera, MotionSensor, Speaker
- **OOP Principles**: Abstract classes, interfaces, inheritance, polymorphism
- **Automation Engine**: IF-THEN rule system
- **Energy Management**: Real-time energy consumption dashboard
- **Interactive Floor Plan GUI**: Visual house layout with clickable room tiles
- **Dynamic Device Management**: Add rooms and devices through the UI
- **Exception Handling**: Custom exceptions for error management

## Project Structure

```
src/main/java/com/smarthome/
├── Main.java (Console version)
├── gui/
│   ├── FloorPlanGUI.java (NEW - Interactive floor plan UI)
│   └── SmartHomeGUI.java (Original list-based GUI)
├── model/
│   ├── SmartDevice.java (abstract)
│   ├── Light.java
│   ├── Thermostat.java
│   ├── SmartTV.java
│   ├── SmartPlug.java (NEW)
│   ├── SmartLock.java (NEW)
│   ├── Camera.java (NEW)
│   ├── Speaker.java (NEW)
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

src/main/resources/
├── floorplan.css (NEW - Floor plan styling)
└── style.css (Original styling)
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

## Quick Start

### 🚀 Recommended: Using Maven

```bash
cd JAVA_Project
mvn clean compile
mvn javafx:run
```

### 🎯 Using the Launcher Script

```bash
chmod +x run-gui.sh
./run-gui.sh
# Select option 1 for Floor Plan UI (recommended)
# Select option 2 for original List View UI
```

### Alternative Methods

#### Method 1: Console Version (No GUI)

```bash
javac -d bin src/main/java/com/smarthome/**/*.java
java -cp bin com.smarthome.Main
```

#### Method 2: Run Specific GUI

**Floor Plan UI (New):**
```bash
mvn exec:java -Dexec.mainClass="com.smarthome.gui.FloorPlanGUI"
```

**List View UI (Original):**
```bash
mvn exec:java -Dexec.mainClass="com.smarthome.gui.SmartHomeGUI"
```

#### Method 3: Manual Compilation with JavaFX

```bash
# Compile
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
  -d bin src/main/java/com/smarthome/**/*.java

# Run Floor Plan UI
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
  -cp bin com.smarthome.gui.FloorPlanGUI
```

## GUI Features

### Floor Plan UI (New - Recommended)

The interactive floor plan interface includes:

- **🏠 Visual Floor Plan**: House layout with room tiles in a grid
- **🎨 Room Customization**: Choose from 10 color palettes for each room
- **➕ Add Rooms**: Create new rooms dynamically with custom names and colors
- **📱 Device Icons**: Visual device representation with emojis and status indicators
- **🎮 Device Controls**: Click any device to open its control panel
  - **Lights**: Brightness slider (0-100%)
  - **Thermostat**: Temperature control (10-35°C)
  - **Smart TV**: Volume adjustment
  - **Smart Plug**: Power toggle
  - **Smart Lock**: Lock/unlock button
  - **Camera**: Recording controls
  - **Motion Sensor**: Motion simulation
  - **Speaker**: Play/pause and volume
- **⚡ Energy Dashboard**: Real-time consumption with color coding
  - Green: < 25 kWh
  - Orange: 25-50 kWh
  - Red: > 50 kWh
- **💡 Hover Effects**: Tooltips, highlights, and smooth transitions
- **🔄 Global Controls**: Turn all devices on/off, refresh display

### List View UI (Original)

The original list-based interface includes:
- **Device Control Panel**: View and control all devices by room
- **Energy Dashboard**: Real-time energy consumption monitoring
- **Automation Panel**: View and trigger automation rules
- **Interactive Controls**: Sliders for brightness, toggle buttons
- **Bulk Operations**: Turn all devices on/off with one click

## 📚 Documentation

- **[FLOORPLAN_GUIDE.md](FLOORPLAN_GUIDE.md)** - Complete floor plan UI user guide
- **[TEST_PLAN.md](TEST_PLAN.md)** - Comprehensive test cases and manual testing steps
- **[SETUP.md](SETUP.md)** - Detailed setup instructions
- **[TODO.MD](TODO.MD)** - Future enhancements and roadmap

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
