# Quick Start Guide - Smart Home Floor Plan UI

## 🚀 Get Started in 3 Steps

### Step 1: Build the Project
```bash
cd JAVA_Project
mvn clean compile
```

### Step 2: Run the Application
```bash
mvn javafx:run
```

### Step 3: Explore the UI
The floor plan will open showing a **realistic house** with:
- Triangular roof with chimney
- Walls, doors, and windows
- 4 pre-configured rooms inside the house
- Start clicking around!

---

## 🎮 5-Minute Tutorial

### 1. Control a Device (30 seconds)
1. Click on any device icon (e.g., 💡 in Living Room)
2. A control dialog opens
3. Click "Turn ON" button
4. Adjust the brightness slider
5. Click "Close"
6. Notice the device icon is now fully bright!

### 2. Add a New Room (1 minute)
1. Click the green **"+ Add Room"** button at the top
2. Enter a room name: "Bathroom"
3. Click on a blue color square
4. Click **"Add Room"**
5. Your new room appears in the floor plan!

### 3. Add a Device to Your Room (1 minute)
1. Find your new "Bathroom" room
2. Click the **"+ Add Device"** button inside it
3. Select "Light" from the dropdown
4. Enter device name: "Bathroom Light"
5. Click **"Add Device"**
6. The light icon 💡 appears in your room!

### 4. Test Different Device Types (2 minutes)
Try clicking on these devices to see their unique controls:

- **💡 Light**: Brightness slider
- **🌡️ Thermostat**: Temperature control (10-35°C)
- **🔒 Smart Lock**: Lock/Unlock button
- **📷 Camera**: Start/Stop recording
- **🔊 Speaker**: Play/Pause + Volume
- **👁️ Motion Sensor**: Simulate motion

### 5. Monitor Energy (30 seconds)
1. Turn ON several devices
2. Look at the bottom energy panel
3. Watch the number increase
4. Notice the color changes:
   - **Green**: Low consumption
   - **Orange**: Medium consumption
   - **Red**: High consumption
5. Click **"All OFF"** to see energy drop to near zero

---

## 🎯 Common Tasks

### Turn All Devices On/Off
- Click **"All ON"** in the top bar → All devices turn on
- Click **"All OFF"** in the top bar → All devices turn off

### Check Device Status
- Hover over any device icon
- A tooltip shows the current status

### Adjust Device Settings
- Click device icon → Control dialog opens
- Make changes → Click "Close"
- Changes are saved automatically

### Create Multiple Rooms
- Click **"+ Add Room"** multiple times
- Each room can have a different color
- Rooms auto-arrange in a grid layout

### Add Multiple Devices
- Each room can have unlimited devices
- Mix different device types in one room
- Device count updates automatically

---

## 💡 Pro Tips

### Tip 1: Energy Management
Turn off high-consumption devices (TV, Thermostat) to reduce energy usage.

### Tip 2: Room Organization
Use different colors for different areas:
- 🟠 Peach: Living spaces
- 🟣 Lavender: Bedrooms
- 🟢 Mint: Kitchen/Dining
- 🟡 Cream: Entrance/Hallways

### Tip 3: Device Naming
Use clear names like:
- "Kitchen Ceiling Light" (not just "Light")
- "Front Door Lock" (not just "Lock")
- "Living Room Camera" (not just "Camera")

### Tip 4: Testing Automation
1. Add a Motion Sensor to a room
2. Add a Light to the same room
3. Click the Motion Sensor
4. Click "Simulate Motion"
5. (Future: This will trigger automation rules)

### Tip 5: Hover for Info
Hover over any element to see tooltips with additional information.

---

## 🐛 Troubleshooting

### Problem: Application won't start
**Solution**: Ensure Java 11+ and JavaFX are installed
```bash
java -version  # Should show 11 or higher
mvn -version   # Should show Maven 3.6+
```

### Problem: CSS not loading (plain white UI)
**Solution**: Rebuild the project
```bash
mvn clean compile
mvn javafx:run
```

### Problem: Devices not responding
**Solution**: 
1. Check console for error messages
2. Try clicking "Refresh" button
3. Restart the application

### Problem: Energy not updating
**Solution**: 
1. Turn device OFF then ON again
2. Click "Refresh" button
3. Check that device implements EnergyConsumer interface

---

## 📚 Next Steps

### Learn More
- Read **FLOORPLAN_GUIDE.md** for detailed features
- Check **TEST_PLAN.md** for all test cases
- See **UI_REFERENCE.md** for visual layouts

### Customize
- Edit `src/main/resources/floorplan.css` to change colors
- Modify room colors in the Add Room dialog
- Create your own device types (see FLOORPLAN_GUIDE.md)

### Test
- Follow the 33 test cases in TEST_PLAN.md
- Try stress testing with 10+ rooms
- Test all device types and controls

---

## 🎨 Sample Scenarios

### Scenario 1: Morning Routine
1. Add "Bedroom" room with Thermostat and Light
2. Turn ON Thermostat, set to 22°C
3. Turn ON Light, set brightness to 50%
4. Check energy consumption

### Scenario 2: Security Setup
1. Add "Entrance" room
2. Add Smart Lock device
3. Add Camera device
4. Add Motion Sensor device
5. Test lock/unlock
6. Start camera recording
7. Simulate motion detection

### Scenario 3: Entertainment Center
1. Add "Living Room" room
2. Add Smart TV device
3. Add Speaker device
4. Add Light device
5. Turn all ON
6. Adjust TV volume to 60%
7. Set Speaker to play
8. Dim lights to 30%
9. Check total energy consumption

### Scenario 4: Smart Kitchen
1. Add "Kitchen" room
2. Add Light device (Ceiling Light)
3. Add Smart Plug device (Coffee Maker)
4. Add Smart Plug device (Toaster)
5. Turn ON Coffee Maker
6. Monitor energy usage

---

## 🎓 Learning Objectives

By completing this quick start, you've learned:
- ✅ How to navigate the floor plan UI
- ✅ How to control different device types
- ✅ How to add rooms and devices dynamically
- ✅ How to monitor energy consumption
- ✅ How to use global controls

---

## 🆘 Need Help?

1. **Check Documentation**:
   - FLOORPLAN_GUIDE.md - Complete user guide
   - TEST_PLAN.md - Testing procedures
   - UI_REFERENCE.md - Visual reference

2. **Check Console Output**:
   - Run from terminal to see debug messages
   - Look for error messages or exceptions

3. **Verify Setup**:
   - Java 11+ installed
   - Maven 3.6+ installed
   - JavaFX 17+ available

4. **Common Issues**:
   - See Troubleshooting section above
   - Check README.md for setup instructions

---

## ✨ Enjoy Your Smart Home!

You're now ready to explore the full capabilities of the Smart Home Floor Plan UI. Have fun creating your virtual smart home! 🏠
