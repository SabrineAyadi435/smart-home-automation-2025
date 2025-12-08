package com.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import com.enums.AirQuality;
import com.room.Room;

/**
 * Custom component representing a room card in the home view.
 * Displays room name, device count, temperature, and air quality indicator.
 */
public class RoomCard extends VBox {
    
    private Room room;
    private Label nameLabel;
    private Label deviceCountLabel;
    private Label temperatureLabel;
    private Circle airQualityIndicator;
    private Label airQualityLabel;
    private Button removeButton;
    private Runnable onRemoveAction;
    
    /**
     * Creates a new RoomCard for the specified room.
     * 
     * @param room The room to display
     */
    public RoomCard(Room room) {
        this.room = room;
        initializeUI();
        updateDisplay();
    }
    
    /**
     * Initializes the UI components and layout.
     */
    private void initializeUI() {
        // Set card styling
        this.getStyleClass().add("room-card");
        this.setAlignment(Pos.TOP_LEFT);
        this.setSpacing(8);
        this.setPadding(new Insets(15));
        this.setPrefWidth(200);
        this.setPrefHeight(180);
        this.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 8; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
                     "-fx-cursor: hand;");
        
        // Room name label
        nameLabel = new Label();
        nameLabel.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold; -fx-text-fill: #212121;");
        
        // Device count label
        deviceCountLabel = new Label();
        deviceCountLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: #757575;");
        
        // Temperature label
        temperatureLabel = new Label();
        temperatureLabel.setStyle("-fx-font-size: 14pt; -fx-text-fill: #424242;");
        
        // Air quality indicator (circle + label)
        airQualityIndicator = new Circle(8);
        airQualityLabel = new Label();
        airQualityLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: #616161;");
        
        // Horizontal box for indicator and label
        HBox airQualityHBox = new HBox(8);
        airQualityHBox.setAlignment(Pos.CENTER_LEFT);
        airQualityHBox.getChildren().addAll(airQualityIndicator, airQualityLabel);
        
        // Spacer to push remove button to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // Remove button
        removeButton = new Button("Remove");
        removeButton.setStyle("-fx-background-color: #F44336; " +
                             "-fx-text-fill: white; " +
                             "-fx-font-size: 11pt; " +
                             "-fx-padding: 5 15 5 15; " +
                             "-fx-background-radius: 4; " +
                             "-fx-cursor: hand;");
        removeButton.setMaxWidth(Double.MAX_VALUE);
        
        // Prevent click event from propagating to the card
        removeButton.setOnMouseClicked(event -> {
            event.consume(); // Stop event from bubbling up
            if (onRemoveAction != null) {
                onRemoveAction.run();
            }
        });
        
        // Add hover effect to remove button
        removeButton.setOnMouseEntered(e -> {
            removeButton.setStyle("-fx-background-color: #D32F2F; " +
                                 "-fx-text-fill: white; " +
                                 "-fx-font-size: 11pt; " +
                                 "-fx-padding: 5 15 5 15; " +
                                 "-fx-background-radius: 4; " +
                                 "-fx-cursor: hand;");
        });
        
        removeButton.setOnMouseExited(e -> {
            removeButton.setStyle("-fx-background-color: #F44336; " +
                                 "-fx-text-fill: white; " +
                                 "-fx-font-size: 11pt; " +
                                 "-fx-padding: 5 15 5 15; " +
                                 "-fx-background-radius: 4; " +
                                 "-fx-cursor: hand;");
        });
        
        // Add all components to the card
        this.getChildren().addAll(
            nameLabel,
            deviceCountLabel,
            temperatureLabel,
            airQualityHBox,
            spacer,
            removeButton
        );
        
        // Add hover effect to card
        this.setOnMouseEntered(e -> {
            this.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 8; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3); " +
                         "-fx-cursor: hand;");
        });
        
        this.setOnMouseExited(e -> {
            this.setStyle("-fx-background-color: white; " +
                         "-fx-background-radius: 8; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
                         "-fx-cursor: hand;");
        });
    }
    
    /**
     * Updates the display with current room data.
     */
    public void updateDisplay() {
        if (room == null) {
            return;
        }
        
        // Update room name
        nameLabel.setText(room.getName());
        
        // Update device count
        int deviceCount = room.getDevices().size();
        String deviceText = deviceCount == 1 ? "device" : "devices";
        deviceCountLabel.setText(deviceCount + " " + deviceText);
        
        // Update temperature
        temperatureLabel.setText(String.format("%.1f°C", room.getTemperature()));
        
        // Update air quality
        AirQuality airQuality = room.getAirQuality();
        airQualityLabel.setText(airQuality.toString());
        airQualityIndicator.setFill(getAirQualityColor(airQuality));
    }
    
    /**
     * Gets the color for the air quality indicator based on the air quality level.
     * 
     * @param quality The air quality level
     * @return The color for the indicator
     */
    private Color getAirQualityColor(AirQuality quality) {
        return switch (quality) {
            case GOOD -> Color.web("#4CAF50");      // Green
            case MODERATE -> Color.web("#FFC107");  // Yellow/Amber
            case POOR -> Color.web("#FF9800");      // Orange
            default -> Color.web("#9E9E9E");        // Gray for unknown
        };
    }
    
    /**
     * Gets the room associated with this card.
     * 
     * @return The room
     */
    public Room getRoom() {
        return room;
    }
    
    /**
     * Sets a new room for this card and updates the display.
     * 
     * @param room The new room
     */
    public void setRoom(Room room) {
        this.room = room;
        updateDisplay();
    }
    
    /**
     * Sets the action to be performed when the remove button is clicked.
     * 
     * @param onRemoveAction The action to perform
     */
    public void setOnRemoveAction(Runnable onRemoveAction) {
        this.onRemoveAction = onRemoveAction;
    }
}
