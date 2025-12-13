package com.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.controller.HomeController;
import com.devices.*;
import com.enums.AirQuality;
import com.enums.EnergyMode;
import com.home.Home;
import com.room.Room;

/**
 * Main JavaFX Application entry point for the Smart Home Dashboard.
 * Initializes the dashboard with sample data and sets up the primary stage.
 */
public class DashboardApplication extends Application {

    private HomeController homeController;
    private Stage primaryStage;
    private com.ui.controllers.DashboardController dashboardController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Initialize the home controller with sample data
        this.homeController = initializeHomeController();

        // Load the main dashboard FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/fxml/dashboard.fxml"));
        Parent root = loader.load();

        // Pass homeController to DashboardController
        dashboardController = loader.getController();
        dashboardController.setHomeController(homeController);

        // Create and configure the scene
        Scene scene = new Scene(root);

        // TODO: Load CSS stylesheet when created
        // scene.getStylesheets().add(getClass().getResource("/com/ui/css/main.css").toExternalForm());

        // Configure the primary stage
        primaryStage
                .setTitle("Islamic Smart Home Dashboard - " + homeController.getSecurityController().getHomeState());
        primaryStage.setScene(scene);

        // Set minimum dimensions for usability
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);

        // Set initial size to recommended dimensions
        primaryStage.setWidth(1400);
        primaryStage.setHeight(900);

        // Make window resizable
        primaryStage.setResizable(true);

        // Show maximized for best experience (optional)
        primaryStage.setMaximized(false);

        // Show the stage
        primaryStage.show();

        System.out.println("Dashboard application started successfully");
    }

    @Override
    public void stop() throws Exception {
        // Cleanup resources
        System.out.println("Dashboard application stopping - performing cleanup");

        // Stop UIUpdater
        if (dashboardController != null) {
            dashboardController.stopUIUpdater();
        }

        // Turn off all devices for safety
        if (homeController != null) {
            homeController.turnOffAllDevices();
        }

        super.stop();
        System.out.println("Dashboard application stopped");
    }

    /**
     * Initializes the HomeController with sample rooms and devices for
     * demonstration.
     * Creates a home with 4 rooms and 15 devices of various types.
     * 
     * @return Initialized HomeController with sample data
     */
    private HomeController initializeHomeController() {
        // Create the home
        Home home = new Home("My Smart Home");

        // Create Living Room with devices
        Room livingRoom = new Room("Living Room");
        livingRoom.setTemperature(22.5);
        livingRoom.setAirQuality(AirQuality.GOOD);

        Light ceilingLight = new Light("light-001", "Ceiling Light", 60, EnergyMode.NORMAL);
        ceilingLight.setColor("#FFFFFF");
        livingRoom.addDevice(ceilingLight);

        AC livingAC = new AC("ac-001", "Living Room AC", 22.0);
        livingAC.setFanSpeed("MEDIUM");
        livingRoom.addDevice(livingAC);

        SmartTV tv = new SmartTV("tv-001", "Living Room TV");
        livingRoom.addDevice(tv);

        Speaker speaker = new Speaker("speaker-001", "Living Room Speaker", EnergyMode.NORMAL);
        livingRoom.addDevice(speaker);

        home.addRoom(livingRoom);

        // Create Kitchen with devices
        Room kitchen = new Room("Kitchen");
        kitchen.setTemperature(20.0);
        kitchen.setAirQuality(AirQuality.GOOD);

        Light kitchenLight = new Light("light-002", "Kitchen Light", 80, EnergyMode.NORMAL);
        kitchen.addDevice(kitchenLight);

        SmartFridge fridge = new SmartFridge("fridge-001", "Smart Fridge", EnergyMode.ECO, 4);
        kitchen.addDevice(fridge);

        SmartFaucet faucet = new SmartFaucet("faucet-001", "Kitchen Faucet", EnergyMode.NORMAL, true);
        kitchen.addDevice(faucet);

        SmokeDetector smokeDetector = new SmokeDetector("smoke-001", "Kitchen Smoke Detector", "Kitchen",
                EnergyMode.NORMAL);
        kitchen.addDevice(smokeDetector);

        SmartMicrowave microwave = new SmartMicrowave("microwave-001", "Smart Microwave", EnergyMode.NORMAL);
        kitchen.addDevice(microwave);

        SmartToaster toaster = new SmartToaster("toaster-001", "Smart Toaster", EnergyMode.NORMAL);
        kitchen.addDevice(toaster);

        SmartCooker cooker = new SmartCooker("cooker-001", "Smart Cooker", EnergyMode.NORMAL);
        kitchen.addDevice(cooker);

        home.addRoom(kitchen);

        // Create Bedroom with devices
        Room bedroom = new Room("Bedroom");
        bedroom.setTemperature(21.0);
        bedroom.setAirQuality(AirQuality.GOOD);

        Light bedroomLight = new Light("light-003", "Bedroom Light", 40, EnergyMode.ECO);
        bedroomLight.setColor("#FFE4B5");
        bedroom.addDevice(bedroomLight);

        AC bedroomAC = new AC("ac-002", "Bedroom AC", 21.0);
        bedroomAC.setFanSpeed("LOW");
        bedroom.addDevice(bedroomAC);

        SmartMirror mirror = new SmartMirror("mirror-001", "Smart Mirror", EnergyMode.NORMAL);
        bedroom.addDevice(mirror);

        MotionSensor motionSensor = new MotionSensor("motion-001", "Bedroom Motion Sensor", EnergyMode.NORMAL);
        bedroom.addDevice(motionSensor);

        home.addRoom(bedroom);

        // Create Bathroom with devices
        Room bathroom = new Room("Bathroom");
        bathroom.setTemperature(23.0);
        bathroom.setAirQuality(AirQuality.MODERATE);

        Light bathroomLight = new Light("light-004", "Bathroom Light", 100, EnergyMode.NORMAL);
        bathroom.addDevice(bathroomLight);

        SmartFaucet bathroomFaucet = new SmartFaucet("faucet-002", "Bathroom Faucet", EnergyMode.NORMAL, true);
        bathroom.addDevice(bathroomFaucet);

        AirQualitySensor airSensor = new AirQualitySensor("air-001", "Bathroom Air Sensor", EnergyMode.NORMAL);
        bathroom.addDevice(airSensor);

        SmartWashingMachine washingMachine = new SmartWashingMachine("washer-001", "Smart Washing Machine",
                EnergyMode.NORMAL);
        bathroom.addDevice(washingMachine);

        home.addRoom(bathroom);

        // Create Prayer Room with devices
        Room prayerRoom = new Room("Prayer Room");
        prayerRoom.setTemperature(21.0);
        prayerRoom.setAirQuality(AirQuality.GOOD);

        Light prayerLight = new Light("light-006", "Prayer Room Light", 80, EnergyMode.NORMAL);
        prayerLight.setColor("#FFF8E1"); // Warm white
        prayerRoom.addDevice(prayerLight);

        AC prayerAC = new AC("ac-003", "Prayer Room AC", 21.0);
        prayerAC.setFanSpeed("LOW");
        prayerRoom.addDevice(prayerAC);

        Speaker prayerSpeaker = new Speaker("speaker-002", "Prayer Room Speaker", EnergyMode.NORMAL);
        prayerRoom.addDevice(prayerSpeaker);

        home.addRoom(prayerRoom);

        // Create HomeController and add security devices
        HomeController controller = new HomeController(home);

        // Add security devices to the security controller
        DoorLock frontDoor = new DoorLock("lock-001", "Front Door Lock", EnergyMode.NORMAL);
        controller.addSecurityDevice(frontDoor);

        SecurityCamera frontCamera = new SecurityCamera("camera-001", "Front Door Camera", "1080p", 120,
                EnergyMode.NORMAL);
        controller.addSecurityDevice(frontCamera);

        DoorWindowSensor doorSensor = new DoorWindowSensor("door-001", "Front Door Sensor", "Front Door",
                EnergyMode.NORMAL);
        controller.addSecurityDevice(doorSensor);

        AlarmSiren siren = new AlarmSiren("siren-001", "Main Alarm Siren", EnergyMode.NORMAL);
        controller.addSecurityDevice(siren);

        System.out.println("Home initialized with " + home.getRooms().size() + " rooms and " +
                home.getAllDevices().size() + " devices");

        return controller;
    }

    /**
     * Main method to launch the JavaFX application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
