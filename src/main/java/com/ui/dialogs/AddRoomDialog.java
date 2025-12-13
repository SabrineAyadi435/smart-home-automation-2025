package com.ui.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import com.room.Room;

/**
 * Dialog for adding a new room to the smart home.
 * Prompts the user for a room name and validates the input.
 */
public class AddRoomDialog extends Dialog<Room> {
    
    private TextField roomNameField;
    
    /**
     * Creates a new AddRoomDialog.
     */
    public AddRoomDialog() {
        setTitle("Add New Room");
        setHeaderText("Create a new room in your smart home");
        
        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        // Create the grid layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Create the room name field
        roomNameField = new TextField();
        roomNameField.setPromptText("e.g., Living Room, Kitchen, Bedroom");
        roomNameField.setPrefWidth(300);
        
        // Add labels and fields to the grid
        grid.add(new Label("Room Name:"), 0, 0);
        grid.add(roomNameField, 1, 0);
        
        // Add validation message label
        Label validationLabel = new Label();
        validationLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10pt;");
        grid.add(validationLabel, 1, 1);
        
        getDialogPane().setContent(grid);
        
        // Request focus on the room name field by default
        javafx.application.Platform.runLater(() -> roomNameField.requestFocus());
        
        // Disable the Add button initially
        javafx.scene.Node addButton = getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        
        // Enable/disable Add button based on input validation
        roomNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            String validationMessage = validateInput(newValue);
            if (validationMessage == null) {
                addButton.setDisable(false);
                validationLabel.setText("");
            } else {
                addButton.setDisable(true);
                validationLabel.setText(validationMessage);
            }
        });
        
        // Convert the result to a Room when the Add button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String roomName = roomNameField.getText().trim();
                if (validateInput(roomName) == null) {
                    return new Room(roomName);
                }
            }
            return null;
        });
    }
    
    /**
     * Validates the room name input.
     * 
     * @param roomName The room name to validate
     * @return An error message if validation fails, null if valid
     */
    private String validateInput(String roomName) {
        if (roomName == null || roomName.trim().isEmpty()) {
            return "Room name cannot be empty";
        }
        
        if (roomName.trim().length() < 2) {
            return "Room name must be at least 2 characters";
        }
        
        if (roomName.trim().length() > 50) {
            return "Room name must be less than 50 characters";
        }
        
        // Check for valid characters (letters, numbers, spaces, hyphens)
        if (!roomName.matches("^[a-zA-Z0-9\\s\\-]+$")) {
            return "Room name can only contain letters, numbers, spaces, and hyphens";
        }
        
        return null; // Valid
    }
}
