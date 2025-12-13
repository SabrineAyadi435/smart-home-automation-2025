package com.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import com.controller.HomeController;
import com.controller.SecurityController;
import com.enums.SystemStatus;
import com.ui.utils.ErrorHandler;

import java.util.List;
import java.util.Map;

/**
 * Controller for the Security Panel view.
 * Manages security system status, controls, devices, and event log.
 */
public class SecurityPanelController {

    @FXML
    private Label statusLabel;

    @FXML
    private VBox securityDevicesContainer;

    @FXML
    private VBox securityLogContainer;

    private HomeController homeController;
    private SecurityController securityController;

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        System.out.println("SecurityPanelController initialized");
    }

    /**
     * Sets the HomeController instance.
     * 
     * @param homeController The HomeController managing the smart home
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
        this.securityController = homeController.getSecurityController();
        refresh();
    }

    /**
     * Handles the Arm System button click.
     */
    @FXML
    public void handleArmSystem() {
        if (homeController == null) {
            return;
        }

        try {
            homeController.armSecuritySystem();
            System.out.println("Security system armed");
            updateSecurityStatus();
            ErrorHandler.showInfo("Security System", "Security system has been armed");
        } catch (Exception e) {
            System.err.println("Error arming system: " + e.getMessage());
            ErrorHandler.showError("Error", "Failed to arm security system: " + e.getMessage());
        }
    }

    /**
     * Handles the Disarm System button click.
     */
    @FXML
    public void handleDisarmSystem() {
        if (homeController == null) {
            return;
        }

        try {
            homeController.disarmSecuritySystem();
            System.out.println("Security system disarmed");
            updateSecurityStatus();
            ErrorHandler.showInfo("Security System", "Security system has been disarmed");
        } catch (Exception e) {
            System.err.println("Error disarming system: " + e.getMessage());
            ErrorHandler.showError("Error", "Failed to disarm security system: " + e.getMessage());
        }
    }

    /**
     * Handles the Night Mode button click.
     */
    @FXML
    public void handleNightMode() {
        if (homeController == null) {
            return;
        }

        try {
            homeController.setNightMode();
            System.out.println("Night mode activated");
            updateSecurityStatus();
            ErrorHandler.showInfo("Security System", "Night mode has been activated");
        } catch (Exception e) {
            System.err.println("Error setting night mode: " + e.getMessage());
            ErrorHandler.showError("Error", "Failed to set night mode: " + e.getMessage());
        }
    }

    /**
     * Handles the Away Mode button click.
     */
    @FXML
    public void handleAwayMode() {
        if (homeController == null) {
            return;
        }

        try {
            homeController.setAwayMode();
            System.out.println("Away mode activated");
            updateSecurityStatus();
            ErrorHandler.showInfo("Security System", "Away mode has been activated");
        } catch (Exception e) {
            System.err.println("Error setting away mode: " + e.getMessage());
            ErrorHandler.showError("Error", "Failed to set away mode: " + e.getMessage());
        }
    }

    /**
     * Updates the security status display.
     */
    public void updateSecurityStatus() {
        if (securityController == null || statusLabel == null) {
            return;
        }

        Platform.runLater(() -> {
            try {
                SystemStatus status = homeController.getSecurityStatus();
                statusLabel.setText(status.toString());

                // Color code based on status
                String color = switch (status) {
                    case ARMED, AWAY_MODE -> "#F44336"; // Red
                    case DISARMED -> "#4CAF50"; // Green
                    case NIGHT_MODE -> "#2196F3"; // Blue
                };

                statusLabel.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
            } catch (Exception e) {
                System.err.println("Error updating security status: " + e.getMessage());
                statusLabel.setText("ERROR");
                statusLabel.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold; -fx-text-fill: #9E9E9E;");
            }
        });
    }

    /**
     * Loads all security devices and displays their statuses.
     */
    public void loadSecurityDevices() {
        if (securityController == null || securityDevicesContainer == null) {
            return;
        }

        Platform.runLater(() -> {
            securityDevicesContainer.getChildren().clear();

            try {
                Map<String, String> deviceStatuses = securityController.getAllDeviceStatuses();

                if (deviceStatuses.isEmpty()) {
                    Label noDevicesLabel = new Label("No security devices registered");
                    noDevicesLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: #9E9E9E;");
                    securityDevicesContainer.getChildren().add(noDevicesLabel);
                    return;
                }

                for (Map.Entry<String, String> entry : deviceStatuses.entrySet()) {
                    VBox deviceCard = createSecurityDeviceCard(entry.getKey(), entry.getValue());
                    securityDevicesContainer.getChildren().add(deviceCard);
                }
            } catch (Exception e) {
                System.err.println("Error loading security devices: " + e.getMessage());
                Label errorLabel = new Label("Error loading devices");
                errorLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: #F44336;");
                securityDevicesContainer.getChildren().add(errorLabel);
            }
        });
    }

    /**
     * Creates a card display for a security device.
     */
    private VBox createSecurityDeviceCard(String deviceId, String status) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #F5F5F5; -fx-background-radius: 4px;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Circle statusIndicator = new Circle(6);
        statusIndicator.setFill(javafx.scene.paint.Color.web(
                status.contains("active") || status.contains("ON") ? "#4CAF50" : "#9E9E9E"));

        Label idLabel = new Label(deviceId);
        idLabel.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold;");

        header.getChildren().addAll(statusIndicator, idLabel);

        Label statusText = new Label(status);
        statusText.setStyle("-fx-font-size: 11pt; -fx-text-fill: #666;");

        card.getChildren().addAll(header, statusText);
        return card;
    }

    /**
     * Loads and displays the security log.
     */
    public void loadSecurityLog() {
        if (securityController == null || securityLogContainer == null) {
            return;
        }

        Platform.runLater(() -> {
            securityLogContainer.getChildren().clear();

            try {
                List<String> logEntries = securityController.getSystemLog(50);

                if (logEntries.isEmpty()) {
                    Label noEventsLabel = new Label("No recent security events");
                    noEventsLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: #9E9E9E;");
                    securityLogContainer.getChildren().add(noEventsLabel);
                    return;
                }

                // Display last 10 entries
                int count = Math.min(10, logEntries.size());
                for (int i = logEntries.size() - 1; i >= logEntries.size() - count; i--) {
                    Label logEntry = new Label(logEntries.get(i));
                    logEntry.setStyle(
                            "-fx-font-size: 11pt; -fx-padding: 5px; -fx-background-color: #FAFAFA; -fx-background-radius: 3px;");
                    logEntry.setWrapText(true);
                    securityLogContainer.getChildren().add(logEntry);
                }
            } catch (Exception e) {
                System.err.println("Error loading security log: " + e.getMessage());
                Label errorLabel = new Label("Error loading security log");
                errorLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: #F44336;");
                securityLogContainer.getChildren().add(errorLabel);
            }
        });
    }

    /**
     * Refreshes all security panel data.
     */
    public void refresh() {
        updateSecurityStatus();
        loadSecurityDevices();
        loadSecurityLog();
    }
}
