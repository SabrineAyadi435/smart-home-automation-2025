# 🏠 Smart Home Automation 2025

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.1-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A comprehensive JavaFX application for simulating and managing a modern smart home environment.**

</div>

---

## 📋 Table of Contents

- [Features](#-features)
- [Screenshots](#-screenshots)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Project Structure](#-project-structure)
- [Architecture](#-architecture)
- [Supported Devices](#-supported-devices)
- [Security System](#-security-system)
- [Special Features](#-special-features)
- [Documentation](#-documentation)
- [Contributing](#-contributing)

---

## ✨ Features

### 🎮 Device Management
- Control a wide variety of smart devices including lights, thermostats, locks, cameras, and kitchen appliances
- Add, remove, and configure devices per room
- Real-time device status monitoring

### 🤖 Automation Engine
- Define rules to automate device actions based on triggers
- Motion detection triggers, scheduled actions, and event-driven automation
- Custom rule creation for personalized home automation

### ⚡ Energy Monitoring
- Track real-time energy consumption of all connected devices
- Historical energy usage data
- Power consumption dashboard with live statistics

### 🔒 Security System
- Multiple security modes: Disarmed, Armed, Night Mode, Away Mode
- Motion detection and intrusion alerts
- Security camera management with recording controls
- Smoke detector integration with fire emergency protocols
- Door/window sensor monitoring

### 🏡 Room Management
- Organize devices by room for easy access
- Interactive floor plan visualization
- Dynamic house structure that grows with rooms

### 🎨 Interactive Dashboard
- Beautiful and responsive JavaFX UI
- Real-time notifications and alerts
- Time simulator for testing automation rules
- Toast notifications for important events

### 🕌 Islamic Features
- Prayer time integration
- Qibla direction service
- Islamic calendar support
- Quran API integration for Smart Mirror

---

## 🚀 Getting Started

### Prerequisites

Before running the application, ensure you have the following installed:

| Requirement | Version | Download Link |
|-------------|---------|---------------|
| **Java Development Kit (JDK)** | 21 or higher | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/) |
| **Apache Maven** | 3.8+ | [Maven Downloads](https://maven.apache.org/download.cgi) |
| **Git** | Any recent version | [Git Downloads](https://git-scm.com/downloads) |

> **Note:** JavaFX 21.0.1 is included as a Maven dependency and will be downloaded automatically.

### Installation

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd smart-home-automation-2025/smarthome
   ```

2. **Verify Java version:**
   ```bash
   java -version
   # Should output: java version "21" or higher
   ```

3. **Verify Maven installation:**
   ```bash
   mvn -version
   ```

### Running the Application

#### Option 1: Using Maven JavaFX Plugin (Recommended)

This is the easiest way to run the application with all JavaFX dependencies properly configured:

```bash
cd smart-home-automation-2025/smarthome
mvn clean javafx:run
```

#### Option 2: Using Maven Exec Plugin

```bash
cd smart-home-automation-2025/smarthome
mvn clean compile exec:java -Dexec.mainClass="com.Main"
```

#### Option 3: Build and Run JAR

```bash
# Build the project
mvn clean package

# Run the JAR (ensure JavaFX is in the module path)
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar target/smarthome-1.0-SNAPSHOT.jar
```

#### Running the Console Demo

To run the console-based security system demo:

```bash
mvn compile exec:java -Dexec.mainClass="com.Main"
```

---

## 📁 Project Structure

```
smarthome/
├── pom.xml                          # Maven build configuration
├── README.md                        # This file
├── docs/                            # Additional documentation
│   ├── BACKEND.md                   # Backend architecture documentation
│   ├── UI.md                        # UI architecture documentation
│   └── SECURITY_SYSTEM_README.md    # Security system details
└── src/
    └── main/
        ├── java/com/
        │   ├── Main.java            # Application entry point (console demo)
        │   ├── automation/          # Automation engine and rules
        │   │   ├── AutomationEngine.java
        │   │   └── Rule.java
        │   ├── controller/          # Business logic controllers
        │   │   ├── HomeController.java
        │   │   └── SecurityController.java
        │   ├── devices/             # Smart device implementations
        │   │   ├── SmartDevice.java      # Base class
        │   │   ├── Light.java
        │   │   ├── SmartTV.java
        │   │   ├── SmartFridge.java
        │   │   ├── SmartMicrowave.java
        │   │   ├── SmartCooker.java
        │   │   ├── SmartToaster.java
        │   │   └── ... (19 device types)
        │   ├── enums/               # Enumerations
        │   ├── exceptions/          # Custom exceptions
        │   ├── home/                # Home model
        │   ├── interfaces/          # Device capability interfaces
        │   │   ├── Controllable.java
        │   │   ├── EnergyConsumer.java
        │   │   ├── Schedulable.java
        │   │   ├── AirQualityMonitor.java
        │   │   └── WaterConsumer.java
        │   ├── room/                # Room model
        │   ├── services/            # External services
        │   │   ├── CalendarService.java
        │   │   ├── IslamicCalendarService.java
        │   │   ├── QiblaService.java
        │   │   └── QuranAPIService.java
        │   ├── ui/                  # JavaFX UI components
        │   │   ├── DashboardApplication.java  # Main UI entry point
        │   │   ├── controllers/     # FXML controllers
        │   │   ├── components/      # Reusable UI components
        │   │   ├── dialogs/         # Dialog windows
        │   │   ├── models/          # UI models
        │   │   └── utils/           # UI utilities
        │   └── utils/               # General utilities
        │       └── TimeSimulator.java
        └── resources/com/ui/
            ├── css/                 # Stylesheets
            │   └── main.css
            ├── fxml/                # JavaFX layout files
            │   ├── dashboard.fxml
            │   ├── home-view.fxml
            │   ├── climate-control.fxml
            │   ├── security-panel.fxml
            │   ├── monitoring-panel.fxml
            │   ├── room-panel.fxml
            │   └── notification-panel.fxml
            ├── icons/               # UI icons
            └── images/              # UI images
```

---

## 🏗️ Architecture

### Design Patterns

The application follows industry-standard design patterns:

| Pattern | Implementation |
|---------|----------------|
| **MVC (Model-View-Controller)** | Separation of UI (FXML/Controllers), business logic, and data models |
| **Factory Pattern** | `DeviceControlFactory` for creating device-specific UI controls |
| **Observer Pattern** | Event-driven automation and UI updates |
| **Strategy Pattern** | Different security modes with varying behaviors |

### Core Components

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   UI Layer      │────▶│  Controllers    │────▶│    Models       │
│   (JavaFX)      │     │                 │     │   (Devices)     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                               │
                               ▼
                        ┌─────────────────┐
                        │   Automation    │
                        │    Engine       │
                        └─────────────────┘
```

### Interface-Driven Design

Devices implement capability interfaces for polymorphic behavior:

- **`Controllable`** - Remote control capabilities (on/off, settings)
- **`EnergyConsumer`** - Energy consumption tracking
- **`Schedulable`** - Schedule-based operations
- **`AirQualityMonitor`** - Air quality metrics reporting
- **`WaterConsumer`** - Water usage tracking

---

## 📱 Supported Devices

### Lighting & Ambiance
| Device | Features |
|--------|----------|
| **Light** | On/Off, Brightness, Color temperature |
| **SmartPlug** | Power monitoring, On/Off control |

### Climate Control
| Device | Features |
|--------|----------|
| **AC** | Temperature, Mode, Fan speed, Energy tracking |
| **AirQualitySensor** | CO2, PM2.5, Humidity monitoring |

### Entertainment
| Device | Features |
|--------|----------|
| **SmartTV** | Power, Volume, Channel, Source selection |
| **Speaker** | Volume, Playback, Multi-room audio |
| **SmartMirror** | Display widgets, Prayer times, Weather, Quran verses |

### Kitchen Appliances
| Device | Features |
|--------|----------|
| **SmartFridge** | Temperature, Door status, Inventory tracking |
| **SmartMicrowave** | Power level, Timer, Presets |
| **SmartCooker** | Temperature, Timer, Cooking modes |
| **SmartToaster** | Browning level, Timer |

### Security Devices
| Device | Features |
|--------|----------|
| **SecurityCamera** | Recording, Night vision, Motion detection |
| **MotionSensor** | Sensitivity adjustment, Trigger counting |
| **DoorLock** | Lock/Unlock, Auto-lock scheduling, Access logs |
| **DoorWindowSensor** | Open/Closed state, Location tracking |
| **AlarmSiren** | Volume control, Duration settings |
| **SmokeDetector** | Smoke detection, Battery monitoring |

### Bathroom
| Device | Features |
|--------|----------|
| **SmartFaucet** | Water usage tracking, Temperature control |

---

## 🔒 Security System

The security system provides comprehensive home protection with multiple operational modes:

### Security Modes

| Mode | Description | Use Case |
|------|-------------|----------|
| **DISARMED** | All sensors inactive (except smoke detectors) | When home and active |
| **ARMED** | All sensors active, motion triggers alerts | General security |
| **NIGHT_MODE** | Motion & door sensors active, 00:00-06:00 triggers | Sleeping hours |
| **AWAY_MODE** | Maximum sensitivity, full alarm response | Leaving home |

### Automation Rules

1. **Intruder Detection** - Motion triggers alarm, cameras record, owner notified
2. **Night Security Breach** - Door/window opens at night, lights turn on, alarm triggers
3. **Fire Emergency** - Smoke detected, all doors unlock, alarm at max volume
4. **Battery Monitoring** - Alerts when smoke detector battery < 20%
5. **Auto-Lock** - Doors automatically lock at scheduled times

For detailed security system documentation, see [docs/SECURITY_SYSTEM_README.md](docs/SECURITY_SYSTEM_README.md).

---

## 🌟 Special Features

### Time Simulator
Test automation rules by simulating time progression:
- Adjust simulation speed
- View current simulated time
- Watch real-time power consumption updates

### Islamic Integration
The Smart Mirror device includes:
- **Prayer Times** - Daily prayer schedule based on location
- **Qibla Direction** - Compass heading to Mecca
- **Islamic Calendar** - Hijri date display
- **Quran Verses** - Daily verse from Quran API

### Energy Dashboard
- Real-time power consumption monitoring
- Per-device energy breakdown
- Historical usage trends
- Energy-saving recommendations

---

## 📚 Documentation

Detailed documentation is available in the `docs/` directory:

| Document | Description |
|----------|-------------|
| [BACKEND.md](docs/BACKEND.md) | Device hierarchy, automation engine, core interfaces |
| [UI.md](docs/UI.md) | JavaFX controllers, FXML views, custom components |
| [SECURITY_SYSTEM_README.md](docs/SECURITY_SYSTEM_README.md) | Security modes, automation rules, usage examples |

---

## 🛠️ Development

### Building the Project

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn package

# Generate documentation
mvn javadoc:javadoc
```

### Adding New Devices

1. Create a new class in `com.devices` extending `SmartDevice`
2. Implement relevant interfaces (`Controllable`, `EnergyConsumer`, etc.)
3. Add device type to `AddDeviceDialog`
4. Create UI controls in `DeviceControlFactory`
5. Update any relevant controllers

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Document public APIs with Javadoc
- Keep methods focused and concise

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Future Enhancements

- [ ] Voice control 
- [ ] SMS/Email notifications
- [ ] AI-powered automation suggestions
- [ ] Geofencing for automatic arm/disarm

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

**Made with ❤️**

</div>
