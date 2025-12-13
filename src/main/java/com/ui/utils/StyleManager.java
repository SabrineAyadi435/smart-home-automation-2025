package com.ui.utils;

import javafx.scene.Scene;
import javafx.scene.Node;
import java.net.URL;

/**
 * Utility class for managing CSS stylesheets and applying styles to UI components.
 * Provides centralized CSS management for the application.
 */
public class StyleManager {
    
    // CSS file paths
    private static final String MAIN_CSS = "/com/ui/css/main.css";
    private static final String COMPONENTS_CSS = "/com/ui/css/components.css";
    
    /**
     * Applies the main application stylesheet to a scene.
     * 
     * @param scene The scene to apply the stylesheet to
     */
    public static void applyMainStylesheet(Scene scene) {
        if (scene == null) {
            System.err.println("Cannot apply stylesheet to null scene");
            return;
        }
        
        try {
            URL cssUrl = StyleManager.class.getResource(MAIN_CSS);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Main CSS file not found: " + MAIN_CSS);
            }
        } catch (Exception e) {
            System.err.println("Error loading main stylesheet: " + e.getMessage());
        }
    }
    
    /**
     * Applies the components stylesheet to a scene.
     * 
     * @param scene The scene to apply the stylesheet to
     */
    public static void applyComponentsStylesheet(Scene scene) {
        if (scene == null) {
            System.err.println("Cannot apply stylesheet to null scene");
            return;
        }
        
        try {
            URL cssUrl = StyleManager.class.getResource(COMPONENTS_CSS);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Components CSS file not found: " + COMPONENTS_CSS);
            }
        } catch (Exception e) {
            System.err.println("Error loading components stylesheet: " + e.getMessage());
        }
    }
    
    /**
     * Applies all application stylesheets to a scene.
     * 
     * @param scene The scene to apply stylesheets to
     */
    public static void applyAllStylesheets(Scene scene) {
        applyMainStylesheet(scene);
        applyComponentsStylesheet(scene);
    }
    
    /**
     * Adds a CSS class to a node.
     * 
     * @param node The node to add the class to
     * @param styleClass The CSS class name to add
     */
    public static void addStyleClass(Node node, String styleClass) {
        if (node != null && styleClass != null && !styleClass.isEmpty()) {
            if (!node.getStyleClass().contains(styleClass)) {
                node.getStyleClass().add(styleClass);
            }
        }
    }
    
    /**
     * Removes a CSS class from a node.
     * 
     * @param node The node to remove the class from
     * @param styleClass The CSS class name to remove
     */
    public static void removeStyleClass(Node node, String styleClass) {
        if (node != null && styleClass != null) {
            node.getStyleClass().remove(styleClass);
        }
    }
    
    /**
     * Toggles a CSS class on a node.
     * 
     * @param node The node to toggle the class on
     * @param styleClass The CSS class name to toggle
     */
    public static void toggleStyleClass(Node node, String styleClass) {
        if (node != null && styleClass != null) {
            if (node.getStyleClass().contains(styleClass)) {
                node.getStyleClass().remove(styleClass);
            } else {
                node.getStyleClass().add(styleClass);
            }
        }
    }
    
    /**
     * Applies inline style to a node.
     * 
     * @param node The node to apply the style to
     * @param style The inline CSS style string
     */
    public static void applyInlineStyle(Node node, String style) {
        if (node != null && style != null) {
            node.setStyle(style);
        }
    }
    
    /**
     * Gets the air quality color based on the air quality status.
     * 
     * @param airQuality The air quality status (GOOD, MODERATE, POOR, HAZARDOUS)
     * @return The CSS color string
     */
    public static String getAirQualityColor(String airQuality) {
        if (airQuality == null) {
            return "#9E9E9E"; // Gray for unknown
        }
        
        switch (airQuality.toUpperCase()) {
            case "GOOD":
                return "#4CAF50"; // Green
            case "MODERATE":
                return "#FFC107"; // Amber
            case "POOR":
                return "#FF9800"; // Orange
            case "HAZARDOUS":
                return "#F44336"; // Red
            default:
                return "#9E9E9E"; // Gray
        }
    }
    
    /**
     * Gets the device status color based on whether the device is on or off.
     * 
     * @param isOn true if device is on, false otherwise
     * @return The CSS color string
     */
    public static String getDeviceStatusColor(boolean isOn) {
        return isOn ? "#4CAF50" : "#9E9E9E"; // Green for ON, Gray for OFF
    }
    
    /**
     * Gets the security status color based on the security system status.
     * 
     * @param status The security status (ARMED, DISARMED, TRIGGERED, etc.)
     * @return The CSS color string
     */
    public static String getSecurityStatusColor(String status) {
        if (status == null) {
            return "#9E9E9E"; // Gray for unknown
        }
        
        switch (status.toUpperCase()) {
            case "ARMED":
            case "TRIGGERED":
            case "AWAY_MODE":
                return "#F44336"; // Red
            case "DISARMED":
                return "#4CAF50"; // Green
            case "NIGHT_MODE":
                return "#FF9800"; // Orange
            default:
                return "#9E9E9E"; // Gray
        }
    }
}
