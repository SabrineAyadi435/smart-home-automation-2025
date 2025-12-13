package com.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Custom component for displaying monitoring metrics (energy, water
 * consumption).
 * Displays a title, value, and unit in a styled card format.
 */
public class MonitoringWidget extends VBox {
    private Label titleLabel;
    private Label valueLabel;
    private Label unitLabel;

    /**
     * No-argument constructor for FXML instantiation
     */
    public MonitoringWidget() {
        this("", "");
    }

    public MonitoringWidget(String title, String unit) {
        this.titleLabel = new Label(title);
        this.valueLabel = new Label("0.00");
        this.unitLabel = new Label(unit);

        setupUI();
    }

    private void setupUI() {
        // Set up styling
        this.getStyleClass().add("monitoring-widget");
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);

        // Style title label
        titleLabel.getStyleClass().add("widget-label");
        titleLabel.setStyle("-fx-font-size: 14pt; -fx-text-fill: #757575;");

        // Style value label
        valueLabel.getStyleClass().add("widget-value");
        valueLabel.setStyle("-fx-font-size: 32pt; -fx-font-weight: bold; -fx-text-fill: #85935E;");

        // Style unit label
        unitLabel.getStyleClass().add("widget-unit");
        unitLabel.setStyle("-fx-font-size: 12pt; -fx-text-fill: #9E9E9E;");

        // Add labels to widget
        this.getChildren().addAll(titleLabel, valueLabel, unitLabel);

        // Set minimum size
        this.setMinWidth(200);
        this.setMinHeight(150);

        // Add default styling
        this.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2); " +
                        "-fx-padding: 20px;");
    }

    /**
     * Update the displayed value
     * 
     * @param value the numeric value to display
     */
    public void setValue(double value) {
        valueLabel.setText(String.format("%.2f", value));
    }

    /**
     * Update the displayed value with custom formatting
     * 
     * @param formattedValue the formatted string to display
     */
    public void setValue(String formattedValue) {
        valueLabel.setText(formattedValue);
    }

    /**
     * Get the current displayed value
     * 
     * @return the value label text
     */
    public String getValue() {
        return valueLabel.getText();
    }

    /**
     * Update the title
     * 
     * @param title the new title
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     * Update the unit
     * 
     * @param unit the new unit
     */
    public void setUnit(String unit) {
        unitLabel.setText(unit);
    }
}
