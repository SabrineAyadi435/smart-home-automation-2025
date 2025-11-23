# Smart Home Floor Plan UI - User Guide

## Overview

The Smart Home Floor Plan UI provides an interactive, visual representation of your smart home with a house layout showing rooms and devices. Each room is displayed as a tile with its devices, and you can control everything with intuitive clicks and sliders.

## Features

### 🏠 Realistic House Layout
- **Actual house structure** with roof, walls, doors, and windows
- **Triangular roof** with chimney
- **Interior walls** dividing rooms with doorways
- **Windows** with panes for natural light effect
- **Room tiles** positioned within the house structure
- Each room has a distinct color for easy identification
- Rooms display device count and icons
- Hover effects and smooth transitions

### 📱 Device Types Supported
1. **Light (💡)** - Control brightness with slider
2. **Thermostat (🌡️)** - Adjust temperature (10-35°C)
3. **Smart TV (📺)** - Control volume
4. **Smart Plug (🔌)** - Toggle power on/off
5. **Smart Lock (🔒)** - Lock/unlock doors
6. **Camera (📷)** - Start/stop recording
7. **Motion Sensor (👁️)** - Detect and clear motion
8. **Speaker (🔊)** - Play/pause and volume control

### ✨ Interactive Controls

#### Room Management
- **Add Room**: Click "+ Add Room" button in the top bar
  - Enter room name (e.g., "Bathroom", "Office", "Garage")
  - Choose room color from palette
  - Room appears instantly in floor plan

#### Device Management
- **Add Device**: Click "+ Add Device" button in any room tile
  - Select device type from dropdown
  - Enter device name
  - Device appears in the room immediately

#### Device Control
- **Click any device icon** to open its control panel
- Each device has specific controls:
  - **Lights**: ON/OFF toggle + brightness slider (0-100%)
  - **Thermostat**: ON/OFF + temperature slider (10-35°C)
  - **Smart TV**: ON/OFF + volume slider
  - **Smart Plug**: ON/OFF toggle
  - **Smart Lock**: Lock/Unlock button
  - **Camera**: ON/OFF + Start/Stop recording
  - **Motion Sensor**: Simulate motion / Clear motion
  - **Speaker**: ON/OFF + Play/Pause + volume slider

### ⚡ Energy Dashboard
- Real-time energy consumption display at bottom
- Color-coded indicators:
  - **Green**: Low consumption (< 25 kWh)
  - **Orange**: Medium consumption (25-50 kWh)
  - **Red**: High consumption (> 50 kWh)

### 🎮 Global Controls
- **All ON**: Turn on all devices in the home
- **All OFF**: Turn off all devices in the home
- **Refresh**: Update the display

## Running the Application

### Option 1: Using Maven
```bash
mvn clean compile
mvn javafx:run
```

### Option 2: Using the provided script
```bash
chmod +x run-gui.sh
./run-gui.sh
```

### Option 3: Run FloorPlanGUI directly
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.smarthome.gui.FloorPlanGUI"
```

## Sample Setup

The application starts with 4 pre-configured rooms:

1. **Living Room** (Peach color)
   - Ceiling Light
   - Smart TV
   - Smart Speaker
   - Motion Sensor

2. **Bedroom** (Lavender color)
   - Bedside Lamp
   - Thermostat
   - Phone Charger (Smart Plug)

3. **Kitchen** (Mint green color)
   - Kitchen Light
   - Coffee Maker (Smart Plug)

4. **Entrance** (Cream color)
   - Front Door (Smart Lock)
   - Security Camera
   - Porch Light

## Testing the UI

### Manual Test Steps

1. **Launch Application**
   - Verify floor plan displays with 4 rooms
   - Check that each room shows correct device count

2. **Test Device Controls**
   - Click on a Light icon
   - Toggle ON/OFF - verify icon opacity changes
   - Adjust brightness slider - verify energy consumption updates
   - Close dialog and verify changes persist

3. **Test Thermostat**
   - Click thermostat icon
   - Adjust temperature slider (10-35°C)
   - Verify energy consumption changes based on temperature

4. **Test Smart Lock**
   - Click lock icon in Entrance room
   - Toggle Lock/Unlock
   - Verify status updates in tooltip

5. **Test Camera**
   - Click camera icon
   - Turn ON camera
   - Start recording - verify status changes
   - Stop recording

6. **Test Motion Sensor**
   - Click motion sensor icon
   - Click "Simulate Motion" - verify status shows "Motion Detected"
   - Click "Clear Motion" - verify status returns to "No Motion"

7. **Test Speaker**
   - Click speaker icon
   - Turn ON speaker
   - Click Play - verify status shows "PLAYING"
   - Adjust volume slider
   - Click Pause

8. **Add New Room**
   - Click "+ Add Room" button
   - Enter "Bathroom"
   - Select a blue color
   - Click "Add Room"
   - Verify new room appears in floor plan

9. **Add New Device**
   - Click "+ Add Device" in the new Bathroom room
   - Select "Light" from dropdown
   - Enter "Bathroom Light"
   - Click "Add Device"
   - Verify device appears in room

10. **Test Global Controls**
    - Click "All ON" - verify all devices turn on
    - Check energy dashboard shows increased consumption
    - Click "All OFF" - verify all devices turn off
    - Energy should drop to near zero

11. **Test Energy Dashboard**
    - Turn on multiple high-consumption devices (TV, Thermostat)
    - Verify energy label color changes:
      - Green for low consumption
      - Orange for medium
      - Red for high

12. **Test Hover Effects**
    - Hover over room tiles - verify border color changes to blue
    - Hover over device icons - verify tooltip appears
    - Hover over buttons - verify scale effect

## UI Customization

### Modifying Colors
Edit `src/main/resources/floorplan.css`:
- Room colors: Modify `.room-tile` background colors
- Device icon colors: Modify `.device-icon.light`, `.device-icon.thermostat`, etc.
- Button colors: Modify `.control-button`, `.add-button`

### Adding New Device Types
1. Create device class in `com.smarthome.model` package
2. Implement required interfaces (Controllable, EnergyConsumer, etc.)
3. Add emoji and CSS class in `FloorPlanGUI.java`:
   - Update `getDeviceEmoji()` method
   - Update `getDeviceClass()` method
4. Add device-specific controls in `showDeviceControlDialog()` method
5. Add device type to dropdown in `showAddDeviceDialog()` method

## Architecture

### Key Components

- **FloorPlanGUI.java**: Main UI class with floor plan layout
- **SmartHomeGUI.java**: Original list-based UI (still available)
- **floorplan.css**: Comprehensive styling for the floor plan UI
- **Device Models**: Light, Thermostat, SmartTV, SmartPlug, SmartLock, Camera, MotionSensor, Speaker

### Design Patterns Used

- **Observer Pattern**: UI updates when device state changes
- **Factory Pattern**: Device creation in add device dialog
- **MVC Pattern**: Model (devices), View (GUI), Controller (HomeController)

## Troubleshooting

### Issue: CSS not loading
- Verify `floorplan.css` is in `src/main/resources/`
- Check that Maven copied resources: `mvn clean compile`

### Issue: Devices not responding
- Check console for error messages
- Verify device is turned ON before using specific controls
- Refresh the UI using the Refresh button

### Issue: Energy not updating
- Ensure devices implement `EnergyConsumer` interface
- Check that `updateEnergyDashboard()` is called after state changes

## Future Enhancements

Potential improvements:
- Drag-and-drop room positioning
- Custom room shapes and sizes
- Device grouping and scenes
- Automation rules UI
- Historical energy graphs
- Export/import floor plan configuration
- Multi-floor support
- Real-time device status updates

## Support

For issues or questions:
1. Check console output for error messages
2. Verify all dependencies in `pom.xml`
3. Ensure Java 11+ and JavaFX 17+ are installed
4. Review device implementation for custom devices
