package com.smarthome.gui;

import com.smarthome.controller.HomeController;
import com.smarthome.model.*;
import com.smarthome.automation.AutomationEngine;
import com.smarthome.automation.Rule;
import com.smarthome.interfaces.EnergyConsumer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SmartHomeGUI extends Application {
    private Home home;
    private HomeController controller;
    private AutomationEngine automationEngine;
    private VBox deviceListContainer;
    private Label totalEnergyLabel;
    
    @Override
    public void start(Stage primaryStage) {
        initializeSmartHome();
        
        primaryStage.setTitle("Smart Home Automation System");
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Top: Title and controls
        root.setTop(createTopBar());
        
        // Left: Device list
        root.setLeft(createDevicePanel());
        
        // Center: Energy dashboard
        root.setCenter(createEnergyDashboard());
        
        // Right: Automation panel
        root.setRight(createAutomationPanel());
        
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        updateDeviceList();
        updateEnergyDashboard();
    }
    
    private void initializeSmartHome() {
        home = new Home("My Smart Home");
        controller = new HomeController(home);
        automationEngine = new AutomationEngine();
        
        // Create rooms and devices
        Room livingRoom = new Room("Living Room");
        Room bedroom = new Room("Bedroom");
        Room kitchen = new Room("Kitchen");
        
        Light livingRoomLight = new Light("LR-L1", "Living Room Light", 100);
        SmartTV tv = new SmartTV("LR-TV1", "Living Room TV");
        MotionSensor sensor = new MotionSensor("LR-MS1", "Entry Sensor");
        
        Thermostat thermostat = new Thermostat("BR-TH1", "Bedroom Thermostat", 22.0);
        Light bedroomLight = new Light("BR-L1", "Bedroom Light", 80);
        
        Light kitchenLight = new Light("KT-L1", "Kitchen Light", 100);
        
        livingRoom.addDevice(livingRoomLight);
        livingRoom.addDevice(tv);
        livingRoom.addDevice(sensor);
        
        bedroom.addDevice(thermostat);
        bedroom.addDevice(bedroomLight);
        
        kitchen.addDevice(kitchenLight);
        
        home.addRoom(livingRoom);
        home.addRoom(bedroom);
        home.addRoom(kitchen);
        
        // Add automation rule
        Rule motionRule = new Rule("Auto Light on Motion",
            () -> sensor.isMotionDetected(),
            () -> livingRoomLight.turnOn()
        );
        automationEngine.addRule(motionRule);
    }
    
    private VBox createTopBar() {
        VBox topBar = new VBox(10);
        topBar.setPadding(new Insets(10));
        
        Label title = new Label("🏠 Smart Home Control Center");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        HBox controlButtons = new HBox(10);
        Button allOnBtn = new Button("Turn All ON");
        Button allOffBtn = new Button("Turn All OFF");
        Button refreshBtn = new Button("Refresh");
        
        allOnBtn.setOnAction(e -> {
            controller.turnOnAllDevices();
            updateDeviceList();
            updateEnergyDashboard();
        });
        
        allOffBtn.setOnAction(e -> {
            controller.turnOffAllDevices();
            updateDeviceList();
            updateEnergyDashboard();
        });
        
        refreshBtn.setOnAction(e -> {
            updateDeviceList();
            updateEnergyDashboard();
        });
        
        controlButtons.getChildren().addAll(allOnBtn, allOffBtn, refreshBtn);
        topBar.getChildren().addAll(title, controlButtons);
        
        return topBar;
    }
    
    private ScrollPane createDevicePanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(350);
        
        Label header = new Label("Devices");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        deviceListContainer = new VBox(5);
        
        panel.getChildren().addAll(header, new Separator(), deviceListContainer);
        
        ScrollPane scrollPane = new ScrollPane(panel);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private VBox createEnergyDashboard() {
        VBox dashboard = new VBox(15);
        dashboard.setPadding(new Insets(10));
        dashboard.setAlignment(Pos.TOP_CENTER);
        
        Label header = new Label("⚡ Energy Consumption Dashboard");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        totalEnergyLabel = new Label("Total: 0.00 kWh");
        totalEnergyLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #2196F3;");
        
        dashboard.getChildren().addAll(header, new Separator(), totalEnergyLabel);
        
        return dashboard;
    }
    
    private VBox createAutomationPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(250);
        
        Label header = new Label("Automation");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button evaluateBtn = new Button("Evaluate Rules");
        evaluateBtn.setOnAction(e -> {
            automationEngine.evaluateRules();
            updateDeviceList();
            updateEnergyDashboard();
        });
        
        VBox rulesContainer = new VBox(5);
        for (Rule rule : automationEngine.getRules()) {
            Label ruleLabel = new Label("✓ " + rule.getName());
            rulesContainer.getChildren().add(ruleLabel);
        }
        
        panel.getChildren().addAll(header, new Separator(), evaluateBtn, rulesContainer);
        
        return panel;
    }
    
    private void updateDeviceList() {
        deviceListContainer.getChildren().clear();
        
        for (Room room : home.getRooms()) {
            Label roomLabel = new Label("📍 " + room.getName());
            roomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            deviceListContainer.getChildren().add(roomLabel);
            
            for (SmartDevice device : room.getDevices()) {
                deviceListContainer.getChildren().add(createDeviceCard(device));
            }
            
            deviceListContainer.getChildren().add(new Separator());
        }
    }
    
    private HBox createDeviceCard(SmartDevice device) {
        HBox card = new HBox(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        VBox info = new VBox(5);
        Label nameLabel = new Label(device.getName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label statusLabel = new Label(device.getStatus());
        statusLabel.setTextFill(device.isOn() ? Color.GREEN : Color.GRAY);
        info.getChildren().addAll(nameLabel, statusLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button toggleBtn = new Button(device.isOn() ? "OFF" : "ON");
        toggleBtn.setOnAction(e -> {
            if (device.isOn()) {
                device.turnOff();
            } else {
                device.turnOn();
            }
            updateDeviceList();
            updateEnergyDashboard();
        });
        
        card.getChildren().addAll(info, spacer, toggleBtn);
        
        // Add specific controls
        if (device instanceof Light) {
            Light light = (Light) device;
            Slider brightnessSlider = new Slider(0, 100, 50);
            brightnessSlider.setShowTickLabels(false);
            brightnessSlider.setPrefWidth(100);
            brightnessSlider.valueProperty().addListener((obs, old, newVal) -> {
                light.setBrightness(newVal.intValue());
                updateEnergyDashboard();
            });
            card.getChildren().add(brightnessSlider);
        }
        
        return card;
    }
    
    private void updateEnergyDashboard() {
        double totalEnergy = 0.0;
        
        for (SmartDevice device : home.getAllDevices()) {
            if (device instanceof EnergyConsumer) {
                totalEnergy += ((EnergyConsumer) device).getEnergyConsumption();
            }
        }
        
        totalEnergyLabel.setText(String.format("Total Energy: %.2f kWh", totalEnergy));
        
        // Update color based on consumption
        if (totalEnergy > 50) {
            totalEnergyLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #f44336;");
        } else if (totalEnergy > 25) {
            totalEnergyLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #ff9800;");
        } else {
            totalEnergyLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #4caf50;");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
