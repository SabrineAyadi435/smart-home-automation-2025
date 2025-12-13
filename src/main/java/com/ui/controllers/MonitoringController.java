package com.ui.controllers;

import com.controller.HomeController;
import com.home.Home;
import com.room.Room;
import com.ui.components.MonitoringWidget;
import com.ui.models.RoomConsumption;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the monitoring panel that displays energy and water consumption.
 * Provides real-time updates of consumption metrics and per-room breakdown.
 */
public class MonitoringController {
    
    @FXML
    private MonitoringWidget totalEnergyWidget;
    
    @FXML
    private MonitoringWidget currentEnergyWidget;
    
    @FXML
    private MonitoringWidget totalWaterWidget;
    
    @FXML
    private MonitoringWidget currentWaterWidget;
    
    @FXML
    private TableView<RoomConsumption> roomConsumptionTable;
    
    @FXML
    private TableColumn<RoomConsumption, String> roomNameColumn;
    
    @FXML
    private TableColumn<RoomConsumption, Double> currentEnergyColumn;
    
    @FXML
    private TableColumn<RoomConsumption, Double> totalEnergyColumn;
    
    @FXML
    private TableColumn<RoomConsumption, Double> currentWaterColumn;
    
    @FXML
    private TableColumn<RoomConsumption, Double> totalWaterColumn;
    
    @FXML
    private Label lastUpdatedLabel;
    
    private HomeController homeController;
    private Timeline refreshTimeline;
    private ObservableList<RoomConsumption> roomConsumptionData;
    
    private static final int REFRESH_INTERVAL_SECONDS = 5;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Initialize the controller. Called automatically by JavaFX after FXML loading.
     */
    @FXML
    public void initialize() {
        // Initialize widgets with titles and units
        totalEnergyWidget.setTitle("Total Energy");
        totalEnergyWidget.setUnit("kWh");
        
        currentEnergyWidget.setTitle("Current Energy");
        currentEnergyWidget.setUnit("W");
        
        totalWaterWidget.setTitle("Total Water");
        totalWaterWidget.setUnit("L");
        
        currentWaterWidget.setTitle("Current Water");
        currentWaterWidget.setUnit("L/min");
        
        // Initialize table columns
        roomNameColumn.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        currentEnergyColumn.setCellValueFactory(new PropertyValueFactory<>("currentEnergy"));
        totalEnergyColumn.setCellValueFactory(new PropertyValueFactory<>("totalEnergy"));
        currentWaterColumn.setCellValueFactory(new PropertyValueFactory<>("currentWater"));
        totalWaterColumn.setCellValueFactory(new PropertyValueFactory<>("totalWater"));
        
        // Format numeric columns to 2 decimal places
        currentEnergyColumn.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        totalEnergyColumn.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        currentWaterColumn.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        totalWaterColumn.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        // Initialize observable list for table
        roomConsumptionData = FXCollections.observableArrayList();
        roomConsumptionTable.setItems(roomConsumptionData);
    }
    
    /**
     * Set the HomeController instance for accessing home data.
     * @param homeController the HomeController instance
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
        // Initial data load
        updateAllData();
        // Start auto-refresh
        startAutoRefresh();
    }
    
    /**
     * Update energy consumption data from the Home.
     * Updates both total and current energy widgets.
     */
    public void updateEnergyData() {
        if (homeController == null) {
            return;
        }
        
        Home home = homeController.getHome();
        if (home == null) {
            return;
        }
        
        // Get total energy consumption in kWh
        double totalEnergy = home.getTotalEnergyConsumption();
        totalEnergyWidget.setValue(totalEnergy);
        
        // Get current energy consumption in W
        double currentEnergy = home.getCurrentEnergyConsumption();
        currentEnergyWidget.setValue(currentEnergy);
    }
    
    /**
     * Update water consumption data from the Home.
     * Updates both total and current water widgets.
     */
    public void updateWaterData() {
        if (homeController == null) {
            return;
        }
        
        Home home = homeController.getHome();
        if (home == null) {
            return;
        }
        
        // Get total water consumption in L
        double totalWater = home.getTotalwaterConsumption();
        totalWaterWidget.setValue(totalWater);
        
        // Get current water consumption in L/min
        double currentWater = home.getCurrentwaterConsumption();
        currentWaterWidget.setValue(currentWater);
    }
    
    /**
     * Update the per-room consumption table.
     * Populates the table with consumption data for each room.
     */
    public void updateRoomConsumption() {
        if (homeController == null) {
            return;
        }
        
        Home home = homeController.getHome();
        if (home == null) {
            return;
        }
        
        // Clear existing data
        roomConsumptionData.clear();
        
        // Populate with room data
        for (Room room : home.getRooms()) {
            String roomName = room.getName();
            double currentEnergy = room.getCurrentEnergyConsumption();
            double totalEnergy = room.getTotalEnergyConsumptionKWh();
            
            // Water consumption only for Bathroom and Kitchen
            double currentWater = 0.0;
            double totalWater = 0.0;
            if (roomName.equals("Bathroom") || roomName.equals("Kitchen")) {
                currentWater = room.getCurrentwaterConsumption();
                totalWater = room.getTotalwaterConsumption();
            }
            
            RoomConsumption consumption = new RoomConsumption(
                roomName, currentEnergy, totalEnergy, currentWater, totalWater
            );
            roomConsumptionData.add(consumption);
        }
    }
    
    /**
     * Update all monitoring data (energy, water, and room consumption).
     */
    public void updateAllData() {
        updateEnergyData();
        updateWaterData();
        updateRoomConsumption();
        updateLastUpdatedLabel();
    }
    
    /**
     * Update the last updated timestamp label.
     */
    private void updateLastUpdatedLabel() {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        lastUpdatedLabel.setText("Last Updated: " + timestamp);
    }
    
    /**
     * Start automatic refresh of monitoring data every 5 seconds.
     */
    public void startAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        
        refreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(REFRESH_INTERVAL_SECONDS), event -> {
                // Use Platform.runLater to ensure UI updates happen on JavaFX thread
                Platform.runLater(this::updateAllData);
            })
        );
        
        refreshTimeline.setCycleCount(Animation.INDEFINITE);
        refreshTimeline.play();
    }
    
    /**
     * Stop automatic refresh of monitoring data.
     */
    public void stopAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
            refreshTimeline = null;
        }
    }
    
    /**
     * Get the HomeController instance.
     * @return the HomeController
     */
    public HomeController getHomeController() {
        return homeController;
    }
    
    /**
     * Cleanup method to be called when the controller is no longer needed.
     */
    public void cleanup() {
        stopAutoRefresh();
    }
}
