package com.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import com.controller.HomeController;
import com.enums.AirQuality;
import com.home.Home;
import com.room.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Climate Control view.
 * Manages temperature and air quality for all rooms.
 */
public class ClimateController {

    @FXML
    private Slider masterTemperatureSlider;

    @FXML
    private Label masterTemperatureLabel;

    @FXML
    private Label atmosphericTempLabel;

    @FXML
    private VBox roomClimateContainer;

    private HomeController homeController;
    private Home home;
    private Map<Room, Slider> roomTemperatureSliders = new HashMap<>();

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        System.out.println("ClimateController initialized");

        // Set up master temperature slider listener
        if (masterTemperatureSlider != null) {
            masterTemperatureSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                updateMasterTemperatureLabel(newVal.doubleValue());
            });
        }
    }

    /**
     * Sets the HomeController instance.
     * 
     * @param homeController The HomeController managing the smart home
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
        this.home = homeController.getHome();
        loadRoomClimateControls();
        updateAtmosphericTemperature();
    }

    /**
     * Handles the Apply Master Temperature button click.
     */
    @FXML
    public void handleApplyMasterTemperature() {
        if (homeController == null || masterTemperatureSlider == null) {
            return;
        }

        double temperature = masterTemperatureSlider.getValue();
        setMasterTemperature(temperature);
    }

    /**
     * Sets the temperature for all rooms to the specified value.
     * 
     * @param temperature The temperature to set (in Celsius)
     */
    public void setMasterTemperature(double temperature) {
        if (homeController == null) {
            return;
        }

        try {
            // Round to one decimal place
            double roundedTemp = Math.round(temperature * 10.0) / 10.0;

            // Use HomeController to change all room temperatures
            homeController.changeAllroomsTemperature(roundedTemp);

            System.out.println("Set all rooms to temperature: " + roundedTemp + " C");

            // Update all individual room sliders
            for (Map.Entry<Room, Slider> entry : roomTemperatureSliders.entrySet()) {
                Room room = entry.getKey();
                Slider slider = entry.getValue();
                Platform.runLater(() -> slider.setValue(room.getTemperature()));
            }

        } catch (Exception e) {
            System.err.println("Error setting master temperature: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the master temperature label.
     * 
     * @param temperature The temperature value to display
     */
    private void updateMasterTemperatureLabel(double temperature) {
        if (masterTemperatureLabel != null) {
            masterTemperatureLabel.setText(String.format("%.1f C", temperature));
        }
    }

    /**
     * Updates the atmospheric temperature display.
     */
    private void updateAtmosphericTemperature() {
        if (homeController == null || atmosphericTempLabel == null) {
            return;
        }

        try {
            double atmosphericTemp = homeController.getCurrentTemperature();
            atmosphericTempLabel.setText(String.format("%.1f C", atmosphericTemp));
        } catch (Exception e) {
            atmosphericTempLabel.setText("-- C");
        }
    }

    /**
     * Loads climate controls for all rooms.
     */
    public void loadRoomClimateControls() {
        if (home == null || roomClimateContainer == null) {
            return;
        }

        // Clear existing room controls
        roomClimateContainer.getChildren().clear();
        roomTemperatureSliders.clear();

        List<Room> rooms = home.getRooms();

        if (rooms.isEmpty()) {
            Label noRoomsLabel = new Label("No rooms available");
            noRoomsLabel.setStyle("-fx-font-size: 14pt; -fx-text-fill: #9E9E9E;");
            roomClimateContainer.getChildren().add(noRoomsLabel);
            return;
        }

        // Create climate control for each room
        for (Room room : rooms) {
            VBox roomCard = createRoomClimateCard(room);
            roomClimateContainer.getChildren().add(roomCard);
        }
    }

    /**
     * Creates a climate control card for a single room.
     * 
     * @param room The room to create a control for
     * @return VBox containing the room climate control
     */
    private VBox createRoomClimateCard(Room room) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 6px;");

        // Room header with name and air quality
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        Label roomNameLabel = new Label(room.getName());
        roomNameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        // Air quality indicator
        HBox airQualityBox = new HBox(8);
        airQualityBox.setAlignment(Pos.CENTER_LEFT);

        Circle airQualityCircle = new Circle(8);
        AirQuality airQuality = room.getAirQuality();
        airQualityCircle.setFill(javafx.scene.paint.Color.web(getAirQualityColor(airQuality)));

        Label airQualityLabel = new Label(airQuality.toString());
        airQualityLabel.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold; -fx-text-fill: " +
                getAirQualityColor(airQuality) + ";");

        airQualityBox.getChildren().addAll(airQualityCircle, airQualityLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        header.getChildren().addAll(roomNameLabel, spacer, airQualityBox);

        // Temperature control
        HBox tempControl = new HBox(10);
        tempControl.setAlignment(Pos.CENTER_LEFT);

        Label tempLabelPrefix = new Label("Temperature:");
        tempLabelPrefix.setStyle("-fx-font-size: 12pt;");

        Slider tempSlider = new Slider(15, 30, room.getTemperature());
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);
        tempSlider.setMajorTickUnit(5);
        tempSlider.setMinorTickCount(1);
        tempSlider.setPrefWidth(250);

        Label tempValueLabel = new Label(String.format("%.1f C", room.getTemperature()));
        tempValueLabel.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold;");
        tempValueLabel.setMinWidth(60);

        // Update temperature on slider change
        tempSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double temp = Math.round(newVal.doubleValue() * 10.0) / 10.0;
            tempValueLabel.setText(String.format("%.1f C", temp));
        });

        tempSlider.setOnMouseReleased(event -> {
            double newTemp = Math.round(tempSlider.getValue() * 10.0) / 10.0;
            updateRoomTemperature(room, newTemp);
        });

        // Store slider reference
        roomTemperatureSliders.put(room, tempSlider);

        tempControl.getChildren().addAll(tempLabelPrefix, tempSlider, tempValueLabel);

        card.getChildren().addAll(header, tempControl);
        return card;
    }

    /**
     * Updates the temperature for a specific room.
     * 
     * @param room        The room to update
     * @param temperature The new temperature
     */
    public void updateRoomTemperature(Room room, double temperature) {
        if (room == null) {
            return;
        }

        try {
            room.setTemperature(temperature);
            System.out.println("Updated temperature for " + room.getName() + ": " + temperature + " C");
        } catch (Exception e) {
            System.err.println("Error updating room temperature: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the air quality display for all rooms.
     */
    public void updateAirQualityDisplay() {
        // Reload all room climate controls to refresh air quality indicators
        loadRoomClimateControls();
    }

    /**
     * Gets the color code for the specified air quality level.
     * 
     * @param quality The air quality level
     * @return The hex color code
     */
    private String getAirQualityColor(AirQuality quality) {
        return switch (quality) {
            case GOOD -> "#4CAF50"; // Green
            case MODERATE -> "#FFC107"; // Amber
            case POOR -> "#FF9800"; // Orange
        };
    }

    /**
     * Handles the Purify Rooms button click.
     * Sets air quality to GOOD for all rooms.
     */
    @FXML
    public void handlePurifyRooms() {
        if (home == null)
            return;

        System.out.println("💨 Purifying all rooms...");
        for (Room room : home.getRooms()) {
            room.setAirQuality(AirQuality.GOOD);
        }

        // Show notification
        com.ui.utils.NotificationManager.getInstance().addNotification(
                "Air Purification",
                "All rooms are being purified. Air quality set to GOOD.",
                com.ui.models.NotificationType.SUCCESS);

        // Refresh UI
        loadRoomClimateControls();
    }

    /**
     * Handles the Wudu Time button click.
     * Warms up the bathroom for ablution.
     */
    @FXML
    public void handleWuduTime() {
        if (home == null)
            return;

        Room bathroom = home.getRooms().stream()
                .filter(r -> r.getName().equalsIgnoreCase("Bathroom"))
                .findFirst()
                .orElse(null);

        if (bathroom != null) {
            System.out.println("💧 Wudu Time activated for Bathroom");
            updateRoomTemperature(bathroom, 26.0); // Warm temperature

            // Show notification
            com.ui.utils.NotificationManager.getInstance().addNotification(
                    "Wudu Time",
                    "Bathroom is warming up for Wudu (26.0°C).",
                    com.ui.models.NotificationType.INFO);

            // Refresh UI to show new temp
            loadRoomClimateControls();
        } else {
            System.err.println("Bathroom not found!");
            com.ui.utils.NotificationManager.getInstance().addNotification(
                    "Error",
                    "Bathroom not found for Wudu Time.",
                    com.ui.models.NotificationType.ALERT);
        }
    }

    /**
     * Handles the Prepare for Salat button click.
     * Prepares the Prayer Room (warm up and lights on).
     */
    @FXML
    public void handlePrepareSalat() {
        if (home == null)
            return;

        Room prayerRoom = home.getRooms().stream()
                .filter(r -> r.getName().equalsIgnoreCase("Prayer Room"))
                .findFirst()
                .orElse(null);

        if (prayerRoom != null) {
            System.out.println("🕌 Preparing Prayer Room for Salat");

            // 1. Set Temperature
            updateRoomTemperature(prayerRoom, 23.0); // Comfortable temp

            // 2. Turn on Lights
            int lightsTurnedOn = 0;
            for (com.devices.SmartDevice device : prayerRoom.getDevices()) {
                if (device instanceof com.devices.Light) {
                    try {
                        device.turnOn();
                        lightsTurnedOn++;
                    } catch (Exception e) {
                        System.err.println("Failed to turn on light: " + device.getName());
                    }
                }
            }

            // Show notification
            com.ui.utils.NotificationManager.getInstance().addNotification(
                    "Prepare for Salat",
                    "Prayer Room prepared: Temp set to 23.0°C, " + lightsTurnedOn + " lights turned on.",
                    com.ui.models.NotificationType.SUCCESS);

            // Refresh UI
            loadRoomClimateControls();
        } else {
            System.err.println("Prayer Room not found!");
            com.ui.utils.NotificationManager.getInstance().addNotification(
                    "Error",
                    "Prayer Room not found. Please add a Prayer Room first.",
                    com.ui.models.NotificationType.ALERT);
        }
    }

    /**
     * Refreshes all climate data.
     */
    public void refresh() {
        updateAtmosphericTemperature();
        loadRoomClimateControls();
    }
}
