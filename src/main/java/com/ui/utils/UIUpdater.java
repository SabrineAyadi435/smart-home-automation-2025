package com.ui.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

import com.ui.controllers.DashboardController;
import com.ui.controllers.HomeViewController;
import com.ui.controllers.MonitoringController;
import com.ui.controllers.ClimateController;
import com.ui.controllers.SecurityPanelController;

/**
 * Background UI updater that refreshes dashboard data periodically.
 * Uses JavaFX Timeline to update every 5 seconds.
 */
public class UIUpdater {

    private final Timeline updateTimeline;
    private DashboardController dashboardController;
    private HomeViewController currentHomeViewController;
    private MonitoringController currentMonitoringController;
    private ClimateController currentClimateController;
    private SecurityPanelController currentSecurityController;

    private static final int UPDATE_INTERVAL_SECONDS = 5;

    /**
     * Creates a new UIUpdater instance.
     * 
     * @param dashboardController The main dashboard controller
     */
    public UIUpdater(DashboardController dashboardController) {
        this.dashboardController = dashboardController;

        // Create timeline that runs every 5 seconds
        this.updateTimeline = new Timeline(
                new KeyFrame(Duration.seconds(UPDATE_INTERVAL_SECONDS), event -> refreshUI()));

        // Timeline repeats indefinitely
        updateTimeline.setCycleCount(Timeline.INDEFINITE);

        System.out.println("UIUpdater initialized with " + UPDATE_INTERVAL_SECONDS + " second interval");
    }

    /**
     * Starts the automatic UI updates.
     */
    public void start() {
        if (!updateTimeline.getStatus().equals(Timeline.Status.RUNNING)) {
            updateTimeline.play();
            System.out.println("UIUpdater started");
        }
    }

    /**
     * Stops the automatic UI updates.
     */
    public void stop() {
        if (updateTimeline.getStatus().equals(Timeline.Status.RUNNING)) {
            updateTimeline.stop();
            System.out.println("UIUpdater stopped");
        }
    }

    /**
     * Pauses the automatic UI updates.
     */
    public void pause() {
        if (updateTimeline.getStatus().equals(Timeline.Status.RUNNING)) {
            updateTimeline.pause();
            System.out.println("UIUpdater paused");
        }
    }

    /**
     * Resumes the automatic UI updates.
     */
    public void resume() {
        if (updateTimeline.getStatus().equals(Timeline.Status.PAUSED)) {
            updateTimeline.play();
            System.out.println("UIUpdater resumed");
        }
    }

    /**
     * Sets the current HomeViewController for updating.
     * 
     * @param controller The HomeViewController instance
     */
    public void setCurrentHomeViewController(HomeViewController controller) {
        this.currentHomeViewController = controller;
    }

    /**
     * Sets the current MonitoringController for updating.
     * 
     * @param controller The MonitoringController instance
     */
    public void setCurrentMonitoringController(MonitoringController controller) {
        this.currentMonitoringController = controller;
    }

    /**
     * Sets the current ClimateController for updating.
     * 
     * @param controller The ClimateController instance
     */
    public void setCurrentClimateController(ClimateController controller) {
        this.currentClimateController = controller;
    }

    /**
     * Sets the current SecurityPanelController for updating.
     * 
     * @param controller The SecurityPanelController instance
     */
    public void setCurrentSecurityController(SecurityPanelController controller) {
        this.currentSecurityController = controller;
    }

    /**
     * Refreshes the UI by updating the currently active view.
     * Runs on JavaFX Application Thread.
     */
    private void refreshUI() {
        Platform.runLater(() -> {
            try {
                // Update the currently active view controller
                if (currentHomeViewController != null) {
                    currentHomeViewController.refresh();
                }

                if (currentMonitoringController != null) {
                    currentMonitoringController.updateAllData();
                }

                if (currentClimateController != null) {
                    currentClimateController.refresh();
                }

                if (currentSecurityController != null) {
                    currentSecurityController.refresh();
                }

                // Update dashboard timestamp
                if (dashboardController != null) {
                    // The dashboard controller will update its last updated time
                    System.out.println("UI refreshed at " + java.time.LocalTime.now());
                }
            } catch (Exception e) {
                System.err.println("Error refreshing UI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Performs an immediate refresh of the UI (outside the normal schedule).
     */
    public void refreshNow() {
        refreshUI();
    }

    /**
     * Checks if the updater is currently running.
     * 
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return updateTimeline.getStatus().equals(Timeline.Status.RUNNING);
    }

    /**
     * Gets the update interval in seconds.
     * 
     * @return the update interval
     */
    public int getUpdateInterval() {
        return UPDATE_INTERVAL_SECONDS;
    }
}
