# Setup Guide - Smart Home Automation Simulator

This guide will help you set up and run the Smart Home Automation Simulator with JavaFX GUI.

## Quick Start (Easiest Method)

### Using Maven (Recommended)

1. **Install Maven** (if not already installed)
   - **macOS**: `brew install maven`
   - **Ubuntu/Debian**: `sudo apt install maven`
   - **Windows**: Download from https://maven.apache.org/download.cgi

2. **Run the application**
   ```bash
   mvn clean javafx:run
   ```

That's it! Maven will automatically download JavaFX and all dependencies.

---

## Alternative Methods

### Method 1: Using the Run Scripts

**On macOS/Linux:**
```bash
chmod +x run-gui.sh
./run-gui.sh
```

**On Windows:**
```cmd
run-gui.bat
```

### Method 2: Manual Compilation

#### Step 1: Install Java JDK 11+

Check your Java version:
```bash
java -version
```

If you need to install Java:
- **macOS**: `brew install openjdk@11`
- **Ubuntu**: `sudo apt install openjdk-11-jdk`
- **Windows**: Download from https://adoptium.net/

#### Step 2: Install JavaFX

**Option A: Using Package Manager (Linux/macOS)**
- **Ubuntu/Debian**: `sudo apt install openjfx`
- **macOS**: `brew install openjfx`

**Option B: Manual Download**
1. Download JavaFX SDK from: https://gluonhq.com/products/javafx/
2. Extract to a location (e.g., `~/javafx-sdk-17`)
3. Note the path to the `lib` folder

#### Step 3: Compile and Run

**If JavaFX is in your system path:**
```bash
# Compile
javac -d bin src/main/java/com/smarthome/**/*.java

# Run GUI
java -cp bin com.smarthome.gui.SmartHomeGUI

# Or run console version
java -cp bin com.smarthome.Main
```

**If using separate JavaFX SDK:**
```bash
# Set your JavaFX path
export JAVAFX_PATH="/path/to/javafx-sdk-17/lib"

# Compile
javac --module-path $JAVAFX_PATH --add-modules javafx.controls \
  -d bin src/main/java/com/smarthome/**/*.java

# Run
java --module-path $JAVAFX_PATH --add-modules javafx.controls \
  -cp bin com.smarthome.gui.SmartHomeGUI
```

---

## Running the Console Version (No JavaFX Required)

If you want to run without GUI:

```bash
# Compile
javac -d bin src/main/java/com/smarthome/**/*.java

# Run
java -cp bin com.smarthome.Main
```

---

## Troubleshooting

### Error: "JavaFX runtime components are missing"

**Solution**: Use Maven (recommended) or install JavaFX SDK and use module path flags.

### Error: "package javafx.application does not exist"

**Solution**: JavaFX is not in your classpath. Either:
1. Use Maven: `mvn clean javafx:run`
2. Add `--module-path` and `--add-modules` flags
3. Install JavaFX in your JDK

### Error: "class file has wrong version"

**Solution**: You're using an older Java version. Upgrade to JDK 11 or higher.

### Maven build fails

**Solution**: 
1. Check internet connection (Maven needs to download dependencies)
2. Clear Maven cache: `mvn clean`
3. Update Maven: Check you have Maven 3.6+

---

## IDE Setup

### IntelliJ IDEA

1. Open project folder
2. Right-click `pom.xml` → "Add as Maven Project"
3. Run `SmartHomeGUI.java` (IntelliJ handles JavaFX automatically)

### Eclipse

1. File → Import → Maven → Existing Maven Projects
2. Select project folder
3. Right-click project → Run As → Java Application → Select `SmartHomeGUI`

### VS Code

1. Install "Extension Pack for Java"
2. Install "Maven for Java"
3. Open project folder
4. Run from Maven sidebar or use terminal: `mvn javafx:run`

---

## Project Structure

```
smart-home-simulator/
├── src/main/java/com/smarthome/
│   ├── Main.java              # Console version
│   ├── gui/
│   │   └── SmartHomeGUI.java  # JavaFX GUI
│   ├── model/                 # Device classes
│   ├── controller/            # Home controller
│   ├── automation/            # Automation engine
│   ├── interfaces/            # Java interfaces
│   └── exceptions/            # Custom exceptions
├── pom.xml                    # Maven configuration
├── README.md                  # Main documentation
├── SETUP.md                   # This file
├── run-gui.sh                 # Linux/Mac launcher
└── run-gui.bat                # Windows launcher
```

---

## Next Steps

Once running, you can:
- Toggle devices on/off
- Adjust light brightness with sliders
- Monitor real-time energy consumption
- Trigger automation rules
- Control all devices at once

Enjoy your Smart Home Simulator! 🏠✨
