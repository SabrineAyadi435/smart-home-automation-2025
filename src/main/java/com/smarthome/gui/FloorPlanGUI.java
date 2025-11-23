package com.smarthome.gui;

import com.smarthome.controller.HomeController;
import com.smarthome.model.*;
import com.smarthome.automation.AutomationEngine;
import com.smarthome.interfaces.EnergyConsumer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

public class FloorPlanGUI extends Application {
    private Home home;
    private HomeController controller;
    private AutomationEngine automationEngine;
    private Pane floorPlanContainer;
    private Label totalEnergyLabel;
    private Map<Room, String> roomColors;
    
    @Override
    public void start(Stage primaryStage) {
        initializeSmartHome();
        
        primaryStage.setTitle("Smart Home Floor Plan");
        
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        
        // Top bar
        root.setTop(createTopBar());
        
        // Center: Floor plan
        root.setCenter(createFloorPlanView());
        
        // Bottom: Energy dashboard
        root.setBottom(createEnergyPanel());
        
        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/floorplan.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        updateFloorPlan();
        updateEnergyDashboard();
    }
    
    private void initializeSmartHome() {
        home = new Home("My Smart Home");
        controller = new HomeController(home);
        automationEngine = new AutomationEngine();
        roomColors = new HashMap<>();
        
        // Create sample rooms with devices
        Room livingRoom = new Room("Living Room");
        roomColors.put(livingRoom, "#FFE5B4");
        livingRoom.addDevice(new Light("LR-L1", "Ceiling Light", 100));
        livingRoom.addDevice(new SmartTV("LR-TV1", "Smart TV"));
        livingRoom.addDevice(new Speaker("LR-SP1", "Smart Speaker"));
        livingRoom.addDevice(new MotionSensor("LR-MS1", "Motion Sensor"));
        
        Room bedroom = new Room("Bedroom");
        roomColors.put(bedroom, "#E6E6FA");
        bedroom.addDevice(new Light("BR-L1", "Bedside Lamp", 80));
        bedroom.addDevice(new Thermostat("BR-TH1", "Thermostat", 22.0));
        bedroom.addDevice(new SmartPlug("BR-PL1", "Phone Charger"));
        
        Room kitchen = new Room("Kitchen");
        roomColors.put(kitchen, "#F0FFF0");
        kitchen.addDevice(new Light("KT-L1", "Kitchen Light", 100));
        kitchen.addDevice(new SmartPlug("KT-PL1", "Coffee Maker"));
        
        Room entrance = new Room("Entrance");
        roomColors.put(entrance, "#FFF8DC");
        entrance.addDevice(new SmartLock("EN-LK1", "Front Door"));
        entrance.addDevice(new Camera("EN-CM1", "Security Camera"));
        entrance.addDevice(new Light("EN-L1", "Porch Light", 100));
        
        home.addRoom(livingRoom);
        home.addRoom(bedroom);
        home.addRoom(kitchen);
        home.addRoom(entrance);
    }
    
    private VBox createTopBar() {
        VBox topBar = new VBox(10);
        topBar.getStyleClass().add("top-bar");
        topBar.setPadding(new Insets(15));
        
        Label title = new Label("🏠 Smart Home Floor Plan");
        title.getStyleClass().add("title-label");
        
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        
        Button addRoomBtn = new Button("+ Add Room");
        addRoomBtn.getStyleClass().add("add-button");
        addRoomBtn.setOnAction(e -> showAddRoomDialog());
        
        Button allOnBtn = new Button("All ON");
        allOnBtn.getStyleClass().add("control-button");
        allOnBtn.setOnAction(e -> {
            controller.turnOnAllDevices();
            updateFloorPlan();
            updateEnergyDashboard();
        });
        
        Button allOffBtn = new Button("All OFF");
        allOffBtn.getStyleClass().add("control-button");
        allOffBtn.setOnAction(e -> {
            controller.turnOffAllDevices();
            updateFloorPlan();
            updateEnergyDashboard();
        });
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("control-button");
        refreshBtn.setOnAction(e -> {
            updateFloorPlan();
            updateEnergyDashboard();
        });
        
        controls.getChildren().addAll(addRoomBtn, allOnBtn, allOffBtn, refreshBtn);
        topBar.getChildren().addAll(title, controls);
        
        return topBar;
    }
    
    private ScrollPane createFloorPlanView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        
        // House structure with realistic layout
        Pane houseStructure = createHouseStructure();
        
        container.getChildren().add(houseStructure);
        
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        
        return scrollPane;
    }
    
    private Pane createHouseStructure() {
        Pane house = new Pane();
        house.setPrefSize(1100, 700);
        house.setStyle("-fx-background-color: #e8f5e9;"); // Lawn/exterior
        
        // Main house outline with roof
        VBox houseBox = new VBox();
        houseBox.setLayoutX(100);
        houseBox.setLayoutY(80);
        
        // Roof
        Pane roof = createRoof();
        
        // House body
        Pane houseBody = new Pane();
        houseBody.setPrefSize(900, 550);
        houseBody.setStyle("-fx-background-color: #f5f5dc; -fx-border-color: #8b4513; -fx-border-width: 4;");
        
        // Draw walls and rooms
        drawWalls(houseBody);
        
        // Add room containers
        floorPlanContainer = new Pane();
        floorPlanContainer.setPrefSize(900, 550);
        houseBody.getChildren().add(floorPlanContainer);
        
        houseBox.getChildren().addAll(roof, houseBody);
        house.getChildren().add(houseBox);
        
        return house;
    }
    
    private Pane createRoof() {
        Pane roofPane = new Pane();
        roofPane.setPrefSize(900, 80);
        
        // Triangular roof using polygon
        javafx.scene.shape.Polygon roof = new javafx.scene.shape.Polygon();
        roof.getPoints().addAll(
            0.0, 80.0,      // Bottom left
            450.0, 0.0,     // Top center
            900.0, 80.0     // Bottom right
        );
        roof.setFill(Color.web("#8b4513"));
        roof.setStroke(Color.web("#654321"));
        roof.setStrokeWidth(3);
        
        // Chimney
        javafx.scene.shape.Rectangle chimney = new javafx.scene.shape.Rectangle(650, 20, 40, 60);
        chimney.setFill(Color.web("#a0522d"));
        chimney.setStroke(Color.web("#654321"));
        chimney.setStrokeWidth(2);
        
        roofPane.getChildren().addAll(roof, chimney);
        return roofPane;
    }
    
    private void drawWalls(Pane houseBody) {
        // Vertical walls
        addWall(houseBody, 300, 0, 4, 550);    // Left vertical wall
        addWall(houseBody, 600, 0, 4, 550);    // Right vertical wall
        
        // Horizontal walls
        addWall(houseBody, 0, 275, 300, 4);    // Top left horizontal
        addWall(houseBody, 600, 275, 300, 4);  // Top right horizontal
        
        // Doors (gaps in walls)
        addDoor(houseBody, 295, 250, true);    // Door in left wall
        addDoor(houseBody, 595, 250, true);    // Door in right wall
        addDoor(houseBody, 425, 270, false);   // Door in center horizontal
        
        // Windows
        addWindow(houseBody, 150, 50);
        addWindow(houseBody, 150, 350);
        addWindow(houseBody, 750, 50);
        addWindow(houseBody, 750, 350);
    }
    
    private void addWall(Pane parent, double x, double y, double width, double height) {
        javafx.scene.shape.Rectangle wall = new javafx.scene.shape.Rectangle(x, y, width, height);
        wall.setFill(Color.web("#8b7355"));
        wall.setStroke(Color.web("#654321"));
        wall.setStrokeWidth(1);
        parent.getChildren().add(wall);
    }
    
    private void addDoor(Pane parent, double x, double y, boolean vertical) {
        javafx.scene.shape.Rectangle door = new javafx.scene.shape.Rectangle(
            x, y, vertical ? 10 : 50, vertical ? 50 : 10
        );
        door.setFill(Color.web("#8b4513"));
        door.setStroke(Color.web("#654321"));
        door.setStrokeWidth(2);
        
        // Door knob
        javafx.scene.shape.Circle knob = new javafx.scene.shape.Circle(
            x + (vertical ? 8 : 10), 
            y + (vertical ? 25 : 5), 
            3
        );
        knob.setFill(Color.GOLD);
        
        parent.getChildren().addAll(door, knob);
    }
    
    private void addWindow(Pane parent, double x, double y) {
        javafx.scene.shape.Rectangle window = new javafx.scene.shape.Rectangle(x, y, 60, 50);
        window.setFill(Color.web("#87ceeb"));
        window.setStroke(Color.web("#654321"));
        window.setStrokeWidth(3);
        
        // Window panes
        javafx.scene.shape.Line line1 = new javafx.scene.shape.Line(x + 30, y, x + 30, y + 50);
        javafx.scene.shape.Line line2 = new javafx.scene.shape.Line(x, y + 25, x + 60, y + 25);
        line1.setStroke(Color.web("#654321"));
        line2.setStroke(Color.web("#654321"));
        line1.setStrokeWidth(2);
        line2.setStrokeWidth(2);
        
        parent.getChildren().addAll(window, line1, line2);
    }
    
    private HBox createEnergyPanel() {
        HBox panel = new HBox(20);
        panel.getStyleClass().add("energy-panel");
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.CENTER);
        
        Label energyIcon = new Label("⚡");
        energyIcon.setStyle("-fx-font-size: 32px;");
        
        VBox energyInfo = new VBox(5);
        Label energyTitle = new Label("Total Energy Consumption");
        energyTitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        totalEnergyLabel = new Label("0.00 kWh");
        totalEnergyLabel.getStyleClass().addAll("energy-label", "energy-low");
        
        energyInfo.getChildren().addAll(energyTitle, totalEnergyLabel);
        panel.getChildren().addAll(energyIcon, energyInfo);
        
        return panel;
    }
    
    private void updateFloorPlan() {
        floorPlanContainer.getChildren().clear();
        
        // Position rooms in specific locations to match house layout
        java.util.List<Room> rooms = home.getRooms();
        
        // Define room positions (x, y) for a realistic layout
        double[][] positions = {
            {20, 20},      // Top-left (Living Room)
            {320, 20},     // Top-center (Bedroom)
            {620, 20},     // Top-right (Kitchen)
            {20, 295},     // Bottom-left (Entrance)
            {320, 295},    // Bottom-center (additional room)
            {620, 295},    // Bottom-right (additional room)
        };
        
        for (int i = 0; i < rooms.size() && i < positions.length; i++) {
            VBox roomTile = createRoomTile(rooms.get(i));
            roomTile.setLayoutX(positions[i][0]);
            roomTile.setLayoutY(positions[i][1]);
            floorPlanContainer.getChildren().add(roomTile);
        }
        
        // Add any additional rooms in a flow pattern
        for (int i = positions.length; i < rooms.size(); i++) {
            VBox roomTile = createRoomTile(rooms.get(i));
            // Position additional rooms below the main structure
            roomTile.setLayoutX(20 + ((i - positions.length) % 3) * 300);
            roomTile.setLayoutY(570);
            floorPlanContainer.getChildren().add(roomTile);
        }
    }
    
    private VBox createRoomTile(Room room) {
        VBox tile = new VBox(8);
        tile.getStyleClass().add("room-tile");
        tile.setPrefWidth(260);
        tile.setPrefHeight(240);
        tile.setMaxWidth(260);
        tile.setMaxHeight(240);
        
        // Room color indicator with more realistic styling
        String roomColor = roomColors.getOrDefault(room, "#FFFFFF");
        tile.setStyle("-fx-background-color: " + roomColor + "; " +
                     "-fx-border-color: #654321; -fx-border-width: 3; " +
                     "-fx-border-radius: 0; -fx-background-radius: 0; -fx-padding: 10; " +
                     "-fx-effect: innershadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);");
        
        // Room header with floor texture
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: rgba(139, 115, 85, 0.3); " +
                       "-fx-padding: 5; -fx-background-radius: 3;");
        
        Label roomIcon = new Label("🚪");
        roomIcon.setStyle("-fx-font-size: 16px;");
        
        VBox roomInfo = new VBox(2);
        Label roomName = new Label(room.getName());
        roomName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label deviceCount = new Label(room.getDevices().size() + " devices");
        deviceCount.setStyle("-fx-font-size: 10px; -fx-text-fill: #7f8c8d;");
        
        roomInfo.getChildren().addAll(roomName, deviceCount);
        header.getChildren().addAll(roomIcon, roomInfo);
        
        // Devices grid - more compact
        FlowPane devicesGrid = new FlowPane();
        devicesGrid.setHgap(6);
        devicesGrid.setVgap(6);
        devicesGrid.setPrefWrapLength(240);
        devicesGrid.setAlignment(Pos.CENTER);
        
        for (SmartDevice device : room.getDevices()) {
            devicesGrid.getChildren().add(createDeviceIcon(device));
        }
        
        // Add device button - smaller
        Button addDeviceBtn = new Button("+ Device");
        addDeviceBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                             "-fx-font-size: 9px; -fx-padding: 3 8; -fx-background-radius: 3; " +
                             "-fx-cursor: hand;");
        addDeviceBtn.setOnAction(e -> showAddDeviceDialog(room));
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        tile.getChildren().addAll(header, devicesGrid, spacer, addDeviceBtn);
        
        // Add tooltip
        Tooltip tooltip = new Tooltip("Click devices to control them");
        Tooltip.install(tile, tooltip);
        
        return tile;
    }
    
    private VBox createDeviceIcon(SmartDevice device) {
        VBox iconBox = new VBox(3);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setPrefWidth(55);
        iconBox.setCursor(javafx.scene.Cursor.HAND);
        
        // Device icon - smaller
        Label icon = new Label(getDeviceEmoji(device));
        icon.setStyle("-fx-font-size: 20px; -fx-padding: 5; " +
                     "-fx-background-radius: 50%; -fx-min-width: 35px; " +
                     "-fx-min-height: 35px; -fx-alignment: center;");
        
        // Apply device-specific colors
        String bgColor = getDeviceColor(device);
        icon.setStyle(icon.getStyle() + "-fx-background-color: " + bgColor + ";");
        
        if (!device.isOn()) {
            icon.setOpacity(0.4);
        } else {
            icon.setOpacity(1.0);
        }
        
        // Device name - very compact
        Label name = new Label(device.getName().length() > 10 ? 
            device.getName().substring(0, 8) + ".." : device.getName());
        name.setStyle("-fx-font-size: 8px; -fx-text-fill: #2c3e50;");
        name.setWrapText(true);
        name.setMaxWidth(50);
        name.setAlignment(Pos.CENTER);
        
        iconBox.getChildren().addAll(icon, name);
        
        // Click to open control dialog
        iconBox.setOnMouseClicked(e -> showDeviceControlDialog(device));
        
        // Tooltip
        Tooltip tooltip = new Tooltip(device.getStatus());
        Tooltip.install(iconBox, tooltip);
        
        return iconBox;
    }
    
    private String getDeviceColor(SmartDevice device) {
        if (device instanceof Light) return "#f39c12";
        if (device instanceof Thermostat) return "#e74c3c";
        if (device instanceof SmartTV) return "#8e44ad";
        if (device instanceof SmartPlug) return "#9b59b6";
        if (device instanceof SmartLock) return "#34495e";
        if (device instanceof Camera) return "#16a085";
        if (device instanceof MotionSensor) return "#27ae60";
        if (device instanceof Speaker) return "#2980b9";
        return "#95a5a6";
    }
    
    private String getDeviceEmoji(SmartDevice device) {
        if (device instanceof Light) return "💡";
        if (device instanceof Thermostat) return "🌡️";
        if (device instanceof SmartTV) return "📺";
        if (device instanceof SmartPlug) return "🔌";
        if (device instanceof SmartLock) return "🔒";
        if (device instanceof Camera) return "📷";
        if (device instanceof MotionSensor) return "👁️";
        if (device instanceof Speaker) return "🔊";
        return "📱";
    }
    
    private String getDeviceClass(SmartDevice device) {
        if (device instanceof Light) return "light";
        if (device instanceof Thermostat) return "thermostat";
        if (device instanceof SmartTV) return "tv";
        if (device instanceof SmartPlug) return "plug";
        if (device instanceof SmartLock) return "lock";
        if (device instanceof Camera) return "camera";
        if (device instanceof MotionSensor) return "sensor";
        if (device instanceof Speaker) return "speaker";
        return "device";
    }
    
    private void showDeviceControlDialog(SmartDevice device) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Control: " + device.getName());
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("modal-dialog");
        content.setAlignment(Pos.TOP_CENTER);
        
        Label title = new Label(getDeviceEmoji(device) + " " + device.getName());
        title.getStyleClass().add("modal-title");
        
        Label status = new Label("Status: " + device.getStatus());
        status.setStyle("-fx-font-size: 14px;");
        
        // Toggle button
        Button toggleBtn = new Button(device.isOn() ? "Turn OFF" : "Turn ON");
        toggleBtn.getStyleClass().add("toggle-button");
        if (device.isOn()) toggleBtn.getStyleClass().add("on");
        toggleBtn.setOnAction(e -> {
            if (device.isOn()) {
                device.turnOff();
            } else {
                device.turnOn();
            }
            status.setText("Status: " + device.getStatus());
            toggleBtn.setText(device.isOn() ? "Turn OFF" : "Turn ON");
            updateFloorPlan();
            updateEnergyDashboard();
        });
        
        content.getChildren().addAll(title, new Separator(), status, toggleBtn);
        
        // Device-specific controls
        if (device instanceof Light) {
            Light light = (Light) device;
            Label brightnessLabel = new Label("Brightness");
            brightnessLabel.getStyleClass().add("form-label");
            
            Slider brightnessSlider = new Slider(0, 100, 50);
            brightnessSlider.setShowTickLabels(true);
            brightnessSlider.setShowTickMarks(true);
            brightnessSlider.setMajorTickUnit(25);
            brightnessSlider.setBlockIncrement(10);
            
            Label brightnessValue = new Label("50%");
            brightnessSlider.valueProperty().addListener((obs, old, newVal) -> {
                light.setBrightness(newVal.intValue());
                brightnessValue.setText(newVal.intValue() + "%");
                updateEnergyDashboard();
            });
            
            content.getChildren().addAll(new Separator(), brightnessLabel, brightnessSlider, brightnessValue);
        }
        
        if (device instanceof Thermostat) {
            Thermostat thermostat = (Thermostat) device;
            Label tempLabel = new Label("Target Temperature");
            tempLabel.getStyleClass().add("form-label");
            
            Slider tempSlider = new Slider(10, 35, 22);
            tempSlider.setShowTickLabels(true);
            tempSlider.setShowTickMarks(true);
            tempSlider.setMajorTickUnit(5);
            tempSlider.setBlockIncrement(1);
            
            Label tempValue = new Label("22°C");
            tempSlider.valueProperty().addListener((obs, old, newVal) -> {
                thermostat.setTargetTemperature(newVal.doubleValue());
                tempValue.setText(String.format("%.1f°C", newVal.doubleValue()));
                updateEnergyDashboard();
            });
            
            content.getChildren().addAll(new Separator(), tempLabel, tempSlider, tempValue);
        }
        
        if (device instanceof SmartTV) {
            SmartTV tv = (SmartTV) device;
            Label volumeLabel = new Label("Volume");
            volumeLabel.getStyleClass().add("form-label");
            
            Slider volumeSlider = new Slider(0, 100, 50);
            volumeSlider.setShowTickLabels(true);
            volumeSlider.setShowTickMarks(true);
            volumeSlider.setMajorTickUnit(25);
            
            Label volumeValue = new Label("50%");
            volumeSlider.valueProperty().addListener((obs, old, newVal) -> {
                tv.setVolume(newVal.intValue());
                volumeValue.setText(newVal.intValue() + "%");
            });
            
            content.getChildren().addAll(new Separator(), volumeLabel, volumeSlider, volumeValue);
        }
        
        if (device instanceof Speaker) {
            Speaker speaker = (Speaker) device;
            
            HBox playControls = new HBox(10);
            playControls.setAlignment(Pos.CENTER);
            
            Button playBtn = new Button(speaker.isPlaying() ? "⏸ Pause" : "▶ Play");
            playBtn.getStyleClass().add("control-button");
            playBtn.setOnAction(e -> {
                if (speaker.isPlaying()) {
                    speaker.pause();
                    playBtn.setText("▶ Play");
                } else {
                    speaker.play();
                    playBtn.setText("⏸ Pause");
                }
                status.setText("Status: " + speaker.getStatus());
                updateEnergyDashboard();
            });
            
            playControls.getChildren().add(playBtn);
            
            Label volumeLabel = new Label("Volume");
            volumeLabel.getStyleClass().add("form-label");
            
            Slider volumeSlider = new Slider(0, 100, 50);
            volumeSlider.setShowTickLabels(true);
            volumeSlider.setShowTickMarks(true);
            volumeSlider.setMajorTickUnit(25);
            
            Label volumeValue = new Label("50%");
            volumeSlider.valueProperty().addListener((obs, old, newVal) -> {
                speaker.setVolume(newVal.intValue());
                volumeValue.setText(newVal.intValue() + "%");
                updateEnergyDashboard();
            });
            
            content.getChildren().addAll(new Separator(), playControls, volumeLabel, volumeSlider, volumeValue);
        }
        
        if (device instanceof SmartLock) {
            SmartLock lock = (SmartLock) device;
            
            Button lockBtn = new Button(lock.isLocked() ? "🔓 Unlock" : "🔒 Lock");
            lockBtn.getStyleClass().add("control-button");
            lockBtn.setOnAction(e -> {
                if (lock.isLocked()) {
                    lock.unlock();
                    lockBtn.setText("🔒 Lock");
                } else {
                    lock.lock();
                    lockBtn.setText("🔓 Unlock");
                }
                status.setText("Status: " + lock.getStatus());
                updateFloorPlan();
            });
            
            content.getChildren().addAll(new Separator(), lockBtn);
        }
        
        if (device instanceof Camera) {
            Camera camera = (Camera) device;
            
            Button recordBtn = new Button(camera.isRecording() ? "⏹ Stop Recording" : "⏺ Start Recording");
            recordBtn.getStyleClass().add("control-button");
            recordBtn.setOnAction(e -> {
                if (camera.isRecording()) {
                    camera.stopRecording();
                    recordBtn.setText("⏺ Start Recording");
                } else {
                    camera.startRecording();
                    recordBtn.setText("⏹ Stop Recording");
                }
                status.setText("Status: " + camera.getStatus());
            });
            
            content.getChildren().addAll(new Separator(), recordBtn);
        }
        
        if (device instanceof MotionSensor) {
            MotionSensor sensor = (MotionSensor) device;
            
            Button motionBtn = new Button("Simulate Motion");
            motionBtn.getStyleClass().add("control-button");
            motionBtn.setOnAction(e -> {
                sensor.detectMotion();
                status.setText("Status: " + sensor.getStatus());
                updateFloorPlan();
            });
            
            Button clearBtn = new Button("Clear Motion");
            clearBtn.getStyleClass().add("control-button");
            clearBtn.setOnAction(e -> {
                sensor.clearMotion();
                status.setText("Status: " + sensor.getStatus());
                updateFloorPlan();
            });
            
            HBox motionControls = new HBox(10);
            motionControls.setAlignment(Pos.CENTER);
            motionControls.getChildren().addAll(motionBtn, clearBtn);
            
            content.getChildren().addAll(new Separator(), motionControls);
        }
        
        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("control-button");
        closeBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(new Separator(), closeBtn);
        
        Scene scene = new Scene(content, 400, 500);
        scene.getStylesheets().add(getClass().getResource("/floorplan.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }
    
    private void showAddRoomDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New Room");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("modal-dialog");
        
        Label title = new Label("Add New Room");
        title.getStyleClass().add("modal-title");
        
        Label nameLabel = new Label("Room Name:");
        nameLabel.getStyleClass().add("form-label");
        
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Bathroom, Office, Garage");
        nameField.getStyleClass().add("text-input");
        
        Label colorLabel = new Label("Room Color:");
        colorLabel.getStyleClass().add("form-label");
        
        FlowPane colorPicker = new FlowPane();
        colorPicker.setHgap(10);
        colorPicker.setVgap(10);
        
        String[] colors = {"#FFE5B4", "#E6E6FA", "#F0FFF0", "#FFF8DC", "#FFE4E1", 
                          "#E0FFFF", "#FFF0F5", "#F5F5DC", "#FAFAD2", "#FFE4B5"};
        
        final String[] selectedColor = {colors[0]};
        
        for (String color : colors) {
            Rectangle colorBox = new Rectangle(40, 40);
            colorBox.setFill(Color.web(color));
            colorBox.setStroke(Color.web("#bdc3c7"));
            colorBox.setStrokeWidth(2);
            colorBox.setCursor(javafx.scene.Cursor.HAND);
            
            colorBox.setOnMouseClicked(e -> {
                selectedColor[0] = color;
                // Visual feedback
                colorPicker.getChildren().forEach(node -> {
                    if (node instanceof Rectangle) {
                        ((Rectangle) node).setStrokeWidth(2);
                        ((Rectangle) node).setStroke(Color.web("#bdc3c7"));
                    }
                });
                colorBox.setStrokeWidth(4);
                colorBox.setStroke(Color.web("#2c3e50"));
            });
            
            colorPicker.getChildren().add(colorBox);
        }
        
        // Set first color as selected
        if (colorPicker.getChildren().size() > 0) {
            Rectangle firstBox = (Rectangle) colorPicker.getChildren().get(0);
            firstBox.setStrokeWidth(4);
            firstBox.setStroke(Color.web("#2c3e50"));
        }
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addBtn = new Button("Add Room");
        addBtn.getStyleClass().add("add-button");
        addBtn.setOnAction(e -> {
            String roomName = nameField.getText().trim();
            if (!roomName.isEmpty()) {
                Room newRoom = new Room(roomName);
                home.addRoom(newRoom);
                roomColors.put(newRoom, selectedColor[0]);
                updateFloorPlan();
                dialog.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a room name.");
                alert.showAndWait();
            }
        });
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("control-button");
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(addBtn, cancelBtn);
        
        content.getChildren().addAll(title, new Separator(), nameLabel, nameField, 
                                     colorLabel, colorPicker, new Separator(), buttons);
        
        Scene scene = new Scene(content, 450, 500);
        scene.getStylesheets().add(getClass().getResource("/floorplan.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }
    
    private void showAddDeviceDialog(Room room) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Device to " + room.getName());
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("modal-dialog");
        
        Label title = new Label("Add Device to " + room.getName());
        title.getStyleClass().add("modal-title");
        
        Label typeLabel = new Label("Device Type:");
        typeLabel.getStyleClass().add("form-label");
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Light", "Thermostat", "Smart TV", "Smart Plug", 
                                   "Smart Lock", "Camera", "Motion Sensor", "Speaker");
        typeCombo.setValue("Light");
        typeCombo.setPrefWidth(300);
        
        Label nameLabel = new Label("Device Name:");
        nameLabel.getStyleClass().add("form-label");
        
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Ceiling Light, Wall Socket");
        nameField.getStyleClass().add("text-input");
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addBtn = new Button("Add Device");
        addBtn.getStyleClass().add("add-button");
        addBtn.setOnAction(e -> {
            String deviceName = nameField.getText().trim();
            String deviceType = typeCombo.getValue();
            
            if (!deviceName.isEmpty()) {
                String deviceId = room.getName().substring(0, 2).toUpperCase() + "-" + 
                                System.currentTimeMillis() % 10000;
                
                SmartDevice newDevice = null;
                
                switch (deviceType) {
                    case "Light":
                        newDevice = new Light(deviceId, deviceName, 100);
                        break;
                    case "Thermostat":
                        newDevice = new Thermostat(deviceId, deviceName, 22.0);
                        break;
                    case "Smart TV":
                        newDevice = new SmartTV(deviceId, deviceName);
                        break;
                    case "Smart Plug":
                        newDevice = new SmartPlug(deviceId, deviceName);
                        break;
                    case "Smart Lock":
                        newDevice = new SmartLock(deviceId, deviceName);
                        break;
                    case "Camera":
                        newDevice = new Camera(deviceId, deviceName);
                        break;
                    case "Motion Sensor":
                        newDevice = new MotionSensor(deviceId, deviceName);
                        break;
                    case "Speaker":
                        newDevice = new Speaker(deviceId, deviceName);
                        break;
                }
                
                if (newDevice != null) {
                    room.addDevice(newDevice);
                    updateFloorPlan();
                    updateEnergyDashboard();
                    dialog.close();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a device name.");
                alert.showAndWait();
            }
        });
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("control-button");
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttons.getChildren().addAll(addBtn, cancelBtn);
        
        content.getChildren().addAll(title, new Separator(), typeLabel, typeCombo, 
                                     nameLabel, nameField, new Separator(), buttons);
        
        Scene scene = new Scene(content, 400, 350);
        scene.getStylesheets().add(getClass().getResource("/floorplan.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }
    
    private void updateEnergyDashboard() {
        double totalEnergy = 0.0;
        
        for (SmartDevice device : home.getAllDevices()) {
            if (device instanceof EnergyConsumer) {
                totalEnergy += ((EnergyConsumer) device).getEnergyConsumption();
            }
        }
        
        totalEnergyLabel.setText(String.format("%.2f kWh", totalEnergy));
        
        // Update style based on consumption
        totalEnergyLabel.getStyleClass().removeAll("energy-low", "energy-medium", "energy-high");
        if (totalEnergy > 50) {
            totalEnergyLabel.getStyleClass().add("energy-high");
        } else if (totalEnergy > 25) {
            totalEnergyLabel.getStyleClass().add("energy-medium");
        } else {
            totalEnergyLabel.getStyleClass().add("energy-low");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
