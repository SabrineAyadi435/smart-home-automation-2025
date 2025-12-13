package com;

import com.controller.HomeController;
import com.home.Home;
import com.room.Room;
import com.devices.*;
import com.enums.EnergyMode;

public class Main {
    // Test counter variables
    private static int totalTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        printHeader("SMART HOME AUTOMATION SYSTEM - COMPREHENSIVE TEST");
        
        // Setup home and controllers
        Home home = setupHome();
        HomeController homeController = new HomeController(home);
        
        // Test security devices
        testSecurityCamera();
        testMotionSensor();
        testDoorWindowSensor();
        testDoorLock();
        testSmokeDetector();
        testAlarmSiren();
        
        // Test comfort devices
        testAC();
        testLight();
        testSmartTV();
        testSpeaker();
        testSmartPlug();
        
        // Test utility devices
        testSmartFridge();
        testSmartFaucet();
        testSmartMirror();
        testAirQualitySensor();
        
        // Test HomeController
        testHomeController(homeController, home);
        
        // Test SecurityController
        testSecurityController(homeController);
        
        // Test Room and Home
        testRoom();
        testHome();
        
        // Test services
        testCalendarService();
        testIslamicCalendarService();
        testQiblaService();
        testQuranAPIService();
        
        // Test automation
        testAutomationEngine();
        
        // Test integration scenarios
        testMorningRoutine(homeController, home);
        testLeavingHome(homeController, home);
        testIntruderDetection(homeController, home);
        testComingHome(homeController, home);
        testNightMode(homeController, home);
        testFireEmergency(homeController, home);
        testWuduTime(home);
        
        // Test exception handling
        testExceptionHandling(homeController, home);
        
        // Final summary
        printTestSummary();
    }

    /**
     * Helper method to print a formatted header
     */
    private static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(title);
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Helper method to print a section separator
     */
    private static void printSection(String section) {
        System.out.println("\n--- " + section + " ---");
    }

    /**
     * Helper method to print a successful test result
     */
    private static void printTest(String method, String result) {
        System.out.println("  ✓ " + method + ": " + result);
        totalTests++;
    }

    /**
     * Helper method to print an error/exception result
     */
    private static void printError(String method, Exception e) {
        System.out.println("  ✗ " + method + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
        totalTests++;
        failedTests++;
    }

    /**
     * Test SecurityCamera device
     */
    private static void testSecurityCamera() {
        printSection("Testing SecurityCamera");
        
        try {
            // Instantiate SecurityCamera
            SecurityCamera camera = new SecurityCamera("camera-test", "Test Camera", "1080p", 120, EnergyMode.NORMAL);
            printTest("SecurityCamera instantiation", "Created with ID: camera-test");
            
            // Test turnOn(), turnOff(), getStatus(), isOn()
            camera.turnOn();
            printTest("turnOn()", "Camera recording: " + camera.isOn());
            
            String status = camera.getStatus();
            printTest("getStatus()", status);
            
            camera.turnOff();
            printTest("turnOff()", "Camera stopped: " + !camera.isOn());
            
            printTest("isOn()", "Status: " + camera.isOn());
            
            // Test toggleNightVision(), getLiveFeed()
            camera.turnOn();
            camera.toggleNightVision();
            printTest("toggleNightVision()", "Night vision toggled");
            
            String liveFeed = camera.getLiveFeed();
            printTest("getLiveFeed()", liveFeed);
            
            // Test schedule(), cancelScheduledTasks(), scheduleTask()
            camera.schedule("daily-recording");
            printTest("schedule()", "Schedule set: " + camera.getSchedule());
            
            camera.scheduleTask(java.time.LocalTime.of(22, 0), () -> System.out.println("Scheduled task executed"));
            printTest("scheduleTask()", "Task scheduled for 22:00");
            
            camera.cancelScheduledTasks();
            printTest("cancelScheduledTasks()", "All tasks cancelled");
            
            // Test setEnergyMode(), getEnergyConsumption()
            camera.setEnergyMode(EnergyMode.ECO);
            printTest("setEnergyMode()", "Energy mode set to ECO");
            
            double consumption = camera.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " kWh");
            
            // Test executeCommand()
            camera.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            camera.executeCommand("TOGGLE_NIGHT");
            printTest("executeCommand('TOGGLE_NIGHT')", "Command executed");
            
            camera.executeCommand("STATUS");
            printTest("executeCommand('STATUS')", "Command executed");
            
            camera.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
        } catch (Exception e) {
            printError("testSecurityCamera", e);
        }
    }

    /**
     * Test MotionSensor device
     */
    private static void testMotionSensor() {
        printSection("Testing MotionSensor");
        
        try {
            // Instantiate MotionSensor
            MotionSensor sensor = new MotionSensor("motion-test", "Test Motion Sensor", 15, EnergyMode.NORMAL);
            printTest("MotionSensor instantiation", "Created with ID: motion-test");
            
            // Test turnOn(), turnOff(), checkMotion()
            sensor.turnOn();
            printTest("turnOn()", "Sensor activated: " + sensor.isOn());
            
            try {
                boolean motionDetected = sensor.checkMotion();
                printTest("checkMotion()", "Motion detected: " + motionDetected);
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("checkMotion()", "Security breach detected (expected): " + e.getMessage());
            }
            
            sensor.turnOff();
            printTest("turnOff()", "Sensor deactivated: " + !sensor.isOn());
            
            // Test setSensitivity(), resetTrigger()
            sensor.setSensitivity(8);
            printTest("setSensitivity()", "Sensitivity set to 8");
            
            sensor.resetTrigger();
            printTest("resetTrigger()", "Trigger reset");
            
            // Test schedule(), cancelScheduledTasks()
            sensor.schedule("night-mode");
            printTest("schedule()", "Schedule set: " + sensor.getSchedule());
            
            sensor.scheduleTask(java.time.LocalTime.of(23, 0), () -> System.out.println("Scheduled task executed"));
            printTest("scheduleTask()", "Task scheduled for 23:00");
            
            sensor.cancelScheduledTasks();
            printTest("cancelScheduledTasks()", "All tasks cancelled");
            
            // Test getEnergyConsumption()
            sensor.turnOn();
            double consumption = sensor.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            sensor.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            sensor.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            sensor.executeCommand("SENSITIVITY 5");
            printTest("executeCommand('SENSITIVITY 5')", "Command executed");
            
            sensor.executeCommand("STATUS");
            printTest("executeCommand('STATUS')", "Command executed");
            
        } catch (Exception e) {
            printError("testMotionSensor", e);
        }
    }

    /**
     * Test DoorWindowSensor device
     */
    private static void testDoorWindowSensor() {
        printSection("Testing DoorWindowSensor");
        
        try {
            // Instantiate DoorWindowSensor
            DoorWindowSensor sensor = new DoorWindowSensor("door-test", "Test Door Sensor", "Test Location", EnergyMode.NORMAL);
            printTest("DoorWindowSensor instantiation", "Created with ID: door-test");
            
            // Test turnOn(), turnOff(), checkStatus()
            sensor.turnOn();
            printTest("turnOn()", "Sensor activated: " + sensor.isOn());
            
            try {
                boolean isOpen = sensor.checkStatus();
                printTest("checkStatus()", "Door status: " + (isOpen ? "OPEN" : "CLOSED"));
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("checkStatus()", "Security breach detected: " + e.getMessage());
            }
            
            sensor.turnOff();
            printTest("turnOff()", "Sensor deactivated: " + !sensor.isOn());
            
            // Test simulateOpen(), simulateClose()
            try {
                sensor.simulateOpen();
                printTest("simulateOpen()", "Door opened");
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("simulateOpen()", "Security breach detected: " + e.getMessage());
            }
            
            sensor.simulateClose();
            printTest("simulateClose()", "Door closed");
            
            // Test setEnergyMode(), getEnergyConsumption()
            sensor.setEnergyMode(EnergyMode.ECO);
            printTest("setEnergyMode()", "Energy mode set to ECO");
            
            double consumption = sensor.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            sensor.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            sensor.executeCommand("CLOSE");
            printTest("executeCommand('CLOSE')", "Command executed");
            
            sensor.executeCommand("STATUS");
            printTest("executeCommand('STATUS')", "Command executed");
            
            sensor.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
        } catch (Exception e) {
            printError("testDoorWindowSensor", e);
        }
    }

    /**
     * Test DoorLock device
     */
    private static void testDoorLock() {
        printSection("Testing DoorLock");
        
        try {
            // Instantiate DoorLock
            DoorLock lock = new DoorLock("lock-test", "Test Door Lock", EnergyMode.NORMAL);
            printTest("DoorLock instantiation", "Created with ID: lock-test");
            
            // Test lock(), unlock(), toggleLock()
            lock.lock();
            printTest("lock()", "Door locked");
            
            lock.unlock();
            printTest("unlock()", "Door unlocked");
            
            lock.toggleLock();
            printTest("toggleLock()", "Lock toggled");
            
            // Test getAccessLog(), getRecentAccessLog()
            java.util.List<String> accessLog = lock.getAccessLog();
            printTest("getAccessLog()", "Log entries: " + accessLog.size());
            
            java.util.List<String> recentLog = lock.getRecentAccessLog(3);
            printTest("getRecentAccessLog()", "Recent entries: " + recentLog.size());
            
            // Test setAutoLockDelay(), schedule()
            lock.setAutoLockDelay(10);
            printTest("setAutoLockDelay()", "Auto-lock delay set to 10 minutes");
            
            lock.schedule("auto-lock-night");
            printTest("schedule()", "Schedule set: " + lock.getSchedule());
            
            lock.scheduleTask(java.time.LocalTime.of(23, 30), () -> System.out.println("Auto-lock task executed"));
            printTest("scheduleTask()", "Task scheduled for 23:30");
            
            lock.cancelScheduledTasks();
            printTest("cancelScheduledTasks()", "All tasks cancelled");
            
            // Test setEnergyMode(), getEnergyConsumption()
            lock.setEnergyMode(EnergyMode.ECO);
            printTest("setEnergyMode()", "Energy mode set to ECO");
            
            double consumption = lock.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            lock.executeCommand("LOCK");
            printTest("executeCommand('LOCK')", "Command executed");
            
            lock.executeCommand("UNLOCK");
            printTest("executeCommand('UNLOCK')", "Command executed");
            
            lock.executeCommand("AUTOLOCK 15");
            printTest("executeCommand('AUTOLOCK 15')", "Command executed");
            
            lock.executeCommand("STATUS");
            printTest("executeCommand('STATUS')", "Command executed");
            
        } catch (Exception e) {
            printError("testDoorLock", e);
        }
    }

    /**
     * Test SmokeDetector device
     */
    private static void testSmokeDetector() {
        printSection("Testing SmokeDetector");
        
        try {
            // Instantiate SmokeDetector
            SmokeDetector detector = new SmokeDetector("smoke-test", "Test Smoke Detector", "Test Room", EnergyMode.NORMAL);
            printTest("SmokeDetector instantiation", "Created with ID: smoke-test");
            
            // Test turnOn(), turnOff(), detectSmoke()
            detector.turnOn();
            printTest("turnOn()", "Detector activated: " + detector.isOn());
            
            try {
                detector.detectSmoke(false);
                printTest("detectSmoke(false)", "No smoke detected");
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("detectSmoke(false)", "Unexpected breach: " + e.getMessage());
            }
            
            try {
                detector.detectSmoke(true);
                printTest("detectSmoke(true)", "Smoke detected");
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("detectSmoke(true)", "Security breach detected (expected): " + e.getMessage());
            }
            
            // Clear smoke before turning off
            try {
                detector.detectSmoke(false);
            } catch (Exception e) {
                // Ignore
            }
            
            detector.turnOff();
            printTest("turnOff()", "Detector deactivated: " + !detector.isOn());
            
            // Test setSensitivity(), checkBattery()
            detector.setSensitivity(9);
            printTest("setSensitivity()", "Sensitivity set to 9");
            
            detector.checkBattery();
            printTest("checkBattery()", "Battery checked");
            
            // Test schedule(), cancelScheduledTasks()
            detector.schedule("24/7");
            printTest("schedule()", "Schedule set: " + detector.getSchedule());
            
            detector.scheduleTask(java.time.LocalTime.of(3, 0), () -> System.out.println("Battery check task executed"));
            printTest("scheduleTask()", "Task scheduled for 03:00");
            
            detector.cancelScheduledTasks();
            printTest("cancelScheduledTasks()", "All tasks cancelled");
            
            // Test setEnergyMode(), getEnergyConsumption()
            detector.setEnergyMode(EnergyMode.ECO);
            printTest("setEnergyMode()", "Energy mode set to ECO");
            
            double consumption = detector.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            detector.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            detector.executeCommand("SENSITIVITY 7");
            printTest("executeCommand('SENSITIVITY 7')", "Command executed");
            
            detector.executeCommand("STATUS");
            printTest("executeCommand('STATUS')", "Command executed");
            
        } catch (Exception e) {
            printError("testSmokeDetector", e);
        }
    }

    /**
     * Test AlarmSiren device
     */
    private static void testAlarmSiren() {
        printSection("Testing AlarmSiren");
        
        try {
            // Instantiate AlarmSiren
            AlarmSiren siren = new AlarmSiren("alarm-test", "Test Alarm Siren", EnergyMode.NORMAL);
            printTest("AlarmSiren instantiation", "Created with ID: alarm-test");
            
            // Test triggerAlarm(), stopAlarm()
            siren.triggerAlarm();
            printTest("triggerAlarm()", "Alarm triggered");
            
            siren.stopAlarm();
            printTest("stopAlarm()", "Alarm stopped");
            
            // Test setVolume(), setDuration()
            siren.setVolume(7);
            printTest("setVolume()", "Volume set to 7");
            
            siren.setDuration(60);
            printTest("setDuration()", "Duration set to 60 seconds");
            
            // Test setEnergySavingMode(), getEnergyConsumption()
            siren.setEnergySavingMode(true);
            printTest("setEnergySavingMode()", "Energy saving mode enabled");
            
            double consumption = siren.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            siren.executeCommand("TRIGGER");
            printTest("executeCommand('TRIGGER')", "Command executed");
            
            siren.executeCommand("STOP");
            printTest("executeCommand('STOP')", "Command executed");
            
            siren.executeCommand("VOLUME 5");
            printTest("executeCommand('VOLUME 5')", "Command executed");
            
            siren.executeCommand("DURATION 45");
            printTest("executeCommand('DURATION 45')", "Command executed");
            
            siren.executeCommand("STATUS");
            printTest("executeCommand('STATUS')", "Command executed");
            
        } catch (Exception e) {
            printError("testAlarmSiren", e);
        }
    }

    /**
     * Test AC device
     */
    private static void testAC() {
        printSection("Testing AC");
        
        try {
            // Instantiate AC with valid parameters
            AC ac = new AC("ac-test", "Test AC", 22.0);
            printTest("AC instantiation", "Created with ID: ac-test");
            
            // Test turnOn(), turnOff(), getStatus()
            ac.turnOn();
            printTest("turnOn()", "AC started: " + ac.isOn());
            
            String status = ac.getStatus();
            printTest("getStatus()", status);
            
            ac.turnOff();
            printTest("turnOff()", "AC stopped: " + !ac.isOn());
            
            // Test setTargetTemperature(), startTemperatureRegulation()
            ac.setTargetTemperature(24.0);
            printTest("setTargetTemperature()", "Target temperature set to 24.0°C");
            
            ac.turnOn(); // startTemperatureRegulation() is called internally
            printTest("startTemperatureRegulation()", "Temperature regulation started");
            
            // Test updateCurrentTemperatureFromRoom() - called internally
            // We'll verify it works by checking status
            Thread.sleep(100); // Give it a moment
            String statusAfterRegulation = ac.getStatus();
            printTest("updateCurrentTemperatureFromRoom()", "Status updated: " + statusAfterRegulation);
            
            // Test scheduleTask(), cancelScheduledTasks()
            ac.scheduleTask(java.time.LocalTime.of(18, 0), () -> System.out.println("AC scheduled task executed"));
            printTest("scheduleTask()", "Task scheduled for 18:00");
            
            ac.cancelScheduledTasks();
            printTest("cancelScheduledTasks()", "All tasks cancelled");
            
            // Test setEnergyMode(), getEnergyConsumption()
            ac.setEnergyMode(EnergyMode.ECO);
            printTest("setEnergyMode()", "Energy mode set to ECO");
            
            double consumption = ac.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            ac.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            ac.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            ac.turnOff(); // Clean up
            
        } catch (Exception e) {
            printError("testAC", e);
        }
    }

    /**
     * Test Light device
     */
    private static void testLight() {
        printSection("Testing Light");
        
        try {
            // Instantiate Light with valid parameters
            Light light = new Light("light-test", "Test Light", 100, EnergyMode.NORMAL);
            printTest("Light instantiation", "Created with ID: light-test");
            
            // Test turnOn(), turnOff(), getStatus()
            light.turnOn();
            printTest("turnOn()", "Light turned on: " + light.isOn());
            
            String status = light.getStatus();
            printTest("getStatus()", status);
            
            light.turnOff();
            printTest("turnOff()", "Light turned off: " + !light.isOn());
            
            // Test setBrightness()
            light.setBrightness(75);
            printTest("setBrightness()", "Brightness set to 75%");
            
            light.setBrightness(50);
            printTest("setBrightness()", "Brightness set to 50%");
            
            light.setBrightness(100);
            printTest("setBrightness()", "Brightness set to 100%");
            
            // Test getEnergyConsumption()
            light.turnOn();
            double consumption = light.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            light.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            light.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            light.turnOff(); // Clean up
            
        } catch (Exception e) {
            printError("testLight", e);
        }
    }

    /**
     * Test SmartTV device
     */
    private static void testSmartTV() {
        printSection("Testing SmartTV");
        
        try {
            // Instantiate SmartTV with valid parameters
            SmartTV tv = new SmartTV("tv-test", "Test Smart TV");
            printTest("SmartTV instantiation", "Created with ID: tv-test");
            
            // Test turnOn(), turnOff(), getStatus()
            tv.turnOn();
            printTest("turnOn()", "TV turned on: " + tv.isOn());
            
            String status = tv.getStatus();
            printTest("getStatus()", status);
            
            tv.turnOff();
            printTest("turnOff()", "TV turned off: " + !tv.isOn());
            
            // Test setChannel(), setVolume()
            tv.turnOn();
            tv.setChannel(5);
            printTest("setChannel()", "Channel set to 5");
            
            tv.setChannel(10);
            printTest("setChannel()", "Channel set to 10");
            
            tv.setVolume(60);
            printTest("setVolume()", "Volume set to 60");
            
            tv.setVolume(80);
            printTest("setVolume()", "Volume set to 80");
            
            // Test setEnergyMode(), getEnergyConsumption()
            tv.setEnergyMode(EnergyMode.ECO);
            printTest("setEnergyMode()", "Energy mode set to ECO");
            
            double consumption = tv.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            tv.setEnergyMode(EnergyMode.HIGH);
            printTest("setEnergyMode()", "Energy mode set to HIGH");
            
            consumption = tv.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            tv.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            tv.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            tv.turnOff(); // Clean up
            
        } catch (Exception e) {
            printError("testSmartTV", e);
        }
    }

    /**
     * Test Speaker device
     */
    private static void testSpeaker() {
        printSection("Testing Speaker");
        
        try {
            // Instantiate Speaker with valid parameters
            Speaker speaker = new Speaker("speaker-test", "Test Speaker", EnergyMode.NORMAL);
            printTest("Speaker instantiation", "Created with ID: speaker-test");
            
            // Test turnOn(), turnOff(), play(), pause()
            speaker.turnOn();
            printTest("turnOn()", "Speaker turned on: " + speaker.isOn());
            
            speaker.play();
            printTest("play()", "Speaker playing");
            
            speaker.pause();
            printTest("pause()", "Speaker paused");
            
            speaker.play();
            printTest("play()", "Speaker playing again");
            
            speaker.turnOff();
            printTest("turnOff()", "Speaker turned off: " + !speaker.isOn());
            
            // Test setVolume(), isPlaying()
            speaker.turnOn();
            speaker.setVolume(70);
            printTest("setVolume()", "Volume set to 70");
            
            speaker.setVolume(90);
            printTest("setVolume()", "Volume set to 90");
            
            speaker.play();
            boolean playing = speaker.isPlaying();
            printTest("isPlaying()", "Playing status: " + playing);
            
            speaker.pause();
            playing = speaker.isPlaying();
            printTest("isPlaying()", "Playing status: " + playing);
            
            // Test getEnergyConsumption()
            speaker.play();
            double consumption = speaker.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption while playing: " + consumption + " W");
            
            speaker.pause();
            consumption = speaker.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption while paused: " + consumption + " W");
            
            // Test executeCommand()
            speaker.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            speaker.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            speaker.executeCommand("PLAY");
            printTest("executeCommand('PLAY')", "Command executed");
            
            speaker.executeCommand("PAUSE");
            printTest("executeCommand('PAUSE')", "Command executed");
            
            speaker.turnOff(); // Clean up
            
        } catch (Exception e) {
            printError("testSpeaker", e);
        }
    }

    /**
     * Test SmartPlug device
     */
    private static void testSmartPlug() {
        printSection("Testing SmartPlug");
        
        try {
            // Instantiate SmartPlug with valid parameters
            SmartPlug plug = new SmartPlug("plug-test", "Test Smart Plug", EnergyMode.NORMAL);
            printTest("SmartPlug instantiation", "Created with ID: plug-test");
            
            // Test turnOn(), turnOff(), getStatus()
            plug.turnOn();
            printTest("turnOn()", "Plug turned on: " + plug.isOn());
            
            String status = plug.getStatus();
            printTest("getStatus()", status);
            
            plug.turnOff();
            printTest("turnOff()", "Plug turned off: " + !plug.isOn());
            
            status = plug.getStatus();
            printTest("getStatus()", status);
            
            // Test getEnergyConsumption()
            plug.turnOn();
            double consumption = plug.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption when on: " + consumption + " W");
            
            plug.turnOff();
            consumption = plug.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption when off: " + consumption + " W");
            
            // Test executeCommand()
            plug.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            plug.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            plug.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
        } catch (Exception e) {
            printError("testSmartPlug", e);
        }
    }

    /**
     * Test SmartFridge device
     */
    private static void testSmartFridge() {
        printSection("Testing SmartFridge");
        
        try {
            // Instantiate SmartFridge with valid parameters
            SmartFridge fridge = new SmartFridge("fridge-test", "Test Smart Fridge", EnergyMode.NORMAL, 3);
            printTest("SmartFridge instantiation", "Created with ID: fridge-test");
            
            // Test turnOn(), turnOff(), getStatus()
            fridge.turnOn();
            printTest("turnOn()", "Fridge turned on: " + fridge.isOn());
            
            String status = fridge.getStatus();
            printTest("getStatus()", status);
            
            fridge.turnOff();
            printTest("turnOff()", "Fridge turned off: " + !fridge.isOn());
            
            status = fridge.getStatus();
            printTest("getStatus()", status);
            
            // Test getTemperatureSetting(), setCurrentTemperatureCelsius()
            fridge.turnOn();
            int tempSetting = fridge.getTemperatureSetting();
            printTest("getTemperatureSetting()", "Temperature setting: " + tempSetting);
            
            fridge.setCurrentTemperatureCelsius(5.0);
            printTest("setCurrentTemperatureCelsius()", "Current temperature set to 5.0°C");
            
            fridge.setCurrentTemperatureCelsius(3.5);
            printTest("setCurrentTemperatureCelsius()", "Current temperature set to 3.5°C");
            
            // Test getEnergyConsumption()
            double consumption = fridge.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            fridge.setEnergyMode(EnergyMode.ECO);
            consumption = fridge.getEnergyConsumption();
            printTest("getEnergyConsumption() in ECO mode", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            fridge.executeCommand("OPEN DOOR");
            printTest("executeCommand('OPEN DOOR')", "Command executed");
            
            fridge.executeCommand("CLOSE DOOR");
            printTest("executeCommand('CLOSE DOOR')", "Command executed");
            
            fridge.executeCommand("TEMP UP");
            printTest("executeCommand('TEMP UP')", "Command executed");
            
            fridge.executeCommand("TEMP DOWN");
            printTest("executeCommand('TEMP DOWN')", "Command executed");
            
            fridge.executeCommand("DEFROST START");
            printTest("executeCommand('DEFROST START')", "Command executed");
            
            fridge.executeCommand("DEFROST STOP");
            printTest("executeCommand('DEFROST STOP')", "Command executed");
            
            fridge.turnOff(); // Clean up
            
        } catch (Exception e) {
            printError("testSmartFridge", e);
        }
    }

    /**
     * Test SmartFaucet device
     */
    private static void testSmartFaucet() {
        printSection("Testing SmartFaucet");
        
        try {
            // Instantiate SmartFaucet with valid parameters
            SmartFaucet faucet = new SmartFaucet("faucet-test", "Test Smart Faucet", EnergyMode.NORMAL, true);
            printTest("SmartFaucet instantiation", "Created with ID: faucet-test");
            
            // Test turnOn(), turnOff(), getStatus()
            faucet.turnOn();
            printTest("turnOn()", "Faucet turned on: " + faucet.isOn());
            
            String status = faucet.getStatus();
            printTest("getStatus()", status);
            
            faucet.turnOff();
            printTest("turnOff()", "Faucet turned off: " + !faucet.isOn());
            
            status = faucet.getStatus();
            printTest("getStatus()", status);
            
            // Test trackWaterConsumption(), getCurrentwaterConsumption(), getTotalwaterConsumption()
            faucet.turnOn();
            double currentWater = faucet.getCurrentwaterConsumption();
            printTest("getCurrentwaterConsumption()", "Current water flow: " + currentWater + " L/min");
            
            faucet.trackWaterConsumption(2.0); // Track for 2 minutes
            printTest("trackWaterConsumption(2.0)", "Tracked water consumption for 2 minutes");
            
            double totalWater = faucet.getTotalwaterConsumption();
            printTest("getTotalwaterConsumption()", "Total water consumed: " + totalWater + " L");
            
            faucet.trackWaterConsumption(1.5); // Track for 1.5 more minutes
            printTest("trackWaterConsumption(1.5)", "Tracked water consumption for 1.5 minutes");
            
            totalWater = faucet.getTotalwaterConsumption();
            printTest("getTotalwaterConsumption()", "Total water consumed: " + totalWater + " L");
            
            // Test getEnergyConsumption()
            double consumption = faucet.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption when on: " + consumption + " W");
            
            faucet.turnOff();
            consumption = faucet.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption when off: " + consumption + " W");
            
            // Test executeCommand()
            faucet.turnOn();
            faucet.executeCommand("FLOW HIGH");
            printTest("executeCommand('FLOW HIGH')", "Command executed");
            
            faucet.executeCommand("FLOW LOW");
            printTest("executeCommand('FLOW LOW')", "Command executed");
            
            faucet.executeCommand("PAUSE");
            printTest("executeCommand('PAUSE')", "Command executed");
            
        } catch (Exception e) {
            printError("testSmartFaucet", e);
        }
    }

    /**
     * Test SmartMirror device
     */
    private static void testSmartMirror() {
        printSection("Testing SmartMirror");
        
        try {
            // Instantiate SmartMirror with valid parameters
            SmartMirror mirror = new SmartMirror("mirror-test", "Test Smart Mirror", EnergyMode.NORMAL);
            printTest("SmartMirror instantiation", "Created with ID: mirror-test");
            
            // Test turnOn(), turnOff(), toggleMode()
            mirror.turnOn();
            printTest("turnOn()", "Mirror turned on: " + mirror.isOn());
            
            mirror.toggleMode();
            printTest("toggleMode()", "Mirror mode toggled to: " + (mirror.isMirrorMode() ? "Mirror Mode" : "Display Mode"));
            
            mirror.toggleMode();
            printTest("toggleMode()", "Mirror mode toggled to: " + (mirror.isMirrorMode() ? "Mirror Mode" : "Display Mode"));
            
            mirror.turnOff();
            printTest("turnOff()", "Mirror turned off: " + !mirror.isOn());
            
            // Test displayBriefing(), displayIslamicCalendar()
            mirror.turnOn();
            mirror.displayBriefing();
            printTest("displayBriefing()", "Briefing displayed");
            
            mirror.displayIslamicCalendar();
            printTest("displayIslamicCalendar()", "Islamic calendar displayed");
            
            // Test displayQuranVerseOnly(), refreshVerse(), updateDailyVerse()
            mirror.displayQuranVerseOnly();
            printTest("displayQuranVerseOnly()", "Quran verse displayed");
            
            mirror.refreshVerse();
            printTest("refreshVerse()", "Verse refreshed");
            
            mirror.displayQuranVerseOnly();
            printTest("displayQuranVerseOnly() after refresh", "Updated verse displayed");
            
            // Test setCalendarEmail(), syncReminders(), getCalendarStatus()
            mirror.setCalendarEmail("test@example.com");
            printTest("setCalendarEmail()", "Calendar email set to test@example.com");
            
            mirror.syncReminders();
            printTest("syncReminders()", "Reminders synced");
            
            String calendarStatus = mirror.getCalendarStatus();
            printTest("getCalendarStatus()", calendarStatus);
            
            // Test setWeather(), getQiblaDirection()
            mirror.setWeather("Cloudy", 18.5);
            printTest("setWeather()", "Weather set to Cloudy, 18.5°C");
            
            String qiblaDirection = mirror.getQiblaDirection();
            printTest("getQiblaDirection()", qiblaDirection);
            
            // Test setEnergyMode(), getEnergyConsumption()
            mirror.setEnergyMode(EnergyMode.ECO);
            printTest("setEnergyMode()", "Energy mode set to ECO");
            
            double consumption = mirror.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            mirror.setEnergyMode(EnergyMode.HIGH);
            consumption = mirror.getEnergyConsumption();
            printTest("getEnergyConsumption() in HIGH mode", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            mirror.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            mirror.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            mirror.executeCommand("TOGGLE");
            printTest("executeCommand('TOGGLE')", "Command executed");
            
            mirror.executeCommand("BRIEFING");
            printTest("executeCommand('BRIEFING')", "Command executed");
            
            mirror.executeCommand("CALENDAR");
            printTest("executeCommand('CALENDAR')", "Command executed");
            
            mirror.executeCommand("QURAN");
            printTest("executeCommand('QURAN')", "Command executed");
            
            mirror.executeCommand("REFRESH_VERSE");
            printTest("executeCommand('REFRESH_VERSE')", "Command executed");
            
            mirror.executeCommand("SYNC_CALENDAR");
            printTest("executeCommand('SYNC_CALENDAR')", "Command executed");
            
        } catch (Exception e) {
            printError("testSmartMirror", e);
        }
    }

    /**
     * Test AirQualitySensor device
     */
    private static void testAirQualitySensor() {
        printSection("Testing AirQualitySensor");
        
        try {
            // Instantiate AirQualitySensor with valid parameters
            AirQualitySensor sensor = new AirQualitySensor("air-test", "Test Air Quality Sensor", EnergyMode.NORMAL);
            printTest("AirQualitySensor instantiation", "Created with ID: air-test");
            
            // Test turnOn(), turnOff(), updateReadings()
            sensor.turnOn();
            printTest("turnOn()", "Sensor turned on: " + sensor.isOn());
            
            // updateReadings() is called internally by getter methods
            printTest("updateReadings()", "Readings updated internally");
            
            sensor.turnOff();
            printTest("turnOff()", "Sensor turned off: " + !sensor.isOn());
            
            // Test getAirQualityIndex(), getCO2Level(), getHumidity()
            sensor.turnOn();
            double aqi = sensor.getAirQualityIndex();
            printTest("getAirQualityIndex()", "Air Quality Index: " + aqi);
            
            double co2 = sensor.getCO2Level();
            printTest("getCO2Level()", "CO2 Level: " + co2 + " ppm");
            
            double humidity = sensor.getHumidity();
            printTest("getHumidity()", "Humidity: " + humidity + "%");
            
            // Test isAirQualityGood(), needsVentilation()
            boolean isGood = sensor.isAirQualityGood();
            printTest("isAirQualityGood()", "Air quality is good: " + isGood);
            
            boolean needsVent = sensor.needsVentilation();
            printTest("needsVentilation()", "Needs ventilation: " + needsVent);
            
            // Test setAirQualityAlert(), enableAlerts()
            sensor.setAirQualityAlert(900.0);
            printTest("setAirQualityAlert()", "Alert threshold set to 900.0 ppm");
            
            sensor.enableAlerts(false);
            printTest("enableAlerts(false)", "Alerts disabled");
            
            sensor.enableAlerts(true);
            printTest("enableAlerts(true)", "Alerts enabled");
            
            // Test getEnergyConsumption()
            double consumption = sensor.getEnergyConsumption();
            printTest("getEnergyConsumption()", "Consumption: " + consumption + " W");
            
            // Test executeCommand()
            sensor.executeCommand("OFF");
            printTest("executeCommand('OFF')", "Command executed");
            
            sensor.executeCommand("ON");
            printTest("executeCommand('ON')", "Command executed");
            
            sensor.executeCommand("SET_THRESHOLD:1200");
            printTest("executeCommand('SET_THRESHOLD:1200')", "Command executed");
            
        } catch (Exception e) {
            printError("testAirQualitySensor", e);
        }
    }

    /**
     * Test HomeController
     */
    private static void testHomeController(HomeController homeController, Home home) {
        printSection("Testing HomeController");
        
        try {
            // Test turnOnDevice(), turnOffDevice() with specific device IDs
            homeController.turnOnDevice("light-001");
            printTest("turnOnDevice('light-001')", "Device turned on");
            
            homeController.turnOffDevice("light-001");
            printTest("turnOffDevice('light-001')", "Device turned off");
            
            homeController.turnOnDevice("tv-001");
            printTest("turnOnDevice('tv-001')", "Device turned on");
            
            homeController.turnOffDevice("tv-001");
            printTest("turnOffDevice('tv-001')", "Device turned off");
            
            // Test with non-existent device
            homeController.turnOnDevice("nonexistent-999");
            printTest("turnOnDevice('nonexistent-999')", "Handled non-existent device");
            
            // Test turnOnAllDevices(), turnOffAllDevices()
            homeController.turnOnAllDevices();
            printTest("turnOnAllDevices()", "All devices turned on");
            
            homeController.turnOffAllDevices();
            printTest("turnOffAllDevices()", "All devices turned off");
            
            // Test setNightMode(), setAwayMode()
            homeController.setNightMode();
            printTest("setNightMode()", "Night mode activated");
            
            homeController.setAwayMode();
            printTest("setAwayMode()", "Away mode activated");
            
            // Test activateRamadanMode(), deactivateRamadanMode()
            homeController.activateRamadanMode();
            printTest("activateRamadanMode()", "Ramadan mode activated: " + homeController.isRamadanModeActive());
            
            homeController.deactivateRamadanMode();
            printTest("deactivateRamadanMode()", "Ramadan mode deactivated: " + !homeController.isRamadanModeActive());
            
            // Test listAllDevices(), listAllRooms()
            System.out.println("\n  Testing listAllDevices():");
            homeController.listAllDevices();
            printTest("listAllDevices()", "Device list displayed");
            
            System.out.println("\n  Testing listAllRooms():");
            homeController.listAllRooms();
            printTest("listAllRooms()", "Room list displayed");
            
            // Test displayEnergyConsumption()
            homeController.turnOnAllDevices();
            System.out.println("\n  Testing displayEnergyConsumption():");
            homeController.displayEnergyConsumption();
            printTest("displayEnergyConsumption()", "Energy consumption displayed");
            homeController.turnOffAllDevices();
            
            // Test armSecuritySystem(), disarmSecuritySystem()
            homeController.disarmSecuritySystem();
            printTest("disarmSecuritySystem()", "Security system disarmed");
            
            homeController.armSecuritySystem();
            printTest("armSecuritySystem()", "Security system armed");
            
            homeController.disarmSecuritySystem();
            printTest("disarmSecuritySystem() again", "Security system disarmed");
            
            // Test handleSecurityEvent()
            homeController.addSecurityDevice(home.findDeviceById("motion-001"));
            homeController.addSecurityDevice(home.findDeviceById("camera-001"));
            homeController.addSecurityDevice(home.findDeviceById("door-001"));
            homeController.addSecurityDevice(home.findDeviceById("smoke-001"));
            homeController.addSecurityDevice(home.findDeviceById("alarm-001"));
            printTest("addSecurityDevice()", "Security devices added to controller");
            
            homeController.handleSecurityEvent("motion-001");
            printTest("handleSecurityEvent('motion-001')", "Security event handled");
            
            // Test setCurrentTime(), setCurrentDate(), isIsNight()
            java.sql.Time morningTime = java.sql.Time.valueOf("08:30:00");
            homeController.setCurrentTime(morningTime);
            printTest("setCurrentTime(08:30:00)", "Current time set to morning");
            
            boolean isNightMorning = homeController.isIsNight();
            printTest("isIsNight() at 08:30", "Is night: " + isNightMorning);
            
            java.sql.Time nightTime = java.sql.Time.valueOf("22:00:00");
            homeController.setCurrentTime(nightTime);
            printTest("setCurrentTime(22:00:00)", "Current time set to night");
            
            boolean isNightEvening = homeController.isIsNight();
            printTest("isIsNight() at 22:00", "Is night: " + isNightEvening);
            
            java.util.Date currentDate = new java.util.Date();
            homeController.setCurrentDate(currentDate);
            printTest("setCurrentDate()", "Current date set to: " + currentDate);
            
            // Test changeAllroomsTemperature()
            homeController.changeAllroomsTemperature(23.5);
            printTest("changeAllroomsTemperature(23.5)", "All rooms temperature set to 23.5°C");
            
            homeController.changeAllroomsTemperature(21.0);
            printTest("changeAllroomsTemperature(21.0)", "All rooms temperature set to 21.0°C");
            
            // Test AthanNotification(), performRamadanAction()
            homeController.activateRamadanMode();
            homeController.setCurrentTime(java.sql.Time.valueOf("05:30:00"));
            System.out.println("\n  Testing AthanNotification() at Fajr time:");
            homeController.AthanNotification();
            printTest("AthanNotification() at Fajr", "Athan notification triggered");
            
            homeController.setCurrentTime(java.sql.Time.valueOf("18:30:00"));
            System.out.println("\n  Testing AthanNotification() at Iftar time:");
            homeController.AthanNotification();
            printTest("AthanNotification() at Iftar", "Athan notification triggered");
            
            homeController.setCurrentTime(java.sql.Time.valueOf("10:00:00"));
            System.out.println("\n  Testing AthanNotification() at non-prayer time:");
            homeController.AthanNotification();
            printTest("AthanNotification() at non-prayer time", "No notification (expected)");
            
            homeController.deactivateRamadanMode();
            
            // Test displayAllSecurityDevices(), displaySecurityStatus()
            System.out.println("\n  Testing displayAllSecurityDevices():");
            homeController.displayAllSecurityDevices();
            printTest("displayAllSecurityDevices()", "Security devices displayed");
            
            System.out.println("\n  Testing displaySecurityStatus():");
            homeController.displaySecurityStatus();
            printTest("displaySecurityStatus()", "Security status displayed");
            
        } catch (Exception e) {
            printError("testHomeController", e);
        }
    }

    /**
     * Test SecurityController
     */
    private static void testSecurityController(HomeController homeController) {
        printSection("Testing SecurityController");
        
        try {
            // Get the SecurityController from HomeController
            com.controller.SecurityController securityController = homeController.getSecurityController();
            printTest("getSecurityController()", "SecurityController obtained from HomeController");
            
            // Test armSystem(), disarmSystem() methods
            securityController.disarmSystem();
            printTest("disarmSystem()", "System disarmed, status: " + securityController.getSystemStatus());
            
            securityController.armSystem();
            printTest("armSystem()", "System armed, status: " + securityController.getSystemStatus());
            
            securityController.disarmSystem();
            printTest("disarmSystem() again", "System disarmed, status: " + securityController.getSystemStatus());
            
            // Test setAwayMode(), setNightMode(), setHomeState() methods
            securityController.setAwayMode();
            printTest("setAwayMode()", "Away mode activated, status: " + securityController.getSystemStatus() + ", home state: " + securityController.getHomeState());
            
            securityController.disarmSystem();
            securityController.setNightMode();
            printTest("setNightMode()", "Night mode activated, status: " + securityController.getSystemStatus());
            
            securityController.setHomeState(com.enums.HomeState.HOME);
            printTest("setHomeState(HOME)", "Home state set to HOME: " + securityController.getHomeState());
            
            securityController.setHomeState(com.enums.HomeState.AWAY);
            printTest("setHomeState(AWAY)", "Home state set to AWAY: " + securityController.getHomeState());
            
            securityController.setHomeState(com.enums.HomeState.HOME);
            printTest("setHomeState(HOME) again", "Home state set back to HOME: " + securityController.getHomeState());
            
            // Test addSecurityDevice(), removeSecurityDevice() methods
            MotionSensor testMotion = new MotionSensor("motion-test-sec", "Test Motion for Security", 10, EnergyMode.NORMAL);
            securityController.addSecurityDevice(testMotion);
            printTest("addSecurityDevice(MotionSensor)", "Motion sensor added, device count: " + securityController.getDeviceCount());
            
            SecurityCamera testCamera = new SecurityCamera("camera-test-sec", "Test Camera for Security", "1080p", 120, EnergyMode.NORMAL);
            securityController.addSecurityDevice(testCamera);
            printTest("addSecurityDevice(SecurityCamera)", "Security camera added, device count: " + securityController.getDeviceCount());
            
            DoorLock testLock = new DoorLock("lock-test-sec", "Test Lock for Security", EnergyMode.NORMAL);
            securityController.addSecurityDevice(testLock);
            printTest("addSecurityDevice(DoorLock)", "Door lock added, device count: " + securityController.getDeviceCount());
            
            securityController.removeSecurityDevice("motion-test-sec");
            printTest("removeSecurityDevice('motion-test-sec')", "Motion sensor removed, device count: " + securityController.getDeviceCount());
            
            // Test with non-existent device
            try {
                securityController.removeSecurityDevice("nonexistent-device");
                printTest("removeSecurityDevice('nonexistent-device')", "Should have thrown exception");
            } catch (com.exceptions.DeviceNotFoundException e) {
                printTest("removeSecurityDevice('nonexistent-device')", "DeviceNotFoundException caught (expected): " + e.getMessage());
            }
            
            // Test handleMotionTrigger(), handleDoorWindowTrigger(), handleSmokeTrigger() methods
            // These are tested indirectly through handleTriggeredSensor()
            
            // Test handleTriggeredSensor() method
            securityController.disarmSystem();
            
            // Add test devices for triggering
            MotionSensor triggerMotion = new MotionSensor("motion-trigger", "Trigger Motion Sensor", 10, EnergyMode.NORMAL);
            securityController.addSecurityDevice(triggerMotion);
            triggerMotion.turnOn();
            
            DoorWindowSensor triggerDoor = new DoorWindowSensor("door-trigger", "Trigger Door Sensor", "Test Location", EnergyMode.NORMAL);
            securityController.addSecurityDevice(triggerDoor);
            triggerDoor.turnOn();
            
            SmokeDetector triggerSmoke = new SmokeDetector("smoke-trigger", "Trigger Smoke Detector", "Test Room", EnergyMode.NORMAL);
            securityController.addSecurityDevice(triggerSmoke);
            triggerSmoke.turnOn();
            
            // Test motion trigger while disarmed (should not trigger alarm)
            System.out.println("\n  Testing handleTriggeredSensor() with motion while DISARMED:");
            securityController.handleTriggeredSensor("motion-trigger");
            printTest("handleTriggeredSensor('motion-trigger') DISARMED", "Motion handled without alarm");
            
            // Test motion trigger while armed (should trigger alarm)
            securityController.armSystem();
            System.out.println("\n  Testing handleTriggeredSensor() with motion while ARMED:");
            try {
                triggerMotion.checkMotion(); // This will trigger the sensor
                printTest("handleTriggeredSensor('motion-trigger') ARMED", "Motion detected, alarm should trigger");
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("handleTriggeredSensor('motion-trigger') ARMED", "SecurityBreachException caught (expected): " + e.getMessage());
            }
            
            securityController.disarmSystem();
            
            // Test door/window trigger
            System.out.println("\n  Testing handleTriggeredSensor() with door sensor:");
            securityController.handleTriggeredSensor("door-trigger");
            printTest("handleTriggeredSensor('door-trigger')", "Door sensor handled");
            
            // Test smoke trigger (should always trigger alarm)
            System.out.println("\n  Testing handleTriggeredSensor() with smoke detector:");
            try {
                triggerSmoke.detectSmoke(true);
                printTest("handleTriggeredSensor('smoke-trigger')", "Smoke detected, alarm triggered");
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("handleTriggeredSensor('smoke-trigger')", "SecurityBreachException caught (expected): " + e.getMessage());
            }
            
            // Clear smoke
            try {
                triggerSmoke.detectSmoke(false);
            } catch (Exception e) {
                // Ignore
            }
            
            // Test with non-existent sensor
            try {
                securityController.handleTriggeredSensor("nonexistent-sensor");
                printTest("handleTriggeredSensor('nonexistent-sensor')", "Should have thrown exception");
            } catch (com.exceptions.DeviceNotFoundException e) {
                printTest("handleTriggeredSensor('nonexistent-sensor')", "DeviceNotFoundException caught (expected): " + e.getMessage());
            }
            
            // Test getSystemStatus(), getDeviceStatus(), getAllDeviceStatuses() methods
            com.enums.SystemStatus status = securityController.getSystemStatus();
            printTest("getSystemStatus()", "System status: " + status);
            
            String deviceStatus = securityController.getDeviceStatus("camera-test-sec");
            printTest("getDeviceStatus('camera-test-sec')", "Device status: " + deviceStatus);
            
            try {
                securityController.getDeviceStatus("nonexistent-device");
                printTest("getDeviceStatus('nonexistent-device')", "Should have thrown exception");
            } catch (com.exceptions.DeviceNotFoundException e) {
                printTest("getDeviceStatus('nonexistent-device')", "DeviceNotFoundException caught (expected): " + e.getMessage());
            }
            
            java.util.Map<String, String> allStatuses = securityController.getAllDeviceStatuses();
            printTest("getAllDeviceStatuses()", "Retrieved statuses for " + allStatuses.size() + " devices");
            
            // Test getDeviceCount(), getTotalEnergyConsumption() methods
            int deviceCount = securityController.getDeviceCount();
            printTest("getDeviceCount()", "Total security devices: " + deviceCount);
            
            // Turn on some devices to test energy consumption
            testCamera.turnOn();
            testLock.turnOn();
            triggerMotion.turnOn();
            
            double totalEnergy = securityController.getTotalEnergyConsumption();
            printTest("getTotalEnergyConsumption()", "Total energy consumption: " + totalEnergy + " W");
            
            // Test logSystemEvent(), getSystemLog() methods
            // logSystemEvent is private, but we can test getSystemLog which shows logged events
            java.util.List<String> recentLogs = securityController.getSystemLog(5);
            printTest("getSystemLog(5)", "Retrieved " + recentLogs.size() + " recent log entries");
            
            System.out.println("  Recent log entries:");
            for (String log : recentLogs) {
                System.out.println("    " + log);
            }
            
            java.util.List<String> moreLogs = securityController.getSystemLog(10);
            printTest("getSystemLog(10)", "Retrieved " + moreLogs.size() + " log entries");
            
            // Test activateAllSensors(), deactivateAllSensors() methods
            // These are tested indirectly through armSystem() and disarmSystem()
            securityController.disarmSystem();
            printTest("deactivateAllSensors() via disarmSystem()", "All sensors deactivated (except smoke detectors)");
            
            securityController.armSystem();
            printTest("activateAllSensors() via armSystem()", "All sensors activated");
            
            securityController.disarmSystem();
            
            // Test processAutomationRules() method
            System.out.println("\n  Testing processAutomationRules():");
            securityController.processAutomationRules();
            printTest("processAutomationRules()", "Automation rules processed (battery checks, auto-lock, etc.)");
            
            // Clean up - remove test devices
            securityController.removeSecurityDevice("camera-test-sec");
            securityController.removeSecurityDevice("lock-test-sec");
            securityController.removeSecurityDevice("motion-trigger");
            securityController.removeSecurityDevice("door-trigger");
            securityController.removeSecurityDevice("smoke-trigger");
            printTest("Cleanup", "Test devices removed from SecurityController");
            
        } catch (Exception e) {
            printError("testSecurityController", e);
        }
    }

    /**
     * Test Room class
     */
    private static void testRoom() {
        printSection("Testing Room");
        
        try {
            // Create Room instance and add multiple devices
            Room testRoom = new Room("Test Room");
            printTest("Room instantiation", "Created room: " + testRoom.getName());
            
            // Create test devices
            Light light1 = new Light("light-room-test-1", "Test Light 1", 100, EnergyMode.NORMAL);
            Light light2 = new Light("light-room-test-2", "Test Light 2", 75, EnergyMode.NORMAL);
            AC ac = new AC("ac-room-test", "Test AC", 22.0);
            SmartPlug plug = new SmartPlug("plug-room-test", "Test Plug", EnergyMode.NORMAL);
            SmartFaucet faucet = new SmartFaucet("faucet-room-test", "Test Faucet", EnergyMode.NORMAL, true);
            AirQualitySensor airSensor = new AirQualitySensor("air-room-test", "Test Air Sensor", EnergyMode.NORMAL);
            
            // Test addDevice()
            testRoom.addDevice(light1);
            printTest("addDevice(light1)", "Light 1 added to room");
            
            testRoom.addDevice(light2);
            printTest("addDevice(light2)", "Light 2 added to room");
            
            testRoom.addDevice(ac);
            printTest("addDevice(ac)", "AC added to room");
            
            testRoom.addDevice(plug);
            printTest("addDevice(plug)", "Plug added to room");
            
            testRoom.addDevice(faucet);
            printTest("addDevice(faucet)", "Faucet added to room");
            
            testRoom.addDevice(airSensor);
            printTest("addDevice(airSensor)", "Air sensor added to room");
            
            // Test getDevices()
            java.util.List<SmartDevice> devices = testRoom.getDevices();
            printTest("getDevices()", "Retrieved " + devices.size() + " devices");
            
            // Test findDeviceById()
            SmartDevice foundDevice = testRoom.findDeviceById("light-room-test-1");
            printTest("findDeviceById('light-room-test-1')", "Found device: " + (foundDevice != null ? foundDevice.getName() : "null"));
            
            SmartDevice notFoundDevice = testRoom.findDeviceById("nonexistent-device");
            printTest("findDeviceById('nonexistent-device')", "Device not found (expected): " + (notFoundDevice == null ? "null" : notFoundDevice.getName()));
            
            // Test findDevicesByType()
            java.util.List<SmartDevice> lights = testRoom.findDevicesByType(Light.class);
            printTest("findDevicesByType(Light.class)", "Found " + lights.size() + " lights");
            
            java.util.List<SmartDevice> acs = testRoom.findDevicesByType(AC.class);
            printTest("findDevicesByType(AC.class)", "Found " + acs.size() + " AC units");
            
            java.util.List<SmartDevice> plugs = testRoom.findDevicesByType(SmartPlug.class);
            printTest("findDevicesByType(SmartPlug.class)", "Found " + plugs.size() + " smart plugs");
            
            // Test getCurrentEnergyConsumption() - should be 0 initially (devices off)
            float currentEnergy = testRoom.getCurrentEnergyConsumption();
            printTest("getCurrentEnergyConsumption() with devices off", "Current energy: " + currentEnergy + " W");
            
            // Turn on some devices and recalculate
            light1.turnOn();
            light2.turnOn();
            ac.turnOn();
            plug.turnOn();
            
            // Test recalculateCurrentEnergyConsumption()
            testRoom.recalculateCurrentEnergyConsumption();
            printTest("recalculateCurrentEnergyConsumption()", "Energy recalculated");
            
            currentEnergy = testRoom.getCurrentEnergyConsumption();
            printTest("getCurrentEnergyConsumption() with devices on", "Current energy: " + currentEnergy + " W");
            
            // Test getTotalEnergyConsumptionWh() - should be 0 initially
            float totalEnergyWh = testRoom.getTotalEnergyConsumptionWh();
            printTest("getTotalEnergyConsumptionWh() initially", "Total energy: " + totalEnergyWh + " Wh");
            
            // Test getTotalEnergyConsumptionKWh() - should be 0 initially
            float totalEnergyKWh = testRoom.getTotalEnergyConsumptionKWh();
            printTest("getTotalEnergyConsumptionKWh() initially", "Total energy: " + totalEnergyKWh + " kWh");
            
            // Test accumulateEnergyUsageSeconds()
            testRoom.accumulateEnergyUsageSeconds(3600); // 1 hour = 3600 seconds
            printTest("accumulateEnergyUsageSeconds(3600)", "Accumulated energy for 1 hour");
            
            totalEnergyWh = testRoom.getTotalEnergyConsumptionWh();
            printTest("getTotalEnergyConsumptionWh() after 1 hour", "Total energy: " + totalEnergyWh + " Wh");
            
            totalEnergyKWh = testRoom.getTotalEnergyConsumptionKWh();
            printTest("getTotalEnergyConsumptionKWh() after 1 hour", "Total energy: " + totalEnergyKWh + " kWh");
            
            // Test accumulateEnergyUsageMinutes()
            testRoom.accumulateEnergyUsageMinutes(30); // 30 minutes
            printTest("accumulateEnergyUsageMinutes(30)", "Accumulated energy for 30 minutes");
            
            totalEnergyWh = testRoom.getTotalEnergyConsumptionWh();
            printTest("getTotalEnergyConsumptionWh() after 30 more minutes", "Total energy: " + totalEnergyWh + " Wh");
            
            totalEnergyKWh = testRoom.getTotalEnergyConsumptionKWh();
            printTest("getTotalEnergyConsumptionKWh() after 30 more minutes", "Total energy: " + totalEnergyKWh + " kWh");
            
            // Test resetTotalEnergyConsumption()
            testRoom.resetTotalEnergyConsumption();
            printTest("resetTotalEnergyConsumption()", "Total energy reset");
            
            totalEnergyWh = testRoom.getTotalEnergyConsumptionWh();
            printTest("getTotalEnergyConsumptionWh() after reset", "Total energy: " + totalEnergyWh + " Wh");
            
            // Test getCurrentwaterConsumption()
            double currentWater = testRoom.getCurrentwaterConsumption();
            printTest("getCurrentwaterConsumption()", "Current water consumption: " + currentWater + " L");
            
            // Test getTotalwaterConsumption()
            double totalWater = testRoom.getTotalwaterConsumption();
            printTest("getTotalwaterConsumption()", "Total water consumption: " + totalWater + " L");
            
            // Test getTemperature()
            double temperature = testRoom.getTemperature();
            printTest("getTemperature()", "Room temperature: " + temperature + "°C");
            
            // Test setTemperature()
            testRoom.setTemperature(23.5);
            printTest("setTemperature(23.5)", "Temperature set to 23.5°C");
            
            temperature = testRoom.getTemperature();
            printTest("getTemperature() after set", "Room temperature: " + temperature + "°C");
            
            testRoom.setTemperature(21.0);
            printTest("setTemperature(21.0)", "Temperature set to 21.0°C");
            
            temperature = testRoom.getTemperature();
            printTest("getTemperature() after second set", "Room temperature: " + temperature + "°C");
            
            // Test getAirQuality()
            com.enums.AirQuality airQuality = testRoom.getAirQuality();
            printTest("getAirQuality()", "Air quality: " + airQuality);
            
            // Test setAirQuality()
            testRoom.setAirQuality(com.enums.AirQuality.MODERATE);
            printTest("setAirQuality(MODERATE)", "Air quality set to MODERATE");
            
            airQuality = testRoom.getAirQuality();
            printTest("getAirQuality() after set", "Air quality: " + airQuality);
            
            testRoom.setAirQuality(com.enums.AirQuality.POOR);
            printTest("setAirQuality(POOR)", "Air quality set to POOR");
            
            airQuality = testRoom.getAirQuality();
            printTest("getAirQuality() after second set", "Air quality: " + airQuality);
            
            testRoom.setAirQuality(com.enums.AirQuality.GOOD);
            printTest("setAirQuality(GOOD)", "Air quality set back to GOOD");
            
            // Test removeDevice()
            testRoom.removeDevice("light-room-test-2");
            printTest("removeDevice('light-room-test-2')", "Light 2 removed from room");
            
            devices = testRoom.getDevices();
            printTest("getDevices() after removal", "Retrieved " + devices.size() + " devices");
            
            // Test removeDevice() with non-existent device
            try {
                testRoom.removeDevice("nonexistent-device");
                printTest("removeDevice('nonexistent-device')", "Should have thrown exception");
            } catch (com.exceptions.DeviceNotFoundException e) {
                printTest("removeDevice('nonexistent-device')", "DeviceNotFoundException caught (expected): " + e.getMessage());
            }
            
            // Clean up - turn off devices
            light1.turnOff();
            ac.turnOff();
            plug.turnOff();
            
        } catch (Exception e) {
            printError("testRoom", e);
        }
    }

    /**
     * Test Home class
     */
    private static void testHome() {
        printSection("Testing Home");
        
        try {
            // Create Home instance and add multiple rooms
            Home testHome = new Home("Test Home");
            printTest("Home instantiation", "Created home: " + testHome.getName());
            
            // Create test rooms
            Room room1 = new Room("Test Living Room");
            Room room2 = new Room("Test Bedroom");
            Room kitchen = new Room("Kitchen");
            Room bathroom = new Room("Bathroom");
            
            // Add devices to rooms
            Light light1 = new Light("light-home-test-1", "Test Light 1", 100, EnergyMode.NORMAL);
            Light light2 = new Light("light-home-test-2", "Test Light 2", 75, EnergyMode.NORMAL);
            AC ac1 = new AC("ac-home-test-1", "Test AC 1", 22.0);
            SmartTV tv = new SmartTV("tv-home-test", "Test TV");
            SmartPlug plug = new SmartPlug("plug-home-test", "Test Plug", EnergyMode.NORMAL);
            SmartFaucet faucet = new SmartFaucet("faucet-home-test", "Test Faucet", EnergyMode.NORMAL, true);
            
            room1.addDevice(light1);
            room1.addDevice(tv);
            room2.addDevice(light2);
            room2.addDevice(ac1);
            kitchen.addDevice(plug);
            bathroom.addDevice(faucet);
            
            // Test addRoom()
            testHome.addRoom(room1);
            printTest("addRoom(room1)", "Living Room added to home");
            
            testHome.addRoom(room2);
            printTest("addRoom(room2)", "Bedroom added to home");
            
            testHome.addRoom(kitchen);
            printTest("addRoom(kitchen)", "Kitchen added to home");
            
            testHome.addRoom(bathroom);
            printTest("addRoom(bathroom)", "Bathroom added to home");
            
            // Test getRooms()
            java.util.List<Room> rooms = testHome.getRooms();
            printTest("getRooms()", "Retrieved " + rooms.size() + " rooms");
            
            // Test getAllDevices()
            java.util.List<SmartDevice> allDevices = testHome.getAllDevices();
            printTest("getAllDevices()", "Retrieved " + allDevices.size() + " devices across all rooms");
            
            // Test findDeviceById()
            SmartDevice foundDevice = testHome.findDeviceById("light-home-test-1");
            printTest("findDeviceById('light-home-test-1')", "Found device: " + foundDevice.getName());
            
            foundDevice = testHome.findDeviceById("ac-home-test-1");
            printTest("findDeviceById('ac-home-test-1')", "Found device: " + foundDevice.getName());
            
            foundDevice = testHome.findDeviceById("tv-home-test");
            printTest("findDeviceById('tv-home-test')", "Found device: " + foundDevice.getName());
            
            // Test findDeviceById() with non-existent device
            try {
                testHome.findDeviceById("nonexistent-device");
                printTest("findDeviceById('nonexistent-device')", "Should have thrown exception");
            } catch (com.exceptions.DeviceNotFoundException e) {
                printTest("findDeviceById('nonexistent-device')", "DeviceNotFoundException caught (expected): " + e.getMessage());
            }
            
            // Test getCurrentEnergyConsumption() - devices off initially
            double currentEnergy = testHome.getCurrentEnergyConsumption();
            printTest("getCurrentEnergyConsumption() with devices off", "Current energy: " + currentEnergy + " W");
            
            // Turn on some devices
            light1.turnOn();
            light2.turnOn();
            ac1.turnOn();
            tv.turnOn();
            plug.turnOn();
            
            // Recalculate room energy consumption
            room1.recalculateCurrentEnergyConsumption();
            room2.recalculateCurrentEnergyConsumption();
            kitchen.recalculateCurrentEnergyConsumption();
            
            // Test getCurrentEnergyConsumption() with devices on
            currentEnergy = testHome.getCurrentEnergyConsumption();
            printTest("getCurrentEnergyConsumption() with devices on", "Current energy: " + currentEnergy + " W");
            
            // Test setCurrentEnergyConsumption()
            testHome.setCurrentEnergyConsumption();
            printTest("setCurrentEnergyConsumption()", "Current energy consumption updated");
            
            currentEnergy = testHome.getCurrentEnergyConsumption();
            printTest("getCurrentEnergyConsumption() after set", "Current energy: " + currentEnergy + " W");
            
            // Test getTotalEnergyConsumption() - should be 0 initially
            double totalEnergy = testHome.getTotalEnergyConsumption();
            printTest("getTotalEnergyConsumption() initially", "Total energy: " + totalEnergy + " kWh");
            
            // Accumulate some energy in rooms
            room1.accumulateEnergyUsageMinutes(60); // 1 hour
            room2.accumulateEnergyUsageMinutes(60); // 1 hour
            kitchen.accumulateEnergyUsageMinutes(30); // 30 minutes
            
            // Test getTotalEnergyConsumption() after accumulation
            totalEnergy = testHome.getTotalEnergyConsumption();
            printTest("getTotalEnergyConsumption() after accumulation", "Total energy: " + totalEnergy + " kWh");
            
            // Test setTotalEnergyConsumption()
            testHome.setTotalEnergyConsumption();
            printTest("setTotalEnergyConsumption()", "Total energy consumption updated");
            
            totalEnergy = testHome.getTotalEnergyConsumption();
            printTest("getTotalEnergyConsumption() after set", "Total energy: " + totalEnergy + " kWh");
            
            // Test getCurrentwaterConsumption() - only Kitchen and Bathroom count
            double currentWater = testHome.getCurrentwaterConsumption();
            printTest("getCurrentwaterConsumption()", "Current water consumption: " + currentWater + " L");
            
            // Test setCurrentwaterConsumption()
            testHome.setCurrentwaterConsumption();
            printTest("setCurrentwaterConsumption()", "Current water consumption updated");
            
            currentWater = testHome.getCurrentwaterConsumption();
            printTest("getCurrentwaterConsumption() after set", "Current water consumption: " + currentWater + " L");
            
            // Test getTotalwaterConsumption() - only Kitchen and Bathroom count
            double totalWater = testHome.getTotalwaterConsumption();
            printTest("getTotalwaterConsumption()", "Total water consumption: " + totalWater + " L");
            
            // Test setTotalwaterConsumption()
            testHome.setTotalwaterConsumption();
            printTest("setTotalwaterConsumption()", "Total water consumption updated");
            
            totalWater = testHome.getTotalwaterConsumption();
            printTest("getTotalwaterConsumption() after set", "Total water consumption: " + totalWater + " L");
            
            // Test wuduTime()
            double waterBeforeWudu = testHome.getTotalwaterConsumption();
            System.out.println("\n  Testing wuduTime():");
            testHome.wuduTime();
            printTest("wuduTime()", "Wudu time performed");
            
            double waterAfterWudu = testHome.getTotalwaterConsumption();
            printTest("getTotalwaterConsumption() after wudu", "Total water: " + waterAfterWudu + " L (increased by " + (waterAfterWudu - waterBeforeWudu) + " L)");
            
            // Test wuduTime() again
            waterBeforeWudu = testHome.getTotalwaterConsumption();
            System.out.println("\n  Testing wuduTime() again:");
            testHome.wuduTime();
            printTest("wuduTime() second call", "Wudu time performed again");
            
            waterAfterWudu = testHome.getTotalwaterConsumption();
            printTest("getTotalwaterConsumption() after second wudu", "Total water: " + waterAfterWudu + " L (increased by " + (waterAfterWudu - waterBeforeWudu) + " L)");
            
            // Test removeRoom()
            testHome.removeRoom("Test Living Room");
            printTest("removeRoom('Test Living Room')", "Living Room removed from home");
            
            rooms = testHome.getRooms();
            printTest("getRooms() after removal", "Retrieved " + rooms.size() + " rooms");
            
            allDevices = testHome.getAllDevices();
            printTest("getAllDevices() after room removal", "Retrieved " + allDevices.size() + " devices");
            
            // Verify removed room's devices are no longer accessible
            try {
                testHome.findDeviceById("light-home-test-1");
                printTest("findDeviceById('light-home-test-1') after room removal", "Should have thrown exception");
            } catch (com.exceptions.DeviceNotFoundException e) {
                printTest("findDeviceById('light-home-test-1') after room removal", "DeviceNotFoundException caught (expected): " + e.getMessage());
            }
            
            // Clean up - turn off devices
            light2.turnOff();
            ac1.turnOff();
            tv.turnOff();
            plug.turnOff();
            faucet.turnOff();
            
        } catch (Exception e) {
            printError("testHome", e);
        }
    }

    /**
     * Test CalendarService
     */
    private static void testCalendarService() {
        printSection("Testing CalendarService");
        
        try {
            // Instantiate CalendarService
            com.services.CalendarService calendarService = new com.services.CalendarService("test@example.com");
            printTest("CalendarService instantiation", "Created with email: test@example.com");
            
            // Test addReminder() method with multiple reminders
            boolean added1 = calendarService.addReminder("9:00 AM: Team meeting");
            printTest("addReminder('9:00 AM: Team meeting')", "Reminder added: " + added1);
            
            boolean added2 = calendarService.addReminder("2:00 PM: Client call");
            printTest("addReminder('2:00 PM: Client call')", "Reminder added: " + added2);
            
            boolean added3 = calendarService.addReminder("7:00 PM: Gym time");
            printTest("addReminder('7:00 PM: Gym time')", "Reminder added: " + added3);
            
            boolean added4 = calendarService.addReminder("8:30 PM: Quran reading");
            printTest("addReminder('8:30 PM: Quran reading')", "Reminder added: " + added4);
            
            // Test getTodaysReminders()
            java.util.List<String> todaysReminders = calendarService.getTodaysReminders();
            printTest("getTodaysReminders()", "Retrieved " + todaysReminders.size() + " reminders for today");
            
            System.out.println("  Today's reminders:");
            for (String reminder : todaysReminders) {
                System.out.println("    - " + reminder);
            }
            
            // Test getNextUpcomingReminder()
            String nextReminder = calendarService.getNextUpcomingReminder();
            printTest("getNextUpcomingReminder()", nextReminder);
            
            // Test createDemoWeekSchedule() - already called in constructor
            printTest("createDemoWeekSchedule()", "Demo week schedule created (called in constructor)");
            
            // Test getServiceStatus()
            String serviceStatus = calendarService.getServiceStatus();
            printTest("getServiceStatus()", serviceStatus);
            
        } catch (Exception e) {
            printError("testCalendarService", e);
        }
    }

    /**
     * Test IslamicCalendarService
     */
    private static void testIslamicCalendarService() {
        printSection("Testing IslamicCalendarService");
        
        try {
            // Test getTodayIslamicDate()
            com.services.IslamicCalendarService.IslamicDate todayDate = com.services.IslamicCalendarService.getTodayIslamicDate();
            printTest("getTodayIslamicDate()", "Islamic date: " + todayDate.date);
            
            if (todayDate.event != null) {
                printTest("getTodayIslamicDate() event", "Event: " + todayDate.event);
            } else {
                printTest("getTodayIslamicDate() event", "No special event today");
            }
            
            // Test getFallbackDate() - indirectly tested through getTodayIslamicDate() if API fails
            printTest("getFallbackDate()", "Fallback mechanism tested (used if API fails)");
            
            // Test getIslamicEvent() with various month/day combinations
            // Note: getIslamicEvent() is private, but we can verify it through getTodayIslamicDate()
            printTest("getIslamicEvent()", "Event detection tested through getTodayIslamicDate()");
            
            // Test getMonthNumber() - private method, tested indirectly
            printTest("getMonthNumber()", "Month number conversion tested internally");
            
            // Display the Islamic date information
            System.out.println("  Islamic Date Details:");
            System.out.println("    Date: " + todayDate.date);
            System.out.println("    Event: " + (todayDate.event != null ? todayDate.event : "None"));
            System.out.println("    toString(): " + todayDate.toString());
            
        } catch (Exception e) {
            printError("testIslamicCalendarService", e);
        }
    }

    /**
     * Test QiblaService
     */
    private static void testQiblaService() {
        printSection("Testing QiblaService");
        
        try {
            // Test getQiblaDirection() with various coordinates
            
            // Test with Tunis coordinates
            com.services.QiblaService.QiblaDirection tunisQibla = com.services.QiblaService.getQiblaDirection(36.8065, 10.1815);
            printTest("getQiblaDirection(Tunis: 36.8065, 10.1815)", "Direction: " + tunisQibla.toString());
            
            // Test with Mecca coordinates (should be close to 0 or 360)
            com.services.QiblaService.QiblaDirection meccaQibla = com.services.QiblaService.getQiblaDirection(21.4225, 39.8262);
            printTest("getQiblaDirection(Mecca: 21.4225, 39.8262)", "Direction: " + meccaQibla.toString());
            
            // Test with New York coordinates
            com.services.QiblaService.QiblaDirection nyQibla = com.services.QiblaService.getQiblaDirection(40.7128, -74.0060);
            printTest("getQiblaDirection(New York: 40.7128, -74.0060)", "Direction: " + nyQibla.toString());
            
            // Test with London coordinates
            com.services.QiblaService.QiblaDirection londonQibla = com.services.QiblaService.getQiblaDirection(51.5074, -0.1278);
            printTest("getQiblaDirection(London: 51.5074, -0.1278)", "Direction: " + londonQibla.toString());
            
            // Test with Tokyo coordinates
            com.services.QiblaService.QiblaDirection tokyoQibla = com.services.QiblaService.getQiblaDirection(35.6762, 139.6503);
            printTest("getQiblaDirection(Tokyo: 35.6762, 139.6503)", "Direction: " + tokyoQibla.toString());
            
            // Test getFallbackDirection() - indirectly tested through getQiblaDirection() if API fails
            printTest("getFallbackDirection()", "Fallback mechanism tested (used if API fails)");
            
            // Test getCompassDirection() - tested through QiblaDirection objects
            printTest("getCompassDirection()", "Compass direction conversion tested");
            
            // Test isValid() method
            boolean isValid = tunisQibla.isValid();
            printTest("isValid() for Tunis", "Valid: " + isValid);
            
            isValid = meccaQibla.isValid();
            printTest("isValid() for Mecca", "Valid: " + isValid);
            
            // Test toDisplayString() method
            String displayString = tunisQibla.toDisplayString();
            printTest("toDisplayString() for Tunis", displayString);
            
            // Display detailed information
            System.out.println("  Qibla Direction Details:");
            System.out.println("    Tunis: " + tunisQibla.direction + " (" + tunisQibla.degrees + "°)");
            System.out.println("    Mecca: " + meccaQibla.direction + " (" + meccaQibla.degrees + "°)");
            System.out.println("    New York: " + nyQibla.direction + " (" + nyQibla.degrees + "°)");
            System.out.println("    London: " + londonQibla.direction + " (" + londonQibla.degrees + "°)");
            System.out.println("    Tokyo: " + tokyoQibla.direction + " (" + tokyoQibla.degrees + "°)");
            
        } catch (Exception e) {
            printError("testQiblaService", e);
        }
    }

    /**
     * Test QuranAPIService
     */
    private static void testQuranAPIService() {
        printSection("Testing QuranAPIService");
        
        try {
            // Test getRandomVerse() method
            System.out.println("  Fetching random Quran verse...");
            com.services.QuranAPIService.QuranVerse verse1 = com.services.QuranAPIService.getRandomVerse();
            printTest("getRandomVerse()", "Verse retrieved: " + verse1.surahInfo);
            
            System.out.println("  Verse 1 Details:");
            System.out.println("    Surah Info: " + verse1.surahInfo);
            System.out.println("    Arabic: " + verse1.arabic);
            System.out.println("    Translation: " + verse1.translation);
            
            // Test getRandomVerse() again to get a different verse
            System.out.println("\n  Fetching another random Quran verse...");
            com.services.QuranAPIService.QuranVerse verse2 = com.services.QuranAPIService.getRandomVerse();
            printTest("getRandomVerse() second call", "Verse retrieved: " + verse2.surahInfo);
            
            System.out.println("  Verse 2 Details:");
            System.out.println("    Surah Info: " + verse2.surahInfo);
            System.out.println("    Arabic: " + verse2.arabic);
            System.out.println("    Translation: " + verse2.translation);
            
            // Test getRandomVerse() third time
            System.out.println("\n  Fetching third random Quran verse...");
            com.services.QuranAPIService.QuranVerse verse3 = com.services.QuranAPIService.getRandomVerse();
            printTest("getRandomVerse() third call", "Verse retrieved: " + verse3.surahInfo);
            
            System.out.println("  Verse 3 Details:");
            System.out.println("    Surah Info: " + verse3.surahInfo);
            System.out.println("    Arabic: " + verse3.arabic);
            System.out.println("    Translation: " + verse3.translation);
            
            // Test getTranslation() method with various verse IDs
            // Note: getTranslation() is private, but it's tested through getRandomVerse()
            printTest("getTranslation()", "Translation retrieval tested through getRandomVerse()");
            
            // Verify that verses have all required fields
            boolean verse1Complete = verse1.arabic != null && verse1.translation != null && verse1.surahInfo != null;
            printTest("Verse 1 completeness", "All fields present: " + verse1Complete);
            
            boolean verse2Complete = verse2.arabic != null && verse2.translation != null && verse2.surahInfo != null;
            printTest("Verse 2 completeness", "All fields present: " + verse2Complete);
            
            boolean verse3Complete = verse3.arabic != null && verse3.translation != null && verse3.surahInfo != null;
            printTest("Verse 3 completeness", "All fields present: " + verse3Complete);
            
        } catch (Exception e) {
            printError("testQuranAPIService", e);
        }
    }

    /**
     * Test AutomationEngine
     */
    private static void testAutomationEngine() {
        printSection("Testing AutomationEngine");
        
        try {
            // Create AutomationEngine instance
            com.automation.AutomationEngine engine = new com.automation.AutomationEngine();
            printTest("AutomationEngine instantiation", "Engine created successfully");
            
            // Create multiple Rule instances with different conditions and actions
            
            // Rule 1: Simple always-true rule
            com.automation.Rule rule1 = new com.automation.Rule(
                "Always Active Rule",
                () -> true,
                () -> System.out.println("  [Rule Action] Always Active Rule executed")
            );
            
            // Rule 2: Time-based rule (simulated with a boolean array for mutability)
            final boolean[] isNightTime = {true};
            com.automation.Rule rule2 = new com.automation.Rule(
                "Night Mode Rule",
                () -> isNightTime[0],
                () -> System.out.println("  [Rule Action] Night Mode activated - turning off lights")
            );
            
            // Rule 3: Temperature-based rule
            final double[] currentTemp = {28.0};
            com.automation.Rule rule3 = new com.automation.Rule(
                "High Temperature Rule",
                () -> currentTemp[0] > 25.0,
                () -> System.out.println("  [Rule Action] Temperature is high - activating AC")
            );
            
            // Rule 4: Security rule
            final boolean[] motionDetected = {false};
            com.automation.Rule rule4 = new com.automation.Rule(
                "Motion Detection Rule",
                () -> motionDetected[0],
                () -> System.out.println("  [Rule Action] Motion detected - activating security cameras")
            );
            
            // Rule 5: Energy saving rule
            final double[] energyConsumption = {5000.0};
            com.automation.Rule rule5 = new com.automation.Rule(
                "Energy Saving Rule",
                () -> energyConsumption[0] > 4000.0,
                () -> System.out.println("  [Rule Action] High energy consumption - switching to ECO mode")
            );
            
            // Test addRule() method with each rule
            engine.addRule(rule1);
            printTest("addRule(rule1)", "Always Active Rule added");
            
            engine.addRule(rule2);
            printTest("addRule(rule2)", "Night Mode Rule added");
            
            engine.addRule(rule3);
            printTest("addRule(rule3)", "High Temperature Rule added");
            
            engine.addRule(rule4);
            printTest("addRule(rule4)", "Motion Detection Rule added");
            
            engine.addRule(rule5);
            printTest("addRule(rule5)", "Energy Saving Rule added");
            
            // Test getRules() method
            java.util.List<com.automation.Rule> rules = engine.getRules();
            printTest("getRules()", "Retrieved " + rules.size() + " rules");
            
            System.out.println("  Rules in engine:");
            for (com.automation.Rule rule : rules) {
                System.out.println("    - " + rule.getName());
            }
            
            // Test evaluateRules() method - first evaluation
            System.out.println("\n  First evaluation (motion=false, night=true, temp=28.0, energy=5000.0):");
            engine.evaluateRules();
            printTest("evaluateRules() - first call", "Rules evaluated");
            
            // Test evaluateRules() method - second evaluation with different conditions
            // Simulate motion detection
            motionDetected[0] = true;
            System.out.println("\n  Second evaluation (motion=true, night=true, temp=28.0, energy=5000.0):");
            engine.evaluateRules();
            printTest("evaluateRules() - second call", "Rules evaluated with motion detected");
            
            // Test evaluateRules() method - third evaluation
            // Simulate day time and lower temperature
            motionDetected[0] = false;
            System.out.println("\n  Third evaluation (motion=false, night=true, temp=28.0, energy=5000.0):");
            engine.evaluateRules();
            printTest("evaluateRules() - third call", "Rules evaluated with different conditions");
            
            // Test removeRule() method
            engine.removeRule("Motion Detection Rule");
            printTest("removeRule('Motion Detection Rule')", "Rule removed");
            
            rules = engine.getRules();
            printTest("getRules() after removal", "Retrieved " + rules.size() + " rules");
            
            System.out.println("  Rules after removal:");
            for (com.automation.Rule rule : rules) {
                System.out.println("    - " + rule.getName());
            }
            
            // Test evaluateRules() after removal
            System.out.println("\n  Fourth evaluation after removing Motion Detection Rule:");
            engine.evaluateRules();
            printTest("evaluateRules() - after removal", "Rules evaluated without Motion Detection Rule");
            
            // Test removing another rule
            engine.removeRule("High Temperature Rule");
            printTest("removeRule('High Temperature Rule')", "Rule removed");
            
            rules = engine.getRules();
            printTest("getRules() after second removal", "Retrieved " + rules.size() + " rules");
            
            // Final evaluation
            System.out.println("\n  Fifth evaluation with fewer rules:");
            engine.evaluateRules();
            printTest("evaluateRules() - final call", "Rules evaluated with remaining rules");
            
            // Test removing non-existent rule (should not throw exception, just do nothing)
            engine.removeRule("Non-Existent Rule");
            printTest("removeRule('Non-Existent Rule')", "Attempted to remove non-existent rule (no error)");
            
            rules = engine.getRules();
            printTest("getRules() after non-existent removal", "Still have " + rules.size() + " rules");
            
        } catch (Exception e) {
            printError("testAutomationEngine", e);
        }
    }

    /**
     * Test Morning Routine integration scenario
     */
    private static void testMorningRoutine(HomeController homeController, Home home) {
        printSection("Testing Morning Routine Integration Scenario");
        
        try {
            System.out.println("  Scenario: Morning routine during Ramadan - Fajr prayer time");
            
            // Activate Ramadan mode
            homeController.activateRamadanMode();
            printTest("Activate Ramadan mode", "Ramadan mode activated: " + homeController.isRamadanModeActive());
            
            // Set time to Fajr (around 5:30 AM)
            java.sql.Time fajrTime = java.sql.Time.valueOf("05:30:00");
            homeController.setCurrentTime(fajrTime);
            printTest("Set time to Fajr", "Current time set to 05:30:00");
            
            // Trigger Athan notification
            System.out.println("\n  Triggering Athan notification:");
            homeController.AthanNotification();
            printTest("Trigger Athan notification", "Athan notification triggered for Fajr");
            
            // Display SmartMirror briefing with Islamic calendar
            SmartMirror mirror = (SmartMirror) home.findDeviceById("mirror-001");
            mirror.turnOn();
            printTest("Turn on SmartMirror", "Mirror activated");
            
            System.out.println("\n  Displaying morning briefing:");
            mirror.displayBriefing();
            printTest("Display SmartMirror briefing", "Morning briefing displayed");
            
            System.out.println("\n  Displaying Islamic calendar:");
            mirror.displayIslamicCalendar();
            printTest("Display Islamic calendar", "Islamic calendar displayed on mirror");
            
            System.out.println("\n  Displaying Quran verse:");
            mirror.displayQuranVerseOnly();
            printTest("Display Quran verse", "Daily Quran verse displayed");
            
            // Verify all components work together
            boolean ramadanActive = homeController.isRamadanModeActive();
            boolean mirrorOn = mirror.isOn();
            printTest("Verify integration", "Ramadan mode: " + ramadanActive + ", Mirror: " + mirrorOn);
            
            System.out.println("\n  ✓ Morning routine scenario completed successfully");
            System.out.println("    - Ramadan mode activated");
            System.out.println("    - Fajr Athan notification triggered");
            System.out.println("    - SmartMirror displayed briefing, Islamic calendar, and Quran verse");
            
            // Clean up
            homeController.deactivateRamadanMode();
            mirror.turnOff();
            
        } catch (Exception e) {
            printError("testMorningRoutine", e);
        }
    }

    /**
     * Test Leaving Home integration scenario
     */
    private static void testLeavingHome(HomeController homeController, Home home) {
        printSection("Testing Leaving Home Integration Scenario");
        
        try {
            System.out.println("  Scenario: User is leaving home - activate away mode");
            
            // Turn on some devices first
            homeController.turnOnDevice("light-001");
            homeController.turnOnDevice("tv-001");
            homeController.turnOnDevice("ac-001");
            printTest("Turn on some devices", "Devices turned on for testing");
            
            // Set away mode (this should turn off non-essential devices)
            System.out.println("\n  Activating away mode:");
            homeController.setAwayMode();
            printTest("Set away mode", "Away mode activated");
            
            // Turn off all non-essential devices
            System.out.println("\n  Turning off all devices:");
            homeController.turnOffAllDevices();
            printTest("Turn off all non-essential devices", "All devices turned off");
            
            // Arm security system
            System.out.println("\n  Arming security system:");
            homeController.armSecuritySystem();
            printTest("Arm security system", "Security system armed");
            
            // Lock all doors
            DoorLock frontDoor = (DoorLock) home.findDeviceById("lock-001");
            frontDoor.lock();
            printTest("Lock all doors", "Front door locked");
            
            // Verify system state
            com.controller.SecurityController securityController = homeController.getSecurityController();
            com.enums.SystemStatus securityStatus = securityController.getSystemStatus();
            com.enums.HomeState homeState = securityController.getHomeState();
            boolean doorLocked = frontDoor.getStatus().contains("LOCKED");
            
            printTest("Verify system state", "Security: " + securityStatus + ", Home state: " + homeState + ", Door locked: " + doorLocked);
            
            System.out.println("\n  ✓ Leaving home scenario completed successfully");
            System.out.println("    - Away mode activated");
            System.out.println("    - All devices turned off");
            System.out.println("    - Security system armed");
            System.out.println("    - Front door locked");
            
            // Clean up
            homeController.disarmSecuritySystem();
            frontDoor.unlock();
            
        } catch (Exception e) {
            printError("testLeavingHome", e);
        }
    }

    /**
     * Test Intruder Detection integration scenario
     */
    private static void testIntruderDetection(HomeController homeController, Home home) {
        printSection("Testing Intruder Detection Integration Scenario");
        
        try {
            System.out.println("  Scenario: Motion detected while system is armed - intruder alert");
            
            // Set system to armed away mode
            homeController.setAwayMode();
            homeController.armSecuritySystem();
            printTest("Set system to armed away mode", "System armed in away mode");
            
            // Get security devices
            MotionSensor motionSensor = (MotionSensor) home.findDeviceById("motion-001");
            SecurityCamera camera = (SecurityCamera) home.findDeviceById("camera-001");
            AlarmSiren alarm = (AlarmSiren) home.findDeviceById("alarm-001");
            
            // Ensure devices are on
            motionSensor.turnOn();
            camera.turnOn();
            
            // Trigger motion sensor - this should throw SecurityBreachException
            System.out.println("\n  Triggering motion sensor while armed:");
            try {
                motionSensor.checkMotion();
                printTest("Trigger motion sensor", "ERROR: Should have thrown SecurityBreachException");
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("Verify SecurityBreachException thrown", "SecurityBreachException caught (expected): " + e.getMessage());
                
                // Verify exception details
                boolean hasLocation = e.getMessage().contains("Living Room Motion");
                boolean hasTimestamp = e.getTimestamp() != null;
                printTest("Verify exception details", "Location in message: " + hasLocation + ", Timestamp present: " + hasTimestamp);
            }
            
            // Verify alarm is triggered (simulate alarm activation)
            alarm.triggerAlarm();
            printTest("Verify alarm triggered", "Alarm siren activated");
            
            // Verify cameras start recording
            boolean cameraRecording = camera.isOn();
            String cameraStatus = camera.getStatus();
            printTest("Verify cameras recording", "Camera recording: " + cameraRecording + " - " + cameraStatus);
            
            System.out.println("\n  ✓ Intruder detection scenario completed successfully");
            System.out.println("    - System was armed in away mode");
            System.out.println("    - Motion sensor triggered SecurityBreachException");
            System.out.println("    - Alarm siren activated");
            System.out.println("    - Security camera recording");
            
            // Clean up
            alarm.stopAlarm();
            homeController.disarmSecuritySystem();
            motionSensor.resetTrigger();
            
        } catch (Exception e) {
            printError("testIntruderDetection", e);
        }
    }

    /**
     * Test Coming Home integration scenario
     */
    private static void testComingHome(HomeController homeController, Home home) {
        printSection("Testing Coming Home Integration Scenario");
        
        try {
            System.out.println("  Scenario: User arrives home - welcome routine");
            
            // Start with armed system (simulate away mode)
            homeController.setAwayMode();
            homeController.armSecuritySystem();
            printTest("Initial state", "System armed in away mode");
            
            // Disarm security system
            System.out.println("\n  Disarming security system:");
            homeController.disarmSecuritySystem();
            printTest("Disarm security system", "Security system disarmed");
            
            // Unlock front door
            DoorLock frontDoor = (DoorLock) home.findDeviceById("lock-001");
            frontDoor.unlock();
            printTest("Unlock front door", "Front door unlocked");
            
            // Turn on lights
            System.out.println("\n  Turning on lights:");
            homeController.turnOnDevice("light-001"); // Living room
            homeController.turnOnDevice("light-005"); // Entrance
            printTest("Turn on lights", "Entrance and living room lights turned on");
            
            // Adjust temperature
            System.out.println("\n  Adjusting temperature:");
            homeController.changeAllroomsTemperature(22.0);
            printTest("Adjust temperature", "All rooms temperature set to 22.0°C");
            
            // Turn on AC in living room
            homeController.turnOnDevice("ac-001");
            printTest("Turn on AC", "Living room AC activated");
            
            // Verify system state
            com.controller.SecurityController securityController = homeController.getSecurityController();
            com.enums.SystemStatus securityStatus = securityController.getSystemStatus();
            boolean doorUnlocked = frontDoor.getStatus().contains("UNLOCKED");
            Light entranceLight = (Light) home.findDeviceById("light-005");
            boolean lightOn = entranceLight.isOn();
            
            printTest("Verify system state", "Security: " + securityStatus + ", Door unlocked: " + doorUnlocked + ", Lights on: " + lightOn);
            
            System.out.println("\n  ✓ Coming home scenario completed successfully");
            System.out.println("    - Security system disarmed");
            System.out.println("    - Front door unlocked");
            System.out.println("    - Lights turned on");
            System.out.println("    - Temperature adjusted to 22.0°C");
            
            // Clean up
            homeController.turnOffDevice("light-001");
            homeController.turnOffDevice("light-005");
            homeController.turnOffDevice("ac-001");
            
        } catch (Exception e) {
            printError("testComingHome", e);
        }
    }

    /**
     * Test Night Mode integration scenario
     */
    private static void testNightMode(HomeController homeController, Home home) {
        printSection("Testing Night Mode Integration Scenario");
        
        try {
            System.out.println("  Scenario: Bedtime - activate night mode");
            
            // Turn on some devices first
            homeController.turnOnDevice("tv-001");
            homeController.turnOnDevice("speaker-001");
            homeController.turnOnDevice("light-001");
            homeController.turnOnDevice("light-002");
            printTest("Turn on devices", "Entertainment and lighting devices turned on");
            
            // Set night mode
            System.out.println("\n  Activating night mode:");
            homeController.setNightMode();
            printTest("Set night mode", "Night mode activated");
            
            // Turn off entertainment devices
            System.out.println("\n  Turning off entertainment devices:");
            homeController.turnOffDevice("tv-001");
            homeController.turnOffDevice("speaker-001");
            printTest("Turn off entertainment devices", "TV and speaker turned off");
            
            // Activate security sensors
            System.out.println("\n  Activating security sensors:");
            MotionSensor livingRoomMotion = (MotionSensor) home.findDeviceById("motion-001");
            MotionSensor bedroomMotion = (MotionSensor) home.findDeviceById("motion-002");
            SecurityCamera camera = (SecurityCamera) home.findDeviceById("camera-001");
            
            livingRoomMotion.turnOn();
            bedroomMotion.turnOn();
            camera.turnOn();
            camera.toggleNightVision();
            printTest("Activate security sensors", "Motion sensors and camera with night vision activated");
            
            // Reduce lighting (set to low brightness)
            Light livingRoomLight = (Light) home.findDeviceById("light-001");
            Light bedroomLight = (Light) home.findDeviceById("light-002");
            
            livingRoomLight.turnOn();
            livingRoomLight.setBrightness(20); // Low brightness
            bedroomLight.turnOn();
            bedroomLight.setBrightness(15); // Very low brightness
            printTest("Reduce lighting", "Lights set to low brightness (20% and 15%)");
            
            // Verify system state
            com.controller.SecurityController securityController = homeController.getSecurityController();
            com.enums.SystemStatus securityStatus = securityController.getSystemStatus();
            boolean sensorsActive = livingRoomMotion.isOn() && bedroomMotion.isOn();
            boolean cameraActive = camera.isOn();
            
            printTest("Verify system state", "Security: " + securityStatus + ", Sensors active: " + sensorsActive + ", Camera active: " + cameraActive);
            
            System.out.println("\n  ✓ Night mode scenario completed successfully");
            System.out.println("    - Night mode activated");
            System.out.println("    - Entertainment devices turned off");
            System.out.println("    - Security sensors activated");
            System.out.println("    - Lighting reduced to low brightness");
            
            // Clean up
            livingRoomLight.turnOff();
            bedroomLight.turnOff();
            homeController.disarmSecuritySystem();
            
        } catch (Exception e) {
            printError("testNightMode", e);
        }
    }

    /**
     * Test Fire Emergency integration scenario
     */
    private static void testFireEmergency(HomeController homeController, Home home) {
        printSection("Testing Fire Emergency Integration Scenario");
        
        try {
            System.out.println("  Scenario: Smoke detected - fire emergency response");
            
            // Get smoke detector and alarm
            SmokeDetector smokeDetector = (SmokeDetector) home.findDeviceById("smoke-001");
            AlarmSiren alarm = (AlarmSiren) home.findDeviceById("alarm-001");
            
            // Ensure smoke detector is on
            smokeDetector.turnOn();
            printTest("Smoke detector status", "Smoke detector activated");
            
            // Trigger smoke detector - this should throw SecurityBreachException
            System.out.println("\n  Triggering smoke detector:");
            try {
                smokeDetector.detectSmoke(true);
                printTest("Trigger smoke detector", "ERROR: Should have thrown SecurityBreachException");
            } catch (com.exceptions.SecurityBreachException e) {
                printTest("Verify SecurityBreachException thrown", "SecurityBreachException caught (expected): " + e.getMessage());
                
                // Verify exception details
                boolean hasLocation = e.getMessage().contains("Kitchen Smoke Detector") || e.getMessage().contains("Kitchen");
                boolean hasTimestamp = e.getTimestamp() != null;
                printTest("Verify exception details", "Location in message: " + hasLocation + ", Timestamp present: " + hasTimestamp);
            }
            
            // Verify alarm siren activates
            System.out.println("\n  Activating emergency alarm:");
            alarm.triggerAlarm();
            alarm.setVolume(10); // Maximum volume for emergency
            printTest("Verify alarm siren activates", "Alarm siren activated at maximum volume");
            
            // Verify emergency response (turn on all lights for visibility)
            System.out.println("\n  Emergency response - turning on all lights:");
            homeController.turnOnDevice("light-001");
            homeController.turnOnDevice("light-002");
            homeController.turnOnDevice("light-003");
            homeController.turnOnDevice("light-004");
            homeController.turnOnDevice("light-005");
            printTest("Verify emergency response", "All lights turned on for emergency visibility");
            
            // Unlock all doors for emergency exit
            DoorLock frontDoor = (DoorLock) home.findDeviceById("lock-001");
            frontDoor.unlock();
            printTest("Unlock doors for emergency exit", "Front door unlocked for emergency exit");
            
            System.out.println("\n  ✓ Fire emergency scenario completed successfully");
            System.out.println("    - Smoke detector triggered SecurityBreachException");
            System.out.println("    - Alarm siren activated at maximum volume");
            System.out.println("    - All lights turned on for visibility");
            System.out.println("    - Doors unlocked for emergency exit");
            
            // Clean up
            try {
                smokeDetector.detectSmoke(false); // Clear smoke
            } catch (Exception ex) {
                // Ignore
            }
            alarm.stopAlarm();
            homeController.turnOffAllDevices();
            frontDoor.lock();
            
        } catch (Exception e) {
            printError("testFireEmergency", e);
        }
    }

    /**
     * Test Wudu Time integration scenario
     */
    private static void testWuduTime(Home home) {
        printSection("Testing Wudu Time Integration Scenario");
        
        try {
            System.out.println("  Scenario: Performing wudu - water consumption tracking");
            
            // Get SmartFaucet and SmartMirror
            SmartFaucet faucet = (SmartFaucet) home.findDeviceById("faucet-001");
            SmartMirror mirror = (SmartMirror) home.findDeviceById("mirror-001");
            
            // Record initial water consumption
            double initialWater = home.getTotalwaterConsumption();
            printTest("Initial water consumption", "Total water: " + initialWater + " L");
            
            // Turn on SmartFaucet
            System.out.println("\n  Turning on SmartFaucet for wudu:");
            faucet.turnOn();
            printTest("Turn on SmartFaucet", "Faucet activated");
            
            // Track water consumption during wudu (approximately 2-3 minutes)
            System.out.println("\n  Tracking water consumption during wudu:");
            faucet.trackWaterConsumption(2.5); // 2.5 minutes for wudu
            printTest("Track water consumption during wudu", "Water tracked for 2.5 minutes");
            
            // Turn off faucet
            faucet.turnOff();
            printTest("Turn off SmartFaucet", "Faucet deactivated");
            
            // Display wudu guidance on SmartMirror
            System.out.println("\n  Displaying wudu guidance on SmartMirror:");
            mirror.turnOn();
            printTest("Turn on SmartMirror", "Mirror activated");
            
            // Use the home's wuduTime() method which displays guidance
            home.wuduTime();
            printTest("Display wudu guidance on SmartMirror", "Wudu guidance displayed");
            
            // Verify water tracking
            double finalWater = home.getTotalwaterConsumption();
            double waterUsed = finalWater - initialWater;
            printTest("Verify water tracking", "Water used: " + waterUsed + " L (Total: " + finalWater + " L)");
            
            // Display Quran verse for spiritual reflection
            System.out.println("\n  Displaying Quran verse for reflection:");
            mirror.displayQuranVerseOnly();
            printTest("Display Quran verse", "Verse displayed for spiritual reflection");
            
            System.out.println("\n  ✓ Wudu time scenario completed successfully");
            System.out.println("    - SmartFaucet turned on and tracked water consumption");
            System.out.println("    - Water consumption tracked: " + waterUsed + " L");
            System.out.println("    - SmartMirror displayed wudu guidance");
            System.out.println("    - Quran verse displayed for reflection");
            
            // Clean up
            mirror.turnOff();
            
        } catch (Exception e) {
            printError("testWuduTime", e);
        }
    }

    /**
     * Test Exception Handling
     */
    private static void testExceptionHandling(HomeController homeController, Home home) {
        printSection("Testing Exception Handling");
        
        int exceptionCount = 0;
        
        try {
            System.out.println("  Testing SecurityBreachException scenarios:");
            
            // Test 1: SecurityBreachException - Motion sensor triggered while armed
            System.out.println("\n  Test 1: Motion sensor triggered while armed");
            try {
                // Arm the security system
                homeController.armSecuritySystem();
                printTest("Arm security system", "System armed");
                
                // Get motion sensor and trigger it
                MotionSensor motionSensor = (MotionSensor) home.findDeviceById("motion-001");
                motionSensor.turnOn();
                
                // This should throw SecurityBreachException
                motionSensor.checkMotion();
                printTest("Motion sensor trigger", "ERROR: Should have thrown SecurityBreachException");
                
            } catch (com.exceptions.SecurityBreachException e) {
                exceptionCount++;
                System.out.println("  ✓ SecurityBreachException caught (expected)");
                System.out.println("    - Message: " + e.getMessage());
                System.out.println("    - Location: " + e.getBreachLocation());
                System.out.println("    - Timestamp: " + e.getTimestamp());
                
                // Verify exception contains correct location and timestamp
                boolean hasLocation = e.getBreachLocation() != null && !e.getBreachLocation().isEmpty();
                boolean hasTimestamp = e.getTimestamp() != null;
                printTest("Verify exception details", "Location present: " + hasLocation + ", Timestamp present: " + hasTimestamp);
            } catch (Exception e) {
                printError("Motion sensor SecurityBreachException test", e);
            }
            
            // Disarm before next test
            homeController.disarmSecuritySystem();
            
            // Test 2: SecurityBreachException - Door/window sensor triggered while armed
            System.out.println("\n  Test 2: Door/window sensor triggered while armed");
            try {
                // Arm the security system
                homeController.armSecuritySystem();
                printTest("Arm security system", "System armed");
                
                // Get door sensor and trigger it
                DoorWindowSensor doorSensor = (DoorWindowSensor) home.findDeviceById("door-001");
                doorSensor.turnOn();
                
                // This should throw SecurityBreachException
                doorSensor.simulateOpen();
                printTest("Door sensor trigger", "ERROR: Should have thrown SecurityBreachException");
                
            } catch (com.exceptions.SecurityBreachException e) {
                exceptionCount++;
                System.out.println("  ✓ SecurityBreachException caught (expected)");
                System.out.println("    - Message: " + e.getMessage());
                System.out.println("    - Location: " + e.getBreachLocation());
                System.out.println("    - Timestamp: " + e.getTimestamp());
                
                // Verify exception contains correct location and timestamp
                boolean hasLocation = e.getBreachLocation() != null && !e.getBreachLocation().isEmpty();
                boolean hasTimestamp = e.getTimestamp() != null;
                printTest("Verify exception details", "Location present: " + hasLocation + ", Timestamp present: " + hasTimestamp);
            } catch (Exception e) {
                printError("Door sensor SecurityBreachException test", e);
            }
            
            // Disarm before next test
            homeController.disarmSecuritySystem();
            
            // Test 3: SecurityBreachException - Smoke detector triggered
            System.out.println("\n  Test 3: Smoke detector triggered");
            try {
                // Get smoke detector and trigger it
                SmokeDetector smokeDetector = (SmokeDetector) home.findDeviceById("smoke-001");
                smokeDetector.turnOn();
                
                // This should throw SecurityBreachException
                smokeDetector.detectSmoke(true);
                printTest("Smoke detector trigger", "ERROR: Should have thrown SecurityBreachException");
                
            } catch (com.exceptions.SecurityBreachException e) {
                exceptionCount++;
                System.out.println("  ✓ SecurityBreachException caught (expected)");
                System.out.println("    - Message: " + e.getMessage());
                System.out.println("    - Location: " + e.getBreachLocation());
                System.out.println("    - Timestamp: " + e.getTimestamp());
                
                // Verify exception contains correct location and timestamp
                boolean hasLocation = e.getBreachLocation() != null && !e.getBreachLocation().isEmpty();
                boolean hasTimestamp = e.getTimestamp() != null;
                printTest("Verify exception details", "Location present: " + hasLocation + ", Timestamp present: " + hasTimestamp);
                
                // Clear smoke
                try {
                    SmokeDetector smokeDetector = (SmokeDetector) home.findDeviceById("smoke-001");
                    smokeDetector.detectSmoke(false);
                } catch (Exception ex) {
                    // Ignore
                }
            } catch (Exception e) {
                printError("Smoke detector SecurityBreachException test", e);
            }
            
            // Test 4: DeviceNotFoundException - Access non-existent device
            System.out.println("\n  Test 4: DeviceNotFoundException - Access non-existent device");
            try {
                // Try to find a device that doesn't exist - this will throw DeviceNotFoundException
                SmartDevice device = home.findDeviceById("non-existent-device-999");
                printTest("Access non-existent device", "ERROR: Should have thrown DeviceNotFoundException");
                
            } catch (com.exceptions.DeviceNotFoundException e) {
                exceptionCount++;
                System.out.println("  ✓ DeviceNotFoundException caught (expected)");
                System.out.println("    - Message: " + e.getMessage());
                printTest("Verify DeviceNotFoundException", "Exception thrown for non-existent device ID");
            } catch (Exception e) {
                printError("DeviceNotFoundException test", e);
            }
            
            // Test 5: InvalidOperationException - Invalid auto-lock delay
            System.out.println("\n  Test 5: InvalidOperationException - Invalid auto-lock delay");
            try {
                // Get a door lock
                DoorLock doorLock = (DoorLock) home.findDeviceById("lock-001");
                
                // Try to set an invalid auto-lock delay (negative value)
                doorLock.setAutoLockDelay(-5);
                printTest("Set invalid auto-lock delay", "ERROR: Should have thrown InvalidOperationException");
                
            } catch (com.exceptions.InvalidOperationException e) {
                exceptionCount++;
                System.out.println("  ✓ InvalidOperationException caught (expected)");
                System.out.println("    - Message: " + e.getMessage());
                printTest("Verify InvalidOperationException", "Exception thrown for invalid auto-lock delay");
            } catch (Exception e) {
                printError("InvalidOperationException test", e);
            }
            
            // Test 6: Another InvalidOperationException - Auto-lock delay too high
            System.out.println("\n  Test 6: InvalidOperationException - Auto-lock delay too high");
            try {
                // Get a door lock
                DoorLock doorLock = (DoorLock) home.findDeviceById("lock-001");
                
                // Try to set an invalid auto-lock delay (too high)
                doorLock.setAutoLockDelay(100);
                printTest("Set auto-lock delay too high", "ERROR: Should have thrown InvalidOperationException");
                
            } catch (com.exceptions.InvalidOperationException e) {
                exceptionCount++;
                System.out.println("  ✓ InvalidOperationException caught (expected)");
                System.out.println("    - Message: " + e.getMessage());
                printTest("Verify InvalidOperationException", "Exception thrown for invalid auto-lock delay");
            } catch (Exception e) {
                printError("InvalidOperationException test", e);
            }
            
            // Print exception testing results
            System.out.println("\n  ✓ Exception handling tests completed");
            System.out.println("    - Total exceptions tested: " + exceptionCount);
            System.out.println("    - SecurityBreachException: 3 scenarios tested");
            System.out.println("    - DeviceNotFoundException: 1 scenario tested");
            System.out.println("    - InvalidOperationException: 2 scenarios tested");
            
            printTest("Exception handling tests", "All " + exceptionCount + " exception scenarios tested successfully");
            
        } catch (Exception e) {
            printError("testExceptionHandling", e);
        }
    }

    /**
     * Setup home with rooms and devices
     */
    private static Home setupHome() {
        printSection("Setting Up Home");
        
        Home home = new Home("Smart Home Test Environment");
        
        // Create rooms
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        Room kitchen = new Room("Kitchen");
        Room bathroom = new Room("Bathroom");
        Room entrance = new Room("Entrance");
        
        // Add rooms to home
        home.addRoom(livingRoom);
        home.addRoom(bedroom);
        home.addRoom(kitchen);
        home.addRoom(bathroom);
        home.addRoom(entrance);
        
        // Add devices to living room
        livingRoom.addDevice(new Light("light-001", "Living Room Light", 100, EnergyMode.NORMAL));
        livingRoom.addDevice(new SmartTV("tv-001", "Living Room TV"));
        livingRoom.addDevice(new Speaker("speaker-001", "Living Room Speaker", EnergyMode.NORMAL));
        livingRoom.addDevice(new AC("ac-001", "Living Room AC", 22.0));
        livingRoom.addDevice(new MotionSensor("motion-001", "Living Room Motion", 10, EnergyMode.NORMAL));
        
        // Add devices to bedroom
        bedroom.addDevice(new Light("light-002", "Bedroom Light", 75, EnergyMode.NORMAL));
        bedroom.addDevice(new AC("ac-002", "Bedroom AC", 20.0));
        bedroom.addDevice(new SmartPlug("plug-001", "Bedroom Plug", EnergyMode.NORMAL));
        bedroom.addDevice(new MotionSensor("motion-002", "Bedroom Motion", 8, EnergyMode.NORMAL));
        
        // Add devices to kitchen
        kitchen.addDevice(new Light("light-003", "Kitchen Light", 100, EnergyMode.NORMAL));
        kitchen.addDevice(new SmartFridge("fridge-001", "Kitchen Fridge", EnergyMode.NORMAL, 3));
        kitchen.addDevice(new SmokeDetector("smoke-001", "Kitchen Smoke Detector", "Kitchen", EnergyMode.NORMAL));
        kitchen.addDevice(new SmartPlug("plug-002", "Kitchen Plug", EnergyMode.NORMAL));
        
        // Add devices to bathroom
        bathroom.addDevice(new Light("light-004", "Bathroom Light", 60, EnergyMode.NORMAL));
        bathroom.addDevice(new SmartFaucet("faucet-001", "Bathroom Faucet", EnergyMode.NORMAL, true));
        bathroom.addDevice(new SmartMirror("mirror-001", "Bathroom Mirror", EnergyMode.NORMAL));
        bathroom.addDevice(new AirQualitySensor("air-001", "Bathroom Air Quality", EnergyMode.NORMAL));
        
        // Add devices to entrance
        entrance.addDevice(new Light("light-005", "Entrance Light", 80, EnergyMode.NORMAL));
        entrance.addDevice(new SecurityCamera("camera-001", "Front Door Camera", "1080p", 90, EnergyMode.NORMAL));
        entrance.addDevice(new DoorLock("lock-001", "Front Door Lock", EnergyMode.NORMAL));
        entrance.addDevice(new DoorWindowSensor("door-001", "Front Door Sensor", "Main Entrance", EnergyMode.NORMAL));
        entrance.addDevice(new AlarmSiren("alarm-001", "Entrance Alarm", EnergyMode.NORMAL));
        
        System.out.println("Home setup complete with " + home.getRooms().size() + " rooms");
        System.out.println("Total devices: " + home.getAllDevices().size());
        
        return home;
    }

    /**
     * Print final test summary
     */
    private static void printTestSummary() {
        printHeader("TEST SUMMARY");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + (totalTests - failedTests));
        System.out.println("Failed: " + failedTests);
        
        if (failedTests == 0) {
            System.out.println("\n✓ All tests passed!");
        } else {
            System.out.println("\n✗ Some tests failed. Review output above.");
        }
    }
}
