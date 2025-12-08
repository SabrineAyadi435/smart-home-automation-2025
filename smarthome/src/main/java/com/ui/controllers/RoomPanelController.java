package com.ui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import com.controller.HomeController;
import com.devices.SmartDevice;
import com.enums.AirQuality;
import com.room.Room;
import com.ui.dialogs.AddDeviceDialog;
import com.ui.utils.ErrorHandler;
import com.ui.utils.NotificationManager;
import com.ui.models.NotificationType;

/**
 * Controller for the Room Detail Panel.
 * Displays room information, temperature control, air quality, and all devices
 * in the room.
 */
public class RoomPanelController {

    @FXML
    private Label roomNameLabel;

    @FXML
    private Slider temperatureSlider;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Region airQualityIndicator;

    @FXML
    private Label airQualityLabel;

    @FXML
    private VBox devicesContainer;

    @FXML
    private Button backButton;

    @FXML
    private Button addDeviceButton;

    private Room room;
    private HomeController homeController;
    private DashboardController dashboardController;

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        System.out.println("RoomPanelController initialized");

        // Set up temperature slider listener
        if (temperatureSlider != null) {
            temperatureSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                updateTemperatureLabel(newValue.doubleValue());
            });

            // Update temperature when slider is released
            temperatureSlider.setOnMouseReleased(event -> {
                updateTemperature();
            });
        }
    }

    /**
     * Sets the room to display and loads its data.
     * 
     * @param room The room to display
     */
    public void setRoom(Room room) {
        this.room = room;
        loadRoomData();
    }

    /**
     * Sets the HomeController instance for backend operations.
     * 
     * @param homeController The HomeController managing the smart home
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    /**
     * Sets the DashboardController for navigation purposes.
     * 
     * @param dashboardController The main dashboard controller
     */
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    /**
     * Loads the room data and updates the UI.
     */
    private void loadRoomData() {
        if (room == null) {
            System.err.println("Cannot load room data: Room is null");
            return;
        }

        System.out.println("Loading room data for: " + room.getName());

        // Set room name
        if (roomNameLabel != null) {
            roomNameLabel.setText(room.getName());
        }

        // Set temperature
        if (temperatureSlider != null && temperatureLabel != null) {
            double temp = room.getTemperature();
            temperatureSlider.setValue(temp);
            updateTemperatureLabel(temp);
        }

        // Set air quality
        updateAirQualityDisplay();

        // Load devices
        loadDevices();

        // Add Wudu Time button if room is Bathroom
        if (room.getName().equals("Bathroom")) {
            addWuduTimeButton();
        }
    }

    /**
     * Adds a Wudu Time button to the devices container for the Bathroom.
     */
    private void addWuduTimeButton() {
        if (devicesContainer == null)
            return;

        Button wuduButton = new Button("💧 Wudu Time");
        wuduButton.setStyle(
                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14pt; -fx-padding: 10 20; -fx-background-radius: 5;");
        wuduButton.setMaxWidth(Double.MAX_VALUE);

        wuduButton.setOnAction(e -> {
            if (homeController != null && homeController.getHome() != null) {
                // 1. Find the SmartFaucet in this room
                boolean faucetFound = false;
                for (SmartDevice device : room.getDevices()) {
                    if (device instanceof com.devices.SmartFaucet) {
                        ((com.devices.SmartFaucet) device).addWaterConsumption(0.5);
                        faucetFound = true;
                        // We can break after finding one, or update all. Usually one main faucet.
                        break;
                    }
                }

                if (faucetFound) {
                    // Show notification
                    NotificationManager.getInstance().addNotification(
                            "Water Usage",
                            "Wudu Time: Added 0.5L to water consumption.",
                            NotificationType.INFO);
                } else {
                    // Fallback if no faucet found (shouldn't happen in Bathroom but good for
                    // safety)
                    ErrorHandler.showError("Error", "No Smart Faucet found in this room to record usage.");
                }
            }
        });

        // Add a separator and the button at the top of devices container
        VBox wuduContainer = new VBox(10);
        wuduContainer.setStyle("-fx-padding: 0 0 20 0; -fx-alignment: center;");
        wuduContainer.getChildren().add(wuduButton);

        // Add as first element
        devicesContainer.getChildren().add(0, wuduContainer);
    }

    /**
     * Updates the temperature label with the current slider value.
     * 
     * @param temperature The temperature value to display
     */
    private void updateTemperatureLabel(double temperature) {
        if (temperatureLabel != null) {
            temperatureLabel.setText(String.format("%.1f°C", temperature));
        }
    }

    /**
     * Updates the room temperature based on the slider value.
     * Called when the user adjusts the temperature slider.
     */
    private void updateTemperature() {
        if (room == null || temperatureSlider == null) {
            return;
        }

        double newTemperature = temperatureSlider.getValue();

        try {
            // Update the room temperature
            room.setTemperature(newTemperature);

            System.out.println("Temperature updated for " + room.getName() + ": " +
                    String.format("%.1f°C", newTemperature));

            // Update the label
            updateTemperatureLabel(newTemperature);

        } catch (Exception e) {
            System.err.println("Error updating temperature: " + e.getMessage());
            ErrorHandler.showError("Error", "Failed to update temperature: " + e.getMessage());
        }
    }

    /**
     * Updates the air quality display with color-coded indicator.
     */
    private void updateAirQualityDisplay() {
        if (room == null || airQualityLabel == null || airQualityIndicator == null) {
            return;
        }

        AirQuality quality = room.getAirQuality();
        airQualityLabel.setText(quality.toString());

        // Set color based on air quality
        String color = getAirQualityColor(quality);
        airQualityIndicator.setStyle("-fx-background-radius: 50%; -fx-min-width: 20; " +
                "-fx-min-height: 20; -fx-max-width: 20; -fx-max-height: 20; " +
                "-fx-background-color: " + color + ";");
        airQualityLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
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
     * Loads all devices in the room and displays their controls.
     * This will be fully implemented in task 10 (DeviceControlFactory).
     */
    private void loadDevices() {
        if (room == null || devicesContainer == null) {
            return;
        }

        // Clear existing device controls
        devicesContainer.getChildren().clear();

        // Get all devices in the room
        var devices = room.getDevices();

        if (devices.isEmpty()) {
            // Show "no devices" message
            Label noDevicesLabel = new Label("No devices in this room");
            noDevicesLabel.setStyle("-fx-font-size: 16pt; -fx-text-fill: #9E9E9E; -fx-padding: 40;");
            devicesContainer.getChildren().add(noDevicesLabel);
            System.out.println("No devices to display in " + room.getName());
        } else {
            System.out.println("Loading " + devices.size() + " devices for " + room.getName());

            // Create device controls using DeviceControlFactory
            for (SmartDevice device : devices) {
                // Create the device control using the factory
                javafx.scene.Node deviceControl = DeviceControlFactory.createDeviceControl(device, homeController,
                        room);

                // Create wrapper container with remove button
                VBox deviceWrapper = new VBox(10);
                deviceWrapper.setStyle("-fx-padding: 0; -fx-spacing: 5;");

                // Create remove button container
                HBox buttonBox = new HBox();
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.setStyle("-fx-padding: 0 10 0 10;");

                Button removeButton = new Button("🗑 Remove");
                removeButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; " +
                        "-fx-font-size: 11pt; -fx-padding: 5 15; -fx-background-radius: 4;");
                removeButton.setOnAction(e -> removeDevice(device));

                buttonBox.getChildren().add(removeButton);

                // Add device control and button to wrapper
                deviceWrapper.getChildren().addAll(deviceControl, buttonBox);

                // Add wrapper to container
                devicesContainer.getChildren().add(deviceWrapper);
            }
        }
    }

    /**
     * Handles the Back button click to return to home view.
     */
    @FXML
    public void handleBack() {
        System.out.println("Back button clicked - returning to home view");

        if (dashboardController != null) {
            dashboardController.showHomeView();
        } else {
            System.err.println("Cannot navigate back: DashboardController is null");
            ErrorHandler.showError("Navigation Error", "Cannot return to home view");
        }
    }

    /**
     * Handles the Add Device button click.
     * Opens a dialog to add a new device to the room.
     */
    @FXML
    public void handleAddDevice() {
        System.out.println("Add Device button clicked");

        if (room == null) {
            ErrorHandler.showError("Error", "No room selected");
            return;
        }

        addDevice();
    }

    /**
     * Opens the AddDeviceDialog and adds the created device to the room.
     */
    private void addDevice() {
        try {
            // Create and show the dialog
            AddDeviceDialog dialog = new AddDeviceDialog();
            java.util.Optional<SmartDevice> result = dialog.showAndWait();

            // If user clicked Add and device was created
            if (result.isPresent()) {
                SmartDevice newDevice = result.get();

                // Check if device ID already exists in the room
                if (room.findDeviceById(newDevice.getDeviceId()) != null) {
                    ErrorHandler.showError("Duplicate Device ID",
                            "A device with ID '" + newDevice.getDeviceId() + "' already exists in this room.");
                    return;
                }

                // Add device to the room
                room.addDevice(newDevice);

                // Recalculate energy consumption
                room.recalculateCurrentEnergyConsumption();

                System.out.println("Device added successfully: " + newDevice.getName());

                // Show success message
                ErrorHandler.showSuccess("Device Added",
                        "Device '" + newDevice.getName() + "' has been added to " + room.getName());

                // Refresh the device list display
                loadDevices();

            } else {
                System.out.println("Add device cancelled by user");
            }

        } catch (Exception e) {
            System.err.println("Error adding device: " + e.getMessage());
            e.printStackTrace();
            ErrorHandler.showError("Error Adding Device",
                    "Failed to add device: " + e.getMessage());
        }
    }

    /**
     * Removes a device from the room after confirmation.
     * 
     * @param device The device to remove
     */
    private void removeDevice(SmartDevice device) {
        if (device == null || room == null) {
            return;
        }

        try {
            // Show confirmation dialog
            boolean confirmed = ErrorHandler.showConfirmation(
                    "Remove Device",
                    "Are you sure you want to remove '" + device.getName() + "' from " + room.getName() + "?");

            if (confirmed) {
                // Remove device from the room
                room.removeDevice(device.getDeviceId());

                // Recalculate energy consumption
                room.recalculateCurrentEnergyConsumption();

                System.out.println("Device removed successfully: " + device.getName());

                // Show success message
                ErrorHandler.showInfo("Device Removed",
                        "Device '" + device.getName() + "' has been removed from " + room.getName());

                // Refresh the device list display
                loadDevices();

            } else {
                System.out.println("Remove device cancelled by user");
            }

        } catch (com.exceptions.DeviceNotFoundException e) {
            System.err.println("Device not found: " + e.getMessage());
            ErrorHandler.showError("Device Not Found",
                    "Device '" + device.getName() + "' was not found in " + room.getName());
        } catch (Exception e) {
            System.err.println("Error removing device: " + e.getMessage());
            e.printStackTrace();
            ErrorHandler.showError("Error Removing Device",
                    "Failed to remove device: " + e.getMessage());
        }
    }

    /**
     * Refreshes the room panel with current data.
     */
    public void refresh() {
        System.out.println("Refreshing room panel");
        loadRoomData();
    }
}
