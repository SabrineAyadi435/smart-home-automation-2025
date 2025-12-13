package com.ui.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Utility class for displaying error, warning, and confirmation dialogs.
 * Provides standardized user feedback throughout the application.
 */
public class ErrorHandler {
    
    /**
     * Displays an error dialog with the specified title and message.
     * 
     * @param title The title of the error dialog
     * @param message The error message to display
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        
        // Log error to console for debugging
        System.err.println("ERROR - " + title + ": " + message);
    }
    
    /**
     * Displays a warning dialog with the specified title and message.
     * 
     * @param title The title of the warning dialog
     * @param message The warning message to display
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        
        // Log warning to console for debugging
        System.out.println("WARNING - " + title + ": " + message);
    }
    
    /**
     * Displays a confirmation dialog with the specified title and message.
     * Returns true if the user clicks OK, false otherwise.
     * 
     * @param title The title of the confirmation dialog
     * @param message The confirmation message to display
     * @return true if user confirms (clicks OK), false otherwise
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Displays an information dialog with the specified title and message.
     * 
     * @param title The title of the information dialog
     * @param message The information message to display
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Displays a success message dialog.
     * 
     * @param title The title of the success dialog
     * @param message The success message to display
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
