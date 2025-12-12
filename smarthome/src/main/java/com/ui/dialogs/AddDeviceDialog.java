package com.ui.dialogs;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import com.devices.*;
import com.enums.EnergyMode;

/**
 * Dialog for adding a new device to a room.
 * Prompts the user for device type, ID, name, and energy mode.
 */
public class AddDeviceDialog extends Dialog<SmartDevice> {

    private ComboBox<String> deviceTypeCombo;
    private TextField deviceIdField;
    private TextField deviceNameField;
    private ComboBox<EnergyMode> energyModeCombo;
    private Label validationLabel;

    /**
     * Creates a new AddDeviceDialog.
     */
    public AddDeviceDialog() {
        setTitle("Add New Device");
        setHeaderText("Add a new smart device to this room");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the grid layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create device type combo box
        deviceTypeCombo = new ComboBox<>();
        deviceTypeCombo.getItems().addAll(
                "Light",
                "AC",
                "SmartTV",
                "Speaker",
                "DoorLock",
                "SecurityCamera",
                "SmartFridge",
                "SmartPlug",
                "SmartFaucet",
                "SmartMirror",
                "SmartMicrowave",
                "SmartToaster",
                "SmartCooker",
                "MotionSensor",
                "DoorWindowSensor",
                "SmokeDetector",
                "AirQualitySensor",
                "AlarmSiren");
        deviceTypeCombo.setPromptText("Select device type");
        deviceTypeCombo.setPrefWidth(300);

        // Create device ID field
        deviceIdField = new TextField();
        deviceIdField.setPromptText("e.g., light-001, ac-002");
        deviceIdField.setPrefWidth(300);

        // Create device name field
        deviceNameField = new TextField();
        deviceNameField.setPromptText("e.g., Ceiling Light, Main AC");
        deviceNameField.setPrefWidth(300);

        // Create energy mode combo box
        energyModeCombo = new ComboBox<>();
        energyModeCombo.getItems().addAll(EnergyMode.values());
        energyModeCombo.setValue(EnergyMode.NORMAL);
        energyModeCombo.setPrefWidth(300);

        // Add labels and fields to the grid
        grid.add(new Label("Device Type:"), 0, 0);
        grid.add(deviceTypeCombo, 1, 0);

        grid.add(new Label("Device ID:"), 0, 1);
        grid.add(deviceIdField, 1, 1);

        grid.add(new Label("Device Name:"), 0, 2);
        grid.add(deviceNameField, 1, 2);

        grid.add(new Label("Energy Mode:"), 0, 3);
        grid.add(energyModeCombo, 1, 3);

        // Add validation message label
        validationLabel = new Label();
        validationLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10pt;");
        grid.add(validationLabel, 1, 4);

        getDialogPane().setContent(grid);

        // Request focus on the device type combo by default
        javafx.application.Platform.runLater(() -> deviceTypeCombo.requestFocus());

        // Disable the Add button initially
        javafx.scene.Node addButton = getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Enable/disable Add button based on input validation
        ChangeListener<Object> validationListener = (observable, oldValue, newValue) -> {
            String validationMessage = validateInput();
            if (validationMessage == null) {
                addButton.setDisable(false);
                validationLabel.setText("");
            } else {
                addButton.setDisable(true);
                validationLabel.setText(validationMessage);
            }
        };

        deviceTypeCombo.valueProperty().addListener(validationListener);
        deviceIdField.textProperty().addListener(validationListener);
        deviceNameField.textProperty().addListener(validationListener);
        energyModeCombo.valueProperty().addListener(validationListener);

        // Convert the result to a SmartDevice when the Add button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (validateInput() == null) {
                    return createDevice();
                }
            }
            return null;
        });
    }

    /**
     * Validates all input fields.
     * 
     * @return An error message if validation fails, null if valid
     */
    private String validateInput() {
        // Validate device type
        if (deviceTypeCombo.getValue() == null || deviceTypeCombo.getValue().isEmpty()) {
            return "Please select a device type";
        }

        // Validate device ID
        String deviceId = deviceIdField.getText();
        if (deviceId == null || deviceId.trim().isEmpty()) {
            return "Device ID cannot be empty";
        }

        if (deviceId.trim().length() < 3) {
            return "Device ID must be at least 3 characters";
        }

        if (!deviceId.matches("^[a-zA-Z0-9\\-_]+$")) {
            return "Device ID can only contain letters, numbers, hyphens, and underscores";
        }

        // Validate device name
        String deviceName = deviceNameField.getText();
        if (deviceName == null || deviceName.trim().isEmpty()) {
            return "Device name cannot be empty";
        }

        if (deviceName.trim().length() < 2) {
            return "Device name must be at least 2 characters";
        }

        // Validate energy mode
        if (energyModeCombo.getValue() == null) {
            return "Please select an energy mode";
        }

        return null; // Valid
    }

    /**
     * Creates a SmartDevice instance based on the selected type and input values.
     * 
     * @return A new SmartDevice instance, or null if creation fails
     */
    private SmartDevice createDevice() {
        String deviceType = deviceTypeCombo.getValue();
        String deviceId = deviceIdField.getText().trim();
        String deviceName = deviceNameField.getText().trim();
        EnergyMode energyMode = energyModeCombo.getValue();

        try {
            return switch (deviceType) {
                case "Light" -> new Light(deviceId, deviceName, 50, energyMode); // Default 50% brightness
                case "AC" -> new AC(deviceId, deviceName, 22.0); // Default 22°C target temperature
                case "SmartTV" -> new SmartTV(deviceId, deviceName);
                case "Speaker" -> new Speaker(deviceId, deviceName, energyMode);
                case "DoorLock" -> new DoorLock(deviceId, deviceName, energyMode);
                case "SecurityCamera" -> new SecurityCamera(deviceId, deviceName, "1080p", 120, energyMode); // Default
                                                                                                             // 1080p,
                                                                                                             // 120° FOV
                case "SmartFridge" -> new SmartFridge(deviceId, deviceName, energyMode, 4); // Default 4°C
                case "SmartPlug" -> new SmartPlug(deviceId, deviceName, energyMode);
                case "SmartFaucet" -> new SmartFaucet(deviceId, deviceName, energyMode, true); // Default controllable
                case "SmartMirror" -> new SmartMirror(deviceId, deviceName, energyMode);
                case "SmartMicrowave" -> new SmartMicrowave(deviceId, deviceName, energyMode);
                case "SmartToaster" -> new SmartToaster(deviceId, deviceName, energyMode);
                case "SmartCooker" -> new SmartCooker(deviceId, deviceName, energyMode);
                case "MotionSensor" -> new MotionSensor(deviceId, deviceName, 10, energyMode); // Default 10m range
                case "DoorWindowSensor" -> new DoorWindowSensor(deviceId, deviceName, "Room", energyMode); // Default
                                                                                                           // location
                case "SmokeDetector" -> new SmokeDetector(deviceId, deviceName, "Room", energyMode); // Default location
                case "AirQualitySensor" -> new AirQualitySensor(deviceId, deviceName, energyMode);
                case "AlarmSiren" -> new AlarmSiren(deviceId, deviceName, energyMode);
                default -> {
                    System.err.println("Unknown device type: " + deviceType);
                    yield null;
                }
            };
        } catch (Exception e) {
            System.err.println("Error creating device: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
