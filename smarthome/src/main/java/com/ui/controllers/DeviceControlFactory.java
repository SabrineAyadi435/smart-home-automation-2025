package com.ui.controllers;

import com.devices.*;
import com.controller.HomeController;
import com.room.Room;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Factory class for creating device-specific control panels
 */
public class DeviceControlFactory {

    /**
     * Creates a control panel for the given device with room callback
     * 
     * @param device     The smart device to create controls for
     * @param controller The home controller for device operations
     * @param room       The room containing the device (for energy recalculation)
     * @return A JavaFX Node containing the device controls
     */
    public static Node createDeviceControl(SmartDevice device, HomeController controller, Room room) {
        if (device instanceof Light) {
            return createLightControl((Light) device, room);
        } else if (device instanceof AC) {
            return createACControl((AC) device, room);
        } else if (device instanceof SmartTV) {
            return createSmartTVControl((SmartTV) device, room);
        } else if (device instanceof Speaker) {
            return createSpeakerControl((Speaker) device, room);
        } else if (device instanceof DoorLock) {
            return createDoorLockControl((DoorLock) device, room);
        } else if (device instanceof SecurityCamera) {
            return createSecurityCameraControl((SecurityCamera) device, room);
        } else if (device instanceof SmartFridge) {
            return createSmartFridgeControl((SmartFridge) device, room);
        } else if (device instanceof SmartPlug) {
            return createSmartPlugControl((SmartPlug) device, room);
        } else if (device instanceof SmartFaucet) {
            return createSmartFaucetControl((SmartFaucet) device, room);
        } else if (device instanceof SmartMirror) {
            return createSmartMirrorControl((SmartMirror) device, room);
        } else if (device instanceof AlarmSiren) {
            return createAlarmSirenControl((AlarmSiren) device, room);
        } else if (device instanceof MotionSensor || device instanceof DoorWindowSensor ||
                device instanceof SmokeDetector || device instanceof AirQualitySensor) {
            return createSensorControl(device);
        }

        // Default control for unknown device types
        return createDefaultControl(device);
    }

    /**
     * Creates a control panel for the given device (without room callback)
     * 
     * @param device     The smart device to create controls for
     * @param controller The home controller for device operations
     * @return A JavaFX Node containing the device controls
     */
    public static Node createDeviceControl(SmartDevice device, HomeController controller) {
        if (device instanceof Light) {
            return createLightControl((Light) device);
        } else if (device instanceof AC) {
            return createACControl((AC) device);
        } else if (device instanceof SmartTV) {
            return createSmartTVControl((SmartTV) device);
        } else if (device instanceof Speaker) {
            return createSpeakerControl((Speaker) device);
        } else if (device instanceof DoorLock) {
            return createDoorLockControl((DoorLock) device);
        } else if (device instanceof SecurityCamera) {
            return createSecurityCameraControl((SecurityCamera) device);
        } else if (device instanceof SmartFridge) {
            return createSmartFridgeControl((SmartFridge) device);
        } else if (device instanceof SmartPlug) {
            return createSmartPlugControl((SmartPlug) device);
        } else if (device instanceof SmartFaucet) {
            return createSmartFaucetControl((SmartFaucet) device);
        } else if (device instanceof SmartMirror) {
            return createSmartMirrorControl((SmartMirror) device);
        } else if (device instanceof AlarmSiren) {
            return createAlarmSirenControl((AlarmSiren) device);
        } else if (device instanceof MotionSensor || device instanceof DoorWindowSensor ||
                device instanceof SmokeDetector || device instanceof AirQualitySensor) {
            return createSensorControl(device);
        }

        // Default control for unknown device types
        return createDefaultControl(device);
    }

    /**
     * Creates control panel for Light devices
     */
    private static Node createLightControl(Light light) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header with device name and toggle
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("💡 " + light.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(light.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(light.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                light.turnOn();
                toggleButton.setText("ON");
            } else {
                light.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Brightness control
        HBox brightnessBox = new HBox(10);
        brightnessBox.setAlignment(Pos.CENTER_LEFT);
        Label brightnessLabel = new Label("Brightness:");
        Slider brightnessSlider = new Slider(0, 100, light.getBrightness());
        brightnessSlider.setShowTickLabels(true);
        brightnessSlider.setShowTickMarks(true);
        brightnessSlider.setMajorTickUnit(25);
        brightnessSlider.setMinorTickCount(5);
        brightnessSlider.setPrefWidth(200);
        Label brightnessValue = new Label(light.getBrightness() + "%");
        brightnessValue.setMinWidth(50);

        brightnessSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int brightness = newVal.intValue();
            light.setBrightness(brightness);
            brightnessValue.setText(brightness + "%");
        });

        brightnessBox.getChildren().addAll(brightnessLabel, brightnessSlider, brightnessValue);

        // Color picker
        HBox colorBox = new HBox(10);
        colorBox.setAlignment(Pos.CENTER_LEFT);
        Label colorLabel = new Label("Color:");
        ColorPicker colorPicker = new ColorPicker(Color.web(light.getColor()));
        colorPicker.setOnAction(e -> {
            Color color = colorPicker.getValue();
            String hexColor = String.format("#%02X%02X%02X",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255));
            light.setColor(hexColor);
        });

        colorBox.getChildren().addAll(colorLabel, colorPicker);

        // Status label
        Label statusLabel = new Label("Status: " + light.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, brightnessBox, colorBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for AC devices
     */
    private static Node createACControl(AC ac) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("❄️ " + ac.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(ac.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(ac.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                ac.turnOn();
                toggleButton.setText("ON");
            } else {
                ac.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Temperature control
        HBox tempBox = new HBox(10);
        tempBox.setAlignment(Pos.CENTER_LEFT);
        Label tempLabel = new Label("Temperature:");
        Slider tempSlider = new Slider(16, 30, ac.getTargetTemperature());
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);
        tempSlider.setMajorTickUnit(2);
        tempSlider.setMinorTickCount(1);
        tempSlider.setPrefWidth(200);
        Label tempValue = new Label(String.format("%.1f°C", ac.getTargetTemperature()));
        tempValue.setMinWidth(60);

        tempSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double temp = Math.round(newVal.doubleValue() * 2) / 2.0; // Round to 0.5
            ac.setTargetTemperature(temp);
            tempValue.setText(String.format("%.1f°C", temp));
        });

        tempBox.getChildren().addAll(tempLabel, tempSlider, tempValue);

        // Fan speed control
        HBox fanBox = new HBox(10);
        fanBox.setAlignment(Pos.CENTER_LEFT);
        Label fanLabel = new Label("Fan Speed:");
        ComboBox<String> fanCombo = new ComboBox<>();
        fanCombo.getItems().addAll("LOW", "MEDIUM", "HIGH", "AUTO");
        fanCombo.setValue(ac.getFanSpeed());
        fanCombo.setOnAction(e -> ac.setFanSpeed(fanCombo.getValue()));

        fanBox.getChildren().addAll(fanLabel, fanCombo);

        // Status label
        Label statusLabel = new Label("Status: " + ac.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, tempBox, fanBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for SmartTV devices
     */
    private static Node createSmartTVControl(SmartTV tv) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("📺 " + tv.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(tv.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(tv.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                tv.turnOn();
                toggleButton.setText("ON");
            } else {
                tv.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Channel control
        HBox channelBox = new HBox(10);
        channelBox.setAlignment(Pos.CENTER_LEFT);
        Label channelLabel = new Label("Channel:");
        Spinner<Integer> channelSpinner = new Spinner<>(1, 999, tv.getChannel());
        channelSpinner.setEditable(true);
        channelSpinner.setPrefWidth(100);
        channelSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            tv.setChannel(newVal);
        });

        channelBox.getChildren().addAll(channelLabel, channelSpinner);

        // Volume control
        HBox volumeBox = new HBox(10);
        volumeBox.setAlignment(Pos.CENTER_LEFT);
        Label volumeLabel = new Label("Volume:");
        Slider volumeSlider = new Slider(0, 100, tv.getVolume());
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(25);
        volumeSlider.setPrefWidth(200);
        Label volumeValue = new Label(tv.getVolume() + "");
        volumeValue.setMinWidth(40);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int volume = newVal.intValue();
            tv.setVolume(volume);
            volumeValue.setText(volume + "");
        });

        volumeBox.getChildren().addAll(volumeLabel, volumeSlider, volumeValue);

        // Input source control
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        Label inputLabel = new Label("Input:");
        ComboBox<String> inputCombo = new ComboBox<>();
        inputCombo.getItems().addAll("HDMI1", "HDMI2", "HDMI3", "USB", "TV");
        inputCombo.setValue(tv.getInputSource());
        inputCombo.setOnAction(e -> tv.setInputSource(inputCombo.getValue()));

        inputBox.getChildren().addAll(inputLabel, inputCombo);

        // Status label
        Label statusLabel = new Label("Status: " + tv.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, channelBox, volumeBox, inputBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for Speaker devices
     */
    private static Node createSpeakerControl(Speaker speaker) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("🔊 " + speaker.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(speaker.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(speaker.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                speaker.turnOn();
                toggleButton.setText("ON");
            } else {
                speaker.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Volume control
        HBox volumeBox = new HBox(10);
        volumeBox.setAlignment(Pos.CENTER_LEFT);
        Label volumeLabel = new Label("Volume:");
        Slider volumeSlider = new Slider(0, 100, speaker.getVolume());
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(25);
        volumeSlider.setPrefWidth(200);
        Label volumeValue = new Label(speaker.getVolume() + "%");
        volumeValue.setMinWidth(50);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int volume = newVal.intValue();
            speaker.setVolume(volume);
            volumeValue.setText(volume + "%");
        });

        volumeBox.getChildren().addAll(volumeLabel, volumeSlider, volumeValue);

        // Playback controls
        HBox playbackBox = new HBox(10);
        playbackBox.setAlignment(Pos.CENTER);
        Button prevButton = new Button("⏮ Previous");
        Button playPauseButton = new Button(speaker.isPlaying() ? "⏸ Pause" : "▶ Play");
        Button nextButton = new Button("Next ⏭");

        prevButton.setOnAction(e -> speaker.previous());
        playPauseButton.setOnAction(e -> {
            if (speaker.isPlaying()) {
                speaker.pause();
                playPauseButton.setText("▶ Play");
            } else {
                speaker.play();
                playPauseButton.setText("⏸ Pause");
            }
        });
        nextButton.setOnAction(e -> speaker.next());

        playbackBox.getChildren().addAll(prevButton, playPauseButton, nextButton);

        // Status label
        Label statusLabel = new Label("Status: " + speaker.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, volumeBox, playbackBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for DoorLock devices
     */
    private static Node createDoorLockControl(DoorLock lock) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("🔒 " + lock.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer);

        // Lock status indicator
        HBox statusBox = new HBox(15);
        statusBox.setAlignment(Pos.CENTER);
        Circle statusCircle = new Circle(30);
        statusCircle.setFill(lock.isLocked() ? Color.RED : Color.GREEN);
        Label lockStatusLabel = new Label(lock.isLocked() ? "🔒 LOCKED" : "🔓 UNLOCKED");
        lockStatusLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;");

        statusBox.getChildren().addAll(statusCircle, lockStatusLabel);

        // Lock/Unlock toggle
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        ToggleButton lockToggle = new ToggleButton(lock.isLocked() ? "Unlock" : "Lock");
        lockToggle.setSelected(!lock.isLocked());
        lockToggle.setStyle("-fx-font-size: 14pt; -fx-padding: 10px 20px;");
        lockToggle.setOnAction(e -> {
            try {
                if (lock.isLocked()) {
                    lock.unlock();
                    lockToggle.setText("Lock");
                    statusCircle.setFill(Color.GREEN);
                    lockStatusLabel.setText("🔓 UNLOCKED");
                } else {
                    lock.lock();
                    lockToggle.setText("Unlock");
                    statusCircle.setFill(Color.RED);
                    lockStatusLabel.setText("🔒 LOCKED");
                }
            } catch (Exception ex) {
                System.err.println("Lock operation failed: " + ex.getMessage());
            }
        });

        controlBox.getChildren().add(lockToggle);

        // Action buttons
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);

        Button viewLogBtn = new Button("📋 View Access Log");
        viewLogBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 6px 12px;");
        viewLogBtn.setOnAction(e -> {
            java.util.List<String> log = lock.getRecentAccessLog(10);
            System.out.println("=== " + lock.getName() + " Access Log (Last 10 entries) ===");
            if (log.isEmpty()) {
                System.out.println("No access log entries");
            } else {
                for (String entry : log) {
                    System.out.println(entry);
                }
            }
            System.out.println("=====================================");
            com.ui.utils.ErrorHandler.showInfo("Access Log",
                    lock.getName() + " - Last " + log.size() + " entries:\n\n" +
                            String.join("\n", log.isEmpty() ? java.util.List.of("No entries")
                                    : log.subList(0, Math.min(5, log.size()))));
        });

        actionBox.getChildren().add(viewLogBtn);

        // Status label
        Label statusLabel = new Label("Status: " + lock.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, statusBox, controlBox, actionBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for SecurityCamera devices
     */
    private static Node createSecurityCameraControl(SecurityCamera camera) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("📹 " + camera.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(camera.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(camera.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                camera.turnOn();
                toggleButton.setText("ON");
            } else {
                camera.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Recording status
        HBox recordingBox = new HBox(10);
        recordingBox.setAlignment(Pos.CENTER_LEFT);
        Circle recordingIndicator = new Circle(8);
        recordingIndicator.setFill(camera.isRecording() ? Color.RED : Color.GRAY);
        Label recordingLabel = new Label(camera.isRecording() ? "● Recording" : "○ Not Recording");
        recordingLabel.setStyle("-fx-font-size: 12pt;");

        recordingBox.getChildren().addAll(recordingIndicator, recordingLabel);

        // Recording control
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        Button recordButton = new Button(camera.isRecording() ? "⏹ Stop Recording" : "⏺ Start Recording");
        recordButton.setStyle("-fx-font-size: 12pt; -fx-padding: 8px 16px;");
        recordButton.setOnAction(e -> {
            if (camera.isRecording()) {
                camera.stopRecording();
                recordButton.setText("⏺ Start Recording");
                recordingIndicator.setFill(Color.GRAY);
                recordingLabel.setText("○ Not Recording");
            } else {
                camera.startRecording();
                recordButton.setText("⏹ Stop Recording");
                recordingIndicator.setFill(Color.RED);
                recordingLabel.setText("● Recording");
            }
        });

        controlBox.getChildren().add(recordButton);

        // Action buttons
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);

        Button snapBtn = new Button("📷 Snap Photo");
        snapBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 6px 12px;");
        snapBtn.setOnAction(e -> {
            System.out.println("📸 Snapshot captured from " + camera.getName());
            com.ui.utils.ErrorHandler.showInfo("Photo Captured",
                    "Snapshot saved from " + camera.getName());
        });

        actionBox.getChildren().add(snapBtn);

        // Status label
        Label statusLabel = new Label("Status: " + camera.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, recordingBox, controlBox, actionBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for SmartFridge devices
     */
    private static Node createSmartFridgeControl(SmartFridge fridge) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("🧊 " + fridge.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(fridge.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(fridge.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                fridge.turnOn();
                toggleButton.setText("ON");
            } else {
                fridge.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Temperature control
        HBox tempBox = new HBox(10);
        tempBox.setAlignment(Pos.CENTER_LEFT);
        Label tempLabel = new Label("Temperature:");
        Slider tempSlider = new Slider(1, 7, fridge.getTemperatureSetting());
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);
        tempSlider.setMajorTickUnit(1);
        tempSlider.setPrefWidth(200);
        Label tempValue = new Label(String.format("%d°C", fridge.getTemperatureSetting()));
        tempValue.setMinWidth(50);

        tempSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int temp = newVal.intValue();
            fridge.setTemperatureSetting(temp);
            tempValue.setText(temp + "°C");
        });

        tempBox.getChildren().addAll(tempLabel, tempSlider, tempValue);

        // Door status indicator
        HBox doorBox = new HBox(10);
        doorBox.setAlignment(Pos.CENTER_LEFT);
        Label doorLabel = new Label("Door Status:");
        Circle doorIndicator = new Circle(8);
        doorIndicator.setFill(fridge.isDoorOpen() ? Color.ORANGE : Color.GREEN);
        Label doorStatusLabel = new Label(fridge.isDoorOpen() ? "Open" : "Closed");
        doorStatusLabel.setStyle("-fx-font-size: 12pt;");

        doorBox.getChildren().addAll(doorLabel, doorIndicator, doorStatusLabel);

        // Status label
        Label statusLabel = new Label("Status: " + fridge.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, tempBox, doorBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for SmartPlug devices
     */
    private static Node createSmartPlugControl(SmartPlug plug) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("🔌 " + plug.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(plug.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(plug.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                plug.turnOn();
                toggleButton.setText("ON");
            } else {
                plug.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Power consumption display
        HBox powerBox = new HBox(10);
        powerBox.setAlignment(Pos.CENTER_LEFT);
        Label powerLabel = new Label("Power Consumption:");
        Label powerValue = new Label(String.format("%.1f W", plug.getEnergyConsumption()));
        powerValue.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        powerBox.getChildren().addAll(powerLabel, powerValue);

        // Status label
        Label statusLabel = new Label("Status: " + plug.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, powerBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for SmartFaucet devices
     */
    private static Node createSmartFaucetControl(SmartFaucet faucet) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("🚰 " + faucet.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(faucet.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(faucet.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                faucet.turnOn();
                toggleButton.setText("ON");
            } else {
                faucet.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Flow rate control
        HBox flowBox = new HBox(10);
        flowBox.setAlignment(Pos.CENTER_LEFT);
        Label flowLabel = new Label("Flow Rate:");
        Slider flowSlider = new Slider(0, 10, faucet.getFlowRate());
        flowSlider.setShowTickLabels(true);
        flowSlider.setShowTickMarks(true);
        flowSlider.setMajorTickUnit(2);
        flowSlider.setMinorTickCount(1);
        flowSlider.setPrefWidth(200);
        Label flowValue = new Label(String.format("%.1f L/min", faucet.getFlowRate()));
        flowValue.setMinWidth(70);

        flowSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double flow = Math.round(newVal.doubleValue() * 10) / 10.0;
            faucet.setFlowRate(flow);
            flowValue.setText(String.format("%.1f L/min", flow));
        });

        flowBox.getChildren().addAll(flowLabel, flowSlider, flowValue);

        // Status label
        Label statusLabel = new Label("Status: " + faucet.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, flowBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for SmartMirror devices
     */
    private static Node createSmartMirrorControl(SmartMirror mirror) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("🪞 " + mirror.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(mirror.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(mirror.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                mirror.turnOn();
                toggleButton.setText("ON");
            } else {
                mirror.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Display mode control
        HBox modeBox = new HBox(10);
        modeBox.setAlignment(Pos.CENTER_LEFT);
        Label modeLabel = new Label("Display Mode:");
        ComboBox<String> modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("CLOCK", "WEATHER", "CALENDAR", "NEWS", "OFF");
        modeCombo.setValue(mirror.getDisplayMode());
        modeCombo.setOnAction(e -> mirror.setDisplayMode(modeCombo.getValue()));

        modeBox.getChildren().addAll(modeLabel, modeCombo);

        // Brightness control
        HBox brightnessBox = new HBox(10);
        brightnessBox.setAlignment(Pos.CENTER_LEFT);
        Label brightnessLabel = new Label("Brightness:");
        Slider brightnessSlider = new Slider(0, 100, mirror.getBrightness());
        brightnessSlider.setShowTickLabels(true);
        brightnessSlider.setShowTickMarks(true);
        brightnessSlider.setMajorTickUnit(25);
        brightnessSlider.setPrefWidth(200);
        Label brightnessValue = new Label(mirror.getBrightness() + "%");
        brightnessValue.setMinWidth(50);

        brightnessSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int brightness = newVal.intValue();
            mirror.setBrightness(brightness);
            brightnessValue.setText(brightness + "%");
        });

        brightnessBox.getChildren().addAll(brightnessLabel, brightnessSlider, brightnessValue);

        // Action buttons for special features
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);

        Button briefingBtn = new Button("📋 Show Briefing");
        briefingBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 6px 12px;");
        briefingBtn.setOnAction(e -> {
            mirror.displayBriefing();
            String content = mirror.getBriefingContent();
            com.ui.utils.NotificationManager.getInstance().addNotification(
                    "📋 " + mirror.getName() + " - Daily Briefing",
                    content,
                    com.ui.models.NotificationType.MIRROR);
        });

        Button refreshVerseBtn = new Button("📖 Refresh Verse");
        refreshVerseBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 6px 12px;");
        refreshVerseBtn.setOnAction(e -> {
            mirror.refreshVerse();
            String content = mirror.getCurrentVerseContent();
            com.ui.utils.NotificationManager.getInstance().addNotification(
                    "📖 " + mirror.getName() + " - Quran Verse",
                    content,
                    com.ui.models.NotificationType.MIRROR);
        });

        Button calendarBtn = new Button("📅 Islamic Calendar");
        calendarBtn.setStyle("-fx-font-size: 11pt; -fx-padding: 6px 12px;");
        calendarBtn.setOnAction(e -> {
            mirror.displayIslamicCalendar();
            String content = mirror.getIslamicCalendarContent();
            com.ui.utils.NotificationManager.getInstance().addNotification(
                    "📅 " + mirror.getName() + " - Islamic Calendar",
                    content,
                    com.ui.models.NotificationType.MIRROR);
        });

        actionBox.getChildren().addAll(briefingBtn, refreshVerseBtn, calendarBtn);

        // Status label
        Label statusLabel = new Label("Status: " + mirror.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, modeBox, brightnessBox, actionBox, statusLabel);
        return container;
    }

    /**
     * Creates control panel for Sensor devices (Motion, DoorWindow, Smoke,
     * AirQuality)
     */
    private static Node createSensorControl(SmartDevice sensor) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        String icon = "📡";
        if (sensor instanceof MotionSensor)
            icon = "👁️";
        else if (sensor instanceof DoorWindowSensor)
            icon = "🚪";
        else if (sensor instanceof SmokeDetector)
            icon = "🔥";
        else if (sensor instanceof AirQualitySensor)
            icon = "🌫️";

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(icon + " " + sensor.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer);

        // Status indicator
        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        Circle statusIndicator = new Circle(10);
        statusIndicator.setFill(sensor.isOn() ? Color.GREEN : Color.GRAY);
        Label statusLabel = new Label(sensor.isOn() ? "Active" : "Inactive");
        statusLabel.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold;");

        statusBox.getChildren().addAll(statusIndicator, statusLabel);

        // Sensor readings
        VBox readingsBox = new VBox(5);
        readingsBox.setPadding(new Insets(5));
        readingsBox.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 4px;");

        if (sensor instanceof MotionSensor) {
            MotionSensor motionSensor = (MotionSensor) sensor;
            Label readingLabel = new Label("Reading: " + motionSensor.getLastReading());
            Label timeLabel = new Label("Last Update: " +
                    (motionSensor.getLastUpdateTime() != null ? motionSensor.getLastUpdateTime().toString() : "N/A"));
            timeLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");
            readingsBox.getChildren().addAll(readingLabel, timeLabel);

        } else if (sensor instanceof DoorWindowSensor) {
            DoorWindowSensor doorSensor = (DoorWindowSensor) sensor;
            Label readingLabel = new Label("Reading: " + doorSensor.getLastReading());
            Label timeLabel = new Label("Last Update: " +
                    (doorSensor.getLastUpdateTime() != null ? doorSensor.getLastUpdateTime().toString() : "N/A"));
            timeLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");
            readingsBox.getChildren().addAll(readingLabel, timeLabel);

        } else if (sensor instanceof SmokeDetector) {
            SmokeDetector smokeDetector = (SmokeDetector) sensor;
            Label readingLabel = new Label("Reading: " + smokeDetector.getLastReading());
            Label batteryLabel = new Label("Battery: " +
                    String.format("%.0f%%", smokeDetector.getBatteryLevel()));
            Label timeLabel = new Label("Last Update: " +
                    (smokeDetector.getLastUpdateTime() != null ? smokeDetector.getLastUpdateTime().toString() : "N/A"));
            timeLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");
            readingsBox.getChildren().addAll(readingLabel, batteryLabel, timeLabel);

        } else if (sensor instanceof AirQualitySensor) {
            AirQualitySensor airSensor = (AirQualitySensor) sensor;
            Label readingLabel = new Label("Reading: " + airSensor.getLastReading());
            Label co2Label = new Label("CO2: " + String.format("%.0f ppm", airSensor.getCO2Level()));
            Label humidityLabel = new Label("Humidity: " +
                    String.format("%.1f%%", airSensor.getHumidity()));
            Label aqiLabel = new Label("AQI: " +
                    String.format("%.0f/100", airSensor.getAirQualityIndex()));
            Label timeLabel = new Label("Last Update: " +
                    (airSensor.getLastUpdateTime() != null ? airSensor.getLastUpdateTime().toString() : "N/A"));
            timeLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");
            readingsBox.getChildren().addAll(readingLabel, co2Label, humidityLabel, aqiLabel, timeLabel);
        }

        // Device status
        Label deviceStatusLabel = new Label("Status: " + sensor.getStatus());
        deviceStatusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, statusBox, readingsBox, deviceStatusLabel);
        return container;
    }

    /**
     * Creates control panel for AlarmSiren devices
     */
    private static Node createAlarmSirenControl(AlarmSiren siren) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("🚨 " + siren.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer);

        // Alarm status indicator
        HBox statusBox = new HBox(15);
        statusBox.setAlignment(Pos.CENTER);
        Circle statusCircle = new Circle(30);
        statusCircle.setFill(siren.isTriggered() ? Color.RED : Color.GRAY);
        Label alarmStatusLabel = new Label(siren.isTriggered() ? "🚨 ALARM ACTIVE" : "○ Standby");
        alarmStatusLabel.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;");

        statusBox.getChildren().addAll(statusCircle, alarmStatusLabel);

        // Control buttons
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);
        Button triggerButton = new Button("🔔 Trigger Alarm");
        triggerButton.setStyle("-fx-font-size: 12pt; -fx-padding: 8px 16px; " +
                "-fx-background-color: #F44336; -fx-text-fill: white;");
        triggerButton.setOnAction(e -> {
            siren.trigger();
            statusCircle.setFill(Color.RED);
            alarmStatusLabel.setText("🚨 ALARM ACTIVE");
        });

        Button silenceButton = new Button("🔇 Silence");
        silenceButton.setStyle("-fx-font-size: 12pt; -fx-padding: 8px 16px; " +
                "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        silenceButton.setOnAction(e -> {
            siren.silence();
            statusCircle.setFill(Color.GRAY);
            alarmStatusLabel.setText("○ Standby");
        });

        controlBox.getChildren().addAll(triggerButton, silenceButton);

        // Status label
        Label statusLabel = new Label("Status: " + siren.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, statusBox, controlBox, statusLabel);
        return container;
    }

    /**
     * Creates a default control panel for unknown device types
     */
    private static Node createDefaultControl(SmartDevice device) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label("📱 " + device.getName());
        nameLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");

        ToggleButton toggleButton = new ToggleButton(device.isOn() ? "ON" : "OFF");
        toggleButton.setSelected(device.isOn());
        updateToggleStyle(toggleButton);
        toggleButton.setOnAction(e -> {
            if (toggleButton.isSelected()) {
                device.turnOn();
                toggleButton.setText("ON");
            } else {
                device.turnOff();
                toggleButton.setText("OFF");
            }
            updateToggleStyle(toggleButton);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(nameLabel, spacer, toggleButton);

        // Status label
        Label statusLabel = new Label("Status: " + device.getStatus());
        statusLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #666;");

        container.getChildren().addAll(header, statusLabel);
        return container;
    }

    /**
     * Helper method to update toggle button styling based on state
     */

    private static void updateToggleStyle(ToggleButton button) {
        if (button.isSelected()) {
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                    "-fx-font-size: 12pt; -fx-padding: 8px 16px; -fx-background-radius: 4px;");
        } else {
            button.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; " +
                    "-fx-font-size: 12pt; -fx-padding: 8px 16px; -fx-background-radius: 4px;");
        }
    }

    // ========== Overloaded methods with Energy Recalculation ==========

    private static Node createLightControl(Light light, Room room) {
        Node control = createLightControl(light);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createACControl(AC ac, Room room) {
        Node control = createACControl(ac);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createSmartTVControl(SmartTV tv, Room room) {
        Node control = createSmartTVControl(tv);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createSpeakerControl(Speaker speaker, Room room) {
        Node control = createSpeakerControl(speaker);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createDoorLockControl(DoorLock lock, Room room) {
        // Door locks don't significantly affect energy consumption
        return createDoorLockControl(lock);
    }

    private static Node createSecurityCameraControl(SecurityCamera camera, Room room) {
        Node control = createSecurityCameraControl(camera);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createSmartFridgeControl(SmartFridge fridge, Room room) {
        Node control = createSmartFridgeControl(fridge);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createSmartPlugControl(SmartPlug plug, Room room) {
        Node control = createSmartPlugControl(plug);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createSmartFaucetControl(SmartFaucet faucet, Room room) {
        Node control = createSmartFaucetControl(faucet);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createSmartMirrorControl(SmartMirror mirror, Room room) {
        Node control = createSmartMirrorControl(mirror);
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    private static Node createAlarmSirenControl(AlarmSiren siren, Room room) {
        Node control = createAlarmSirenControl(siren);
        // Alarm sirens might not be energy consumers, but include for consistency
        addEnergyCallbackToToggle(control, room);
        return control;
    }

    /**
     * Helper method to find toggle button in a control node and add energy
     * recalculation callback
     */
    private static void addEnergyCallbackToToggle(Node control, Room room) {
        if (room == null)
            return;

        // Search for ToggleButton in the control hierarchy
        if (control instanceof javafx.scene.Parent) {
            findAndWrapToggleButton((javafx.scene.Parent) control, room);
        }
    }

    /**
     * Recursively searches for ToggleButton and wraps its action with energy
     * recalculation
     */
    private static void findAndWrapToggleButton(javafx.scene.Parent parent, Room room) {
        for (javafx.scene.Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) node;
                javafx.event.EventHandler<javafx.event.ActionEvent> originalHandler = toggleButton.getOnAction();
                toggleButton.setOnAction(e -> {
                    if (originalHandler != null) {
                        originalHandler.handle(e);
                    }
                    // Recalculate energy after device state change
                    room.recalculateCurrentEnergyConsumption();
                });
            } else if (node instanceof javafx.scene.Parent) {
                findAndWrapToggleButton((javafx.scene.Parent) node, room);
            }
        }
    }
}
