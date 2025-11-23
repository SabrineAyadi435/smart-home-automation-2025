# Smart Home Floor Plan UI - Test Plan

## Test Environment Setup

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- JavaFX 17+

### Build and Run
```bash
cd JAVA_Project
mvn clean compile
mvn javafx:run
```

Or use the launcher script:
```bash
chmod +x run-gui.sh
./run-gui.sh
# Select option 1 for Floor Plan UI
```

---

## Unit Test Cases

### TC-001: Application Launch
**Objective**: Verify application starts successfully  
**Steps**:
1. Run `mvn javafx:run`
2. Observe application window

**Expected Result**:
- Window opens with title "Smart Home Floor Plan"
- Floor plan displays with 4 pre-configured rooms
- Top bar shows title and control buttons
- Bottom bar shows energy dashboard
- No errors in console

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-002: Initial Room Display
**Objective**: Verify all rooms are displayed correctly  
**Steps**:
1. Launch application
2. Observe floor plan area

**Expected Result**:
- 4 rooms visible: Living Room, Bedroom, Kitchen, Entrance
- Each room has distinct color
- Each room shows device count
- Room tiles have rounded corners and shadows

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-003: Device Icons Display
**Objective**: Verify devices are shown with correct icons  
**Steps**:
1. Launch application
2. Check each room for device icons

**Expected Result**:
- Living Room: 💡 🔊 📺 👁️ (4 devices)
- Bedroom: 💡 🌡️ 🔌 (3 devices)
- Kitchen: 💡 🔌 (2 devices)
- Entrance: 🔒 📷 💡 (3 devices)
- All icons visible and properly sized

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-004: Light Control
**Objective**: Test light device controls  
**Steps**:
1. Click on any Light icon (💡)
2. Control dialog opens
3. Click "Turn ON" button
4. Adjust brightness slider to 75%
5. Click "Turn OFF" button
6. Close dialog

**Expected Result**:
- Dialog opens with light name and status
- ON button turns light on (icon becomes fully opaque)
- Brightness slider adjusts from 0-100%
- Energy consumption updates in real-time
- OFF button turns light off (icon becomes semi-transparent)
- Changes persist after closing dialog

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-005: Thermostat Control
**Objective**: Test thermostat temperature adjustment  
**Steps**:
1. Click thermostat icon (🌡️) in Bedroom
2. Turn ON thermostat
3. Adjust temperature slider to 25°C
4. Observe status and energy consumption
5. Adjust to 18°C
6. Close dialog

**Expected Result**:
- Dialog shows current and target temperature
- Temperature slider ranges from 10-35°C
- Status updates to show target temperature
- Energy consumption changes based on temperature difference
- Higher temperature difference = higher energy consumption

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-006: Smart Lock Control
**Objective**: Test lock/unlock functionality  
**Steps**:
1. Click Smart Lock icon (🔒) in Entrance
2. Observe initial status (should be LOCKED)
3. Click "🔓 Unlock" button
4. Observe status change
5. Click "🔒 Lock" button
6. Close dialog

**Expected Result**:
- Initial status shows "LOCKED"
- Unlock button changes status to "UNLOCKED"
- Button text changes to "🔒 Lock"
- Lock button changes status back to "LOCKED"
- Status updates visible in tooltip on hover

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-007: Camera Control
**Objective**: Test camera recording functionality  
**Steps**:
1. Click Camera icon (📷) in Entrance
2. Turn ON camera
3. Click "⏺ Start Recording" button
4. Observe status change
5. Click "⏹ Stop Recording" button
6. Turn OFF camera

**Expected Result**:
- Camera must be ON to start recording
- Status changes to "RECORDING (1080p)"
- Button changes to "⏹ Stop Recording"
- Stop button changes status to "STANDBY (1080p)"
- Turning OFF camera stops recording automatically

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-008: Motion Sensor Control
**Objective**: Test motion detection simulation  
**Steps**:
1. Click Motion Sensor icon (👁️) in Living Room
2. Click "Simulate Motion" button
3. Observe status change
4. Close and reopen dialog
5. Click "Clear Motion" button
6. Observe status change

**Expected Result**:
- Initial status: "ACTIVE - No Motion"
- After simulate: "ACTIVE - Motion Detected"
- Status persists when dialog is closed and reopened
- After clear: "ACTIVE - No Motion"
- Console shows motion detection messages

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-009: Speaker Control
**Objective**: Test speaker play/pause and volume  
**Steps**:
1. Click Speaker icon (🔊) in Living Room
2. Turn ON speaker
3. Click "▶ Play" button
4. Adjust volume slider to 80%
5. Click "⏸ Pause" button
6. Observe energy consumption changes

**Expected Result**:
- Speaker must be ON to play
- Play button changes to "⏸ Pause"
- Status shows "PLAYING (Vol: 80%)"
- Energy consumption increases when playing
- Pause button changes to "▶ Play"
- Status shows "STANDBY (Vol: 80%)"
- Energy consumption decreases when paused

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-010: Smart TV Control
**Objective**: Test TV volume control  
**Steps**:
1. Click Smart TV icon (📺) in Living Room
2. Turn ON TV
3. Adjust volume slider to 60%
4. Observe status update
5. Turn OFF TV

**Expected Result**:
- TV turns on successfully
- Volume slider adjusts from 0-100%
- Status shows "ON (Channel: 1, Volume: 60)"
- Energy consumption shows 15.0 kWh when ON
- Energy drops to 0.5 kWh when OFF (standby)

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-011: Smart Plug Control
**Objective**: Test smart plug on/off  
**Steps**:
1. Click Smart Plug icon (🔌) in Kitchen
2. Turn ON plug
3. Observe status and energy
4. Turn OFF plug
5. Observe changes

**Expected Result**:
- Status shows "ON (5.0W)" when on
- Energy consumption shows 5.0 kWh
- Status shows "OFF" when off
- Energy consumption drops to 0.0 kWh

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-012: Add New Room
**Objective**: Test room creation functionality  
**Steps**:
1. Click "+ Add Room" button in top bar
2. Enter room name: "Bathroom"
3. Select a blue color from palette
4. Click "Add Room" button
5. Observe floor plan

**Expected Result**:
- Modal dialog opens with form
- Color palette shows 10 color options
- Selected color has thicker border
- New room appears in floor plan with chosen color
- Room shows "0 devices"
- Dialog closes automatically

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-013: Add Device to Room
**Objective**: Test device creation in a room  
**Steps**:
1. Click "+ Add Device" in any room
2. Select "Light" from dropdown
3. Enter device name: "Test Light"
4. Click "Add Device" button
5. Observe room tile

**Expected Result**:
- Modal dialog opens with device form
- Dropdown shows all 8 device types
- Device appears in room immediately
- Device count increments
- Device icon is clickable
- Dialog closes automatically

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-014: Add Multiple Device Types
**Objective**: Test adding various device types  
**Steps**:
1. Create new room "Office"
2. Add Light device
3. Add Thermostat device
4. Add Camera device
5. Add Speaker device
6. Verify all devices appear

**Expected Result**:
- All 4 devices added successfully
- Each device has correct icon
- Device count shows "4 devices"
- All devices are controllable
- Each device has appropriate controls in dialog

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-015: Global All ON Control
**Objective**: Test turning all devices on  
**Steps**:
1. Ensure some devices are OFF
2. Note current energy consumption
3. Click "All ON" button in top bar
4. Observe all device icons
5. Check energy dashboard

**Expected Result**:
- All device icons become fully opaque
- All devices show ON status in tooltips
- Energy consumption increases significantly
- Energy label color may change to orange/red
- Console shows "turned ON" messages for each device

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-016: Global All OFF Control
**Objective**: Test turning all devices off  
**Steps**:
1. Ensure some devices are ON
2. Click "All OFF" button in top bar
3. Observe all device icons
4. Check energy dashboard

**Expected Result**:
- All device icons become semi-transparent (opacity 0.4)
- All devices show OFF status
- Energy consumption drops to near zero (some devices have standby power)
- Energy label color changes to green
- Console shows "turned OFF" messages

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-017: Energy Dashboard Updates
**Objective**: Verify energy consumption calculation  
**Steps**:
1. Turn OFF all devices
2. Note energy: should be ~0-2 kWh
3. Turn ON Living Room Light at 100% brightness
4. Note energy increase (~10 kWh)
5. Turn ON Smart TV
6. Note energy increase (~15 kWh)
7. Turn ON Thermostat at 30°C
8. Note energy increase (varies)

**Expected Result**:
- Energy starts near zero
- Light adds ~10 kWh (100% brightness)
- TV adds ~15 kWh
- Thermostat adds variable energy based on temperature
- Total energy label updates in real-time
- Color changes: Green → Orange → Red as consumption increases

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-018: Energy Color Coding
**Objective**: Test energy dashboard color indicators  
**Steps**:
1. Turn OFF all devices (energy < 25 kWh)
2. Verify label is GREEN
3. Turn ON devices to reach 30 kWh
4. Verify label is ORANGE
5. Turn ON more devices to reach 60 kWh
6. Verify label is RED

**Expected Result**:
- < 25 kWh: Green color
- 25-50 kWh: Orange color
- > 50 kWh: Red color
- Color transitions smoothly
- Energy value displays with 2 decimal places

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-019: Hover Effects
**Objective**: Test UI hover interactions  
**Steps**:
1. Hover over a room tile
2. Hover over a device icon
3. Hover over control buttons
4. Hover over add buttons

**Expected Result**:
- Room tile: Border changes to blue, shadow increases, slight scale up
- Device icon: Tooltip appears showing device status
- Control buttons: Background darkens, slight scale up
- Add buttons: Background darkens, slight scale up
- Cursor changes to hand pointer on interactive elements

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-020: Refresh Functionality
**Objective**: Test UI refresh button  
**Steps**:
1. Make several changes (turn devices on/off)
2. Click "Refresh" button
3. Observe UI updates

**Expected Result**:
- All device states refresh
- Energy consumption recalculates
- Room tiles update
- No data loss
- UI remains responsive

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-021: Modal Dialog Behavior
**Objective**: Test modal dialog interactions  
**Steps**:
1. Open device control dialog
2. Try clicking outside dialog
3. Make changes
4. Click "Close" button
5. Reopen same device dialog

**Expected Result**:
- Dialog appears centered
- Clicking outside doesn't close dialog (modal behavior)
- Changes are saved when dialog closes
- Close button works correctly
- Reopening shows updated state

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-022: Input Validation - Room Name
**Objective**: Test room name validation  
**Steps**:
1. Click "+ Add Room"
2. Leave name field empty
3. Click "Add Room"
4. Observe validation message

**Expected Result**:
- Warning alert appears
- Message: "Please enter a room name."
- Dialog remains open
- No room is created

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-023: Input Validation - Device Name
**Objective**: Test device name validation  
**Steps**:
1. Click "+ Add Device" in any room
2. Leave name field empty
3. Click "Add Device"
4. Observe validation message

**Expected Result**:
- Warning alert appears
- Message: "Please enter a device name."
- Dialog remains open
- No device is created

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-024: CSS Styling
**Objective**: Verify CSS is properly applied  
**Steps**:
1. Launch application
2. Inspect visual elements

**Expected Result**:
- Top bar has dark gradient background
- Room tiles have rounded corners and shadows
- Device icons are circular with colored backgrounds
- Buttons have hover effects
- Energy panel has white background with border
- Fonts are clear and properly sized
- Colors match design specification

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-025: Responsive Layout
**Objective**: Test layout at different window sizes  
**Steps**:
1. Launch application at default size (1400x900)
2. Resize window to smaller (1000x700)
3. Resize window to larger (1600x1000)
4. Observe layout adjustments

**Expected Result**:
- Room tiles wrap to new rows as needed
- Scroll bars appear when content exceeds viewport
- Elements don't overlap
- Text remains readable
- Buttons remain accessible

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-026: Console Output
**Objective**: Verify console logging  
**Steps**:
1. Launch application from terminal
2. Perform various actions (turn devices on/off, add room, etc.)
3. Observe console output

**Expected Result**:
- Device state changes logged
- Room additions logged
- Device additions logged
- No error messages or exceptions
- Messages are clear and informative

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-027: Multiple Rooms with Same Name
**Objective**: Test duplicate room names  
**Steps**:
1. Add room named "Office"
2. Add another room named "Office"
3. Observe both rooms

**Expected Result**:
- Both rooms are created (system allows duplicates)
- Both rooms are independently controllable
- Devices in each room are separate
- Consider adding unique IDs in future enhancement

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-028: Device ID Generation
**Objective**: Verify unique device IDs  
**Steps**:
1. Add multiple devices to same room
2. Check console output for device IDs
3. Verify uniqueness

**Expected Result**:
- Each device gets unique ID
- ID format: [ROOM_INITIALS]-[TIMESTAMP]
- No ID collisions
- IDs are consistent

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-029: Persistence Test
**Objective**: Test in-memory data persistence  
**Steps**:
1. Add new room "Garage"
2. Add 3 devices to Garage
3. Turn some devices ON
4. Navigate through UI
5. Verify data persists

**Expected Result**:
- Room remains in floor plan
- Devices remain in room
- Device states (ON/OFF) persist
- Energy consumption remains accurate
- Note: Data is lost on application restart (in-memory only)

**Status**: ⬜ Pass / ⬜ Fail

---

### TC-030: Stress Test - Many Rooms
**Objective**: Test performance with many rooms  
**Steps**:
1. Add 10 new rooms
2. Add 5 devices to each room
3. Turn all devices ON
4. Observe performance

**Expected Result**:
- All rooms display correctly
- UI remains responsive
- Energy calculation is accurate
- No lag when opening device dialogs
- Scroll functionality works smoothly

**Status**: ⬜ Pass / ⬜ Fail

---

## Integration Test Cases

### ITC-001: HomeController Integration
**Objective**: Verify GUI integrates with HomeController  
**Steps**:
1. Use "All ON" button
2. Verify HomeController.turnOnAllDevices() is called
3. Use "All OFF" button
4. Verify HomeController.turnOffAllDevices() is called

**Expected Result**:
- Controller methods execute correctly
- All devices respond to controller commands
- No exceptions thrown

**Status**: ⬜ Pass / ⬜ Fail

---

### ITC-002: Energy Consumer Interface
**Objective**: Test EnergyConsumer interface integration  
**Steps**:
1. Turn ON devices that implement EnergyConsumer
2. Verify energy consumption is calculated
3. Turn OFF devices
4. Verify energy drops

**Expected Result**:
- Light, Thermostat, SmartTV, SmartPlug, Speaker report energy
- Camera, MotionSensor, SmartLock don't report energy (not consumers)
- Total energy is sum of all consumers

**Status**: ⬜ Pass / ⬜ Fail

---

### ITC-003: Controllable Interface
**Objective**: Test Controllable interface integration  
**Steps**:
1. Test executeCommand() on various devices
2. Verify isControllable() returns true

**Expected Result**:
- All devices except MotionSensor are controllable
- Commands execute correctly
- Interface methods work as expected

**Status**: ⬜ Pass / ⬜ Fail

---

## Performance Test Cases

### PTC-001: Application Startup Time
**Objective**: Measure startup performance  
**Steps**:
1. Close application
2. Note time
3. Launch application
4. Note time when fully loaded

**Expected Result**:
- Application starts in < 5 seconds
- UI is responsive immediately
- No delays in rendering

**Status**: ⬜ Pass / ⬜ Fail

---

### PTC-002: Device Control Response Time
**Objective**: Measure control responsiveness  
**Steps**:
1. Click device icon
2. Measure time to dialog open
3. Toggle device state
4. Measure time to UI update

**Expected Result**:
- Dialog opens in < 500ms
- State changes reflect in < 200ms
- Energy updates in < 300ms
- No noticeable lag

**Status**: ⬜ Pass / ⬜ Fail

---

## Test Summary

Total Test Cases: 33
- Unit Tests: 30
- Integration Tests: 3
- Performance Tests: 2

### Test Execution Checklist
- ⬜ All test cases executed
- ⬜ Pass rate > 95%
- ⬜ Critical bugs fixed
- ⬜ Performance acceptable
- ⬜ Documentation updated

### Sign-off
- Tester Name: _______________
- Date: _______________
- Signature: _______________
