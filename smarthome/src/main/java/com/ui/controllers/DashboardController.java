package com.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.controller.HomeController;
import com.controller.SecurityController;
import com.ui.utils.UIUpdater;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main controller for the Dashboard.
 * Manages navigation between different views and coordinates data flow between
 * UI and backend.
 */
public class DashboardController {

    // FXML injected components
    @FXML
    private BorderPane mainLayout;

    @FXML
    private VBox navigationMenu;

    @FXML
    private StackPane contentArea;

    @FXML
    private Button homeViewButton;

    @FXML
    private Button monitoringViewButton;

    @FXML
    private Button climateViewButton;

    @FXML
    private Button securityViewButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label lastUpdatedLabel;

    @FXML
    private VBox toastContainer;

    // Time Control UI
    @FXML
    private Label simulatedTimeLabel;

    @FXML
    private Button rewindBtn;

    @FXML
    private Button pausePlayBtn;

    @FXML
    private Button forwardBtn;

    @FXML
    private javafx.scene.control.ComboBox<String> speedCombo;

    // Backend controllers
    private HomeController homeController;
    private SecurityController securityController;

    // UI Updater for automatic refresh
    private UIUpdater uiUpdater;

    // Time Simulator
    private com.utils.TimeSimulator timeSimulator;

    // Current active view button
    private Button activeButton;

    // Date formatter for last updated timestamp
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Initializes the controller after FXML loading.
     * Sets up initial UI state.
     */
    @FXML
    public void initialize() {
        System.out.println("DashboardController initialized");

        // Set home view as default active button
        activeButton = homeViewButton;
        updateActiveButton(homeViewButton);

        // Initialize UIUpdater
        uiUpdater = new UIUpdater(this);

        // Update status
        updateStatus("Dashboard initialized");
        updateLastUpdatedTime();

        // Setup toast notification listener
        setupToastNotifications();

        // Setup time simulation controls
        setupTimeControls();
    }

    /**
     * Sets up toast notification system to display notifications as overlays
     */
    private void setupToastNotifications() {
        com.ui.utils.NotificationManager.getInstance().getNotifications().addListener(
                (javafx.collections.ListChangeListener.Change<? extends com.ui.models.Notification> change) -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            for (com.ui.models.Notification notification : change.getAddedSubList()) {
                                showToastNotification(notification);
                            }
                        }
                    }
                });
    }

    /**
     * Shows a toast notification as an overlay
     */
    private void showToastNotification(com.ui.models.Notification notification) {
        Platform.runLater(() -> {
            final com.ui.components.ToastNotification[] toastHolder = new com.ui.components.ToastNotification[1];
            toastHolder[0] = new com.ui.components.ToastNotification(
                    notification,
                    () -> toastContainer.getChildren().remove(toastHolder[0]));
            toastContainer.getChildren().add(toastHolder[0]);
        });
    }

    /**
     * Ramadan Mode button
     */
    @FXML
    private Button ramadanModeBtn;

    /**
     * Sets up time simulation controls
     */
    private void setupTimeControls() {
        // Initialize TimeSimulator
        timeSimulator = com.utils.TimeSimulator.getInstance();

        // Bind time label to simulated time
        timeSimulator.simulatedTimeProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                simulatedTimeLabel.setText(timeSimulator.getFormattedDateTime());
            });
        });

        // Initialize label
        simulatedTimeLabel.setText(timeSimulator.getFormattedDateTime());

        // Setup speed combo box
        speedCombo.getItems().addAll("0.1x", "0.5x", "1x", "2x", "5x", "10x", "50x", "100x");
        speedCombo.setValue("1x");
        speedCombo.setOnAction(e -> {
            String selected = speedCombo.getValue();
            double speed = Double.parseDouble(selected.replace("x", ""));
            timeSimulator.setSpeed(speed);
        });

        // Pause/Play button
        pausePlayBtn.setOnAction(e -> {
            timeSimulator.togglePause();
            pausePlayBtn.setText(timeSimulator.isPaused() ? "▶" : "⏸");
            pausePlayBtn.setStyle(timeSimulator.isPaused()
                    ? "-fx-font-size: 10pt; -fx-padding: 4 8; -fx-background-color: #4CAF50; -fx-text-fill: white;"
                    : "-fx-font-size: 10pt; -fx-padding: 4 8; -fx-background-color: #FF9800; -fx-text-fill: white;");
        });

        // Rewind button (1 hour back)
        rewindBtn.setOnAction(e -> {
            timeSimulator.rewind(1);
        });

        // Forward button (1 hour ahead)
        forwardBtn.setOnAction(e -> {
            timeSimulator.fastForward(1);
        });

        // Ramadan Mode Button
        if (ramadanModeBtn != null) {
            ramadanModeBtn.setOnAction(e -> {
                if (homeController != null) {
                    if (homeController.isRamadanModeActive()) {
                        homeController.deactivateRamadanMode();
                        ramadanModeBtn.setText("🌙 Ramadan Mode");
                        ramadanModeBtn.setStyle(
                                "-fx-font-size: 10pt; -fx-padding: 4 8; -fx-background-color: #78909C; -fx-text-fill: white;");
                    } else {
                        homeController.activateRamadanMode();
                        ramadanModeBtn.setText("🌙 Ramadan Active");
                        ramadanModeBtn.setStyle(
                                "-fx-font-size: 10pt; -fx-padding: 4 8; -fx-background-color: #00695C; -fx-text-fill: white;");
                    }
                }
            });
        }

        // Register tick listener for consumption updates and scheduled events
        timeSimulator.addTickListener(simulatedSeconds -> {
            if (homeController != null) {
                homeController.updateConsumption(simulatedSeconds);
                // Check for scheduled events (Athan, etc.)
                homeController.checkScheduledEvents(timeSimulator.now());
            }
        });

        System.out.println("[DashboardController] Time controls initialized");
    }

    /**
     * Sets the HomeController instance for backend operations.
     * 
     * @param homeController The HomeController managing the
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
        this.securityController = homeController.getSecurityController();
        System.out.println("HomeController set in DashboardController");

        // Update status with home information
        if (homeController != null) {
            updateStatus("Connected to  System");

            // Start automatic UI updates
            if (uiUpdater != null) {
                uiUpdater.start();
            }
        }
    }

    /**
     * Stops the UIUpdater for cleanup during application shutdown.
     */
    public void stopUIUpdater() {
        if (uiUpdater != null) {
            uiUpdater.stop();
        }
    }

    /**
     * Gets the HomeController instance.
     * 
     * @return The HomeController instance
     */
    public HomeController getHomeController() {
        return homeController;
    }

    /**
     * Gets the SecurityController instance.
     * 
     * @return The SecurityController instance
     */
    public SecurityController getSecurityController() {
        return securityController;
    }

    /**
     * Gets the content area StackPane for loading views.
     * 
     * @return The content area StackPane
     */
    public StackPane getContentArea() {
        return contentArea;
    }

    /**
     * Shows the Home View with all rooms displayed in a grid layout.
     */
    @FXML
    public void showHomeView() {
        System.out.println("Navigating to Home View");
        updateActiveButton(homeViewButton);
        updateStatus("Home View - Managing rooms and devices");

        try {
            // Load home view FXML
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/ui/fxml/home-view.fxml"));
            javafx.scene.Parent homeView = loader.load();

            // Get the controller and set HomeController
            com.ui.controllers.HomeViewController homeViewController = loader.getController();
            if (homeViewController != null && homeController != null) {
                homeViewController.setHomeController(homeController);
                homeViewController.setDashboardController(this); // Fix: Set dashboard controller for navigation

                // Register with UIUpdater for auto-refresh
                if (uiUpdater != null) {
                    uiUpdater.setCurrentHomeViewController(homeViewController);
                }
            }

            // Display the home view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(homeView);

            System.out.println("Home View loaded successfully");
        } catch (java.io.IOException e) {
            System.err.println("[ERROR] [DashboardController] Failed to load Home View FXML: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "View Loading Error",
                    "Failed to load Home View. Please restart the application.\n\nDetails: " + e.getMessage());
            showPlaceholder("Error", "Home View could not be loaded");
        } catch (Exception e) {
            System.err.println("[ERROR] [DashboardController] Unexpected error loading Home View: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while loading Home View.\n\nDetails: " + e.getMessage());
            showPlaceholder("Error", "An unexpected error occurred");
        }
    }

    /**
     * Shows the Monitoring View with energy and water consumption widgets.
     */
    @FXML
    public void showMonitoringView() {
        System.out.println("Navigating to Monitoring View");
        updateActiveButton(monitoringViewButton);
        updateStatus("Monitoring View - Energy and water consumption metrics");

        try {
            // Load monitoring panel FXML
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/ui/fxml/monitoring-panel.fxml"));
            javafx.scene.Parent monitoringView = loader.load();

            // Get the controller and set HomeController
            com.ui.controllers.MonitoringController monitoringController = loader.getController();
            if (monitoringController != null && homeController != null) {
                monitoringController.setHomeController(homeController);

                // Register with UIUpdater for auto-refresh
                if (uiUpdater != null) {
                    uiUpdater.setCurrentMonitoringController(monitoringController);
                }
            }

            // Display the monitoring view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(monitoringView);

            System.out.println("Monitoring View loaded successfully");
        } catch (java.io.IOException e) {
            System.err.println("[ERROR] [DashboardController] Failed to load Monitoring View FXML: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "View Loading Error",
                    "Failed to load Monitoring View. Please restart the application.\n\nDetails: " + e.getMessage());
            showPlaceholder("Error", "Monitoring View could not be loaded");
        } catch (Exception e) {
            System.err.println(
                    "[ERROR] [DashboardController] Unexpected error loading Monitoring View: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while loading Monitoring View.\n\nDetails: " + e.getMessage());
            showPlaceholder("Error", "An unexpected error occurred");
        }
    }

    /**
     * Shows the Climate Control View with temperature and air quality controls.
     * This view will be implemented in a later task.
     */
    @FXML
    public void showClimateView() {
        System.out.println("Navigating to Climate View");
        updateActiveButton(climateViewButton);
        updateStatus("Climate View - Temperature & Air Quality control");

        try {
            // Load climate view FXML
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/ui/fxml/climate-control.fxml"));
            javafx.scene.Parent climateView = loader.load();

            // Get the controller and set HomeController
            ClimateController climateController = loader.getController();
            if (climateController != null && homeController != null) {
                climateController.setHomeController(homeController);

                // Register with UIUpdater
                if (uiUpdater != null) {
                    uiUpdater.setCurrentClimateController(climateController);
                }
            }

            // Display the climate view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(climateView);

            System.out.println("Climate View loaded successfully");
        } catch (java.io.IOException e) {
            System.err.println("[ERROR] [DashboardController] Failed to load Climate View FXML: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "View Loading Error",
                    "Failed to load Climate Control View. Please restart the application.\n\nDetails: "
                            + e.getMessage());
            showPlaceholder("Error", "Climate View could not be loaded");
        } catch (Exception e) {
            System.err
                    .println("[ERROR] [DashboardController] Unexpected error loading Climate View: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while loading Climate View.\n\nDetails: " + e.getMessage());
            showPlaceholder("Error", "An unexpected error occurred");
        }
    }

    /**
     * Shows the Security View with security system status and controls.
     * This view will be implemented in a later task.
     */
    @FXML
    public void showSecurityView() {
        System.out.println("Navigating to Security View");
        updateActiveButton(securityViewButton);
        updateStatus("Security View - System status and controls");

        try {
            // Load security panel FXML
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/ui/fxml/security-panel.fxml"));
            javafx.scene.Parent securityView = loader.load();

            // Get the controller and set HomeController
            SecurityPanelController securityController = loader.getController();
            if (securityController != null && homeController != null) {
                securityController.setHomeController(homeController);

                // Register with UIUpdater
                if (uiUpdater != null) {
                    uiUpdater.setCurrentSecurityController(securityController);
                }
            }

            // Display the security view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(securityView);

            System.out.println("Security View loaded successfully");
        } catch (java.io.IOException e) {
            System.err.println("[ERROR] [DashboardController] Failed to load Security Panel FXML: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "View Loading Error",
                    "Failed to load Security Panel. Please restart the application.\n\nDetails: " + e.getMessage());
            showPlaceholder("Error", "Security Panel could not be loaded");
        } catch (Exception e) {
            System.err.println(
                    "[ERROR] [DashboardController] Unexpected error loading Security Panel: " + e.getMessage());
            e.printStackTrace();
            com.ui.utils.ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred while loading Security Panel.\n\nDetails: " + e.getMessage());
            showPlaceholder("Error", "An unexpected error occurred");
        }
    }

    /**
     * Refreshes all data across the dashboard.
     * Updates monitoring widgets, room displays, and device statuses.
     */
    @FXML
    public void refreshAllData() {
        System.out.println("Refreshing all dashboard data");
        updateStatus("Refreshing all data...");

        // Perform refresh in background to avoid blocking UI
        new Thread(() -> {
            try {
                // Simulate data refresh
                Thread.sleep(500);

                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    updateLastUpdatedTime();
                    updateStatus("Data refreshed successfully");

                    // TODO: Trigger refresh on active view controller
                    // This will be implemented when view controllers are created
                    System.out.println("All data refreshed");
                });
            } catch (InterruptedException e) {
                Platform.runLater(() -> {
                    updateStatus("Refresh failed");
                    System.err.println("Error refreshing data: " + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Updates the active navigation button styling.
     * 
     * @param button The button to mark as active
     */
    private void updateActiveButton(Button button) {
        // Reset previous active button
        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                    "-fx-font-size: 14pt; -fx-alignment: center-left; -fx-padding: 10 20;");
        }

        // Set new active button
        activeButton = button;
        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white; " +
                    "-fx-font-size: 14pt; -fx-alignment: center-left; -fx-padding: 10 20; " +
                    "-fx-background-radius: 4;");
        }
    }

    /**
     * Updates the status label text.
     * 
     * @param status The status message to display
     */
    private void updateStatus(String status) {
        if (statusLabel != null) {
            statusLabel.setText("Status: " + status);
        }
    }

    /**
     * Updates the last updated timestamp.
     */
    private void updateLastUpdatedTime() {
        if (lastUpdatedLabel != null) {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            lastUpdatedLabel.setText("Last Updated: " + timestamp);
        }
    }

    /**
     * Shows a placeholder view with a title and message.
     * Used temporarily until actual views are implemented.
     * 
     * @param title   The title to display
     * @param message The message to display
     */
    private void showPlaceholder(String title, String message) {
        VBox placeholder = new VBox(20);
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setStyle("-fx-background-color: #FAFAFA;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 32pt; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16pt; -fx-text-fill: #757575;");

        Label infoLabel = new Label("✓ Navigation working correctly");
        infoLabel.setStyle("-fx-font-size: 14pt; -fx-text-fill: #4CAF50;");

        placeholder.getChildren().addAll(titleLabel, messageLabel, infoLabel);

        contentArea.getChildren().clear();
        contentArea.getChildren().add(placeholder);
    }
}
