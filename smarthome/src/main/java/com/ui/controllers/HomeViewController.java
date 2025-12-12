package com.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;

import com.controller.HomeController;
import com.room.Room;
import com.ui.components.RoomCard;
import com.ui.dialogs.AddRoomDialog;
import com.ui.utils.ErrorHandler;

import java.util.List;
import java.util.Optional;

/**
 * Controller for the Home View that displays all rooms in a dynamic visual
 * floor plan.
 * Handles room addition, removal, and navigation to room details.
 */
public class HomeViewController {

    @FXML
    private AnchorPane floorPlanCanvas;

    @FXML
    private Button addRoomButton;

    private HomeController homeController;
    private DashboardController dashboardController;

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        System.out.println("HomeViewController initialized");
    }

    /**
     * Sets the HomeController instance for backend operations.
     * 
     * @param homeController The HomeController managing the smart home
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
        System.out.println("HomeController set in HomeViewController");
        loadRooms();
    }

    /**
     * Sets the DashboardController for navigation purposes.
     * 
     * @param dashboardController The main dashboard controller
     */
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;

        // Subscribe to time updates for dynamic background
        com.utils.TimeSimulator.getInstance().simulatedTimeProperty().addListener((obs, oldVal, newVal) -> {
            javafx.application.Platform.runLater(() -> updateTime(newVal));
        });

        // Initial update
        updateTime(com.utils.TimeSimulator.getInstance().now());
    }

    /**
     * Updates the view based on the current time (Day/Night cycle).
     * Day: 06:00 - 18:00
     * Night: 18:00 - 06:00
     */
    private void updateTime(java.time.LocalDateTime time) {
        if (floorPlanCanvas == null || floorPlanCanvas.getScene() == null)
            return;

        int hour = time.getHour();
        boolean isNight = hour < 6 || hour >= 18;

        // Get the main scroll pane content (VBox) which holds the background
        if (floorPlanCanvas.getParent() instanceof javafx.scene.layout.VBox) {
            javafx.scene.layout.VBox container = (javafx.scene.layout.VBox) floorPlanCanvas.getParent();

            // Remove existing theme classes
            container.getStyleClass().removeAll("home-view-day", "home-view-night");

            // Add appropriate class
            if (isNight) {
                if (!container.getStyleClass().contains("home-view-night")) {
                    container.getStyleClass().add("home-view-night");
                    System.out.println("Switched to NIGHT mode (Hour: " + hour + ")");
                }
            } else {
                if (!container.getStyleClass().contains("home-view-day")) {
                    container.getStyleClass().add("home-view-day");
                    System.out.println("Switched to DAY mode (Hour: " + hour + ")");
                }
            }
        } else {
            System.err.println("Error: floorPlanCanvas parent is not a VBox, cannot apply background style.");
        }

        // Update Sun/Moon visibility
        if (sunShape != null)
            sunShape.setVisible(!isNight);
        if (moonShape != null)
            moonShape.setVisible(isNight);
    }

    @FXML
    private javafx.scene.layout.StackPane roomSlot2; // Bedroom
    @FXML
    private javafx.scene.layout.StackPane roomSlot3; // Kitchen
    @FXML
    private javafx.scene.layout.StackPane roomSlot4; // Bathroom/Entrance

    private javafx.scene.shape.Circle sunShape;
    private javafx.scene.shape.Circle moonShape;

    /**
     * Loads all rooms from the home and displays them in the dynamic visual
     * floor plan.
     */
    public void loadRooms() {
        if (homeController == null) {
            System.err.println("Cannot load rooms: HomeController is null");
            return;
        }

        // Clear existing content
        floorPlanCanvas.getChildren().clear();

        // Draw Sky Elements (Sun/Moon)
        drawSkyElements();

        // Get all rooms from the home
        List<Room> rooms = homeController.getHome().getRooms();
        int roomCount = rooms.size();

        System.out.println("Loading " + roomCount + " rooms into dynamic floor plan");

        if (roomCount == 0)
            return;

        // Calculate grid dimensions
        // We want roughly a square or slightly rectangular aspect ratio
        // For 4 rooms: 2x2. For 5 rooms: 2 cols, 3 rows (last row has 1). For 6: 2x3.
        // Let's aim for 2 columns minimum, maybe 3 if we have many rooms.
        int cols = 2;
        if (roomCount > 6)
            cols = 3;
        int rows = (int) Math.ceil((double) roomCount / cols);

        drawDynamicHouse(rooms, cols, rows);
    }

    /**
     * Draws the Sun and Moon elements.
     */
    private void drawSkyElements() {
        // Sun - Top Right
        sunShape = new javafx.scene.shape.Circle(750, 80, 40);
        sunShape.setFill(javafx.scene.paint.Color.web("#FFEB3B")); // Yellow
        sunShape.setEffect(new javafx.scene.effect.DropShadow(20, javafx.scene.paint.Color.web("#FFC107"))); // Glow

        // Moon - Top Right (same position)
        moonShape = new javafx.scene.shape.Circle(750, 80, 35);
        moonShape.setFill(javafx.scene.paint.Color.web("#F5F5F5")); // White/Grey
        moonShape.setEffect(new javafx.scene.effect.DropShadow(15, javafx.scene.paint.Color.WHITE)); // Glow

        // Add to canvas
        floorPlanCanvas.getChildren().addAll(sunShape, moonShape);

        // Initial visibility will be set by updateTime
    }

    private void drawDynamicHouse(List<Room> rooms, int cols, int rows) {
        // Constants for layout
        double roomWidth = 220;
        double roomHeight = 220;
        double padding = 20; // Padding inside the house
        double wallThickness = 3;

        // Calculate house body dimensions
        double bodyWidth = (cols * roomWidth) + ((cols + 1) * padding);
        double bodyHeight = (rows * roomHeight) + ((rows + 1) * padding);

        // Starting position (centered horizontally)
        double startX = (floorPlanCanvas.getPrefWidth() - bodyWidth) / 2;
        double startY = 150; // Leave space for roof

        // Draw Chimney
        javafx.scene.shape.Rectangle chimney = new javafx.scene.shape.Rectangle(
                startX + bodyWidth - 80, startY - 80,
                50, 80);
        chimney.setFill(javafx.scene.paint.Color.web("#8D6E63"));
        chimney.setStroke(javafx.scene.paint.Color.BLACK);
        chimney.setStrokeWidth(2);
        floorPlanCanvas.getChildren().add(chimney);

        // Draw Dome Roof (Islamic style)
        javafx.scene.shape.Path dome = new javafx.scene.shape.Path();
        double domeHeight = 100; // Height of the dome
        double domeWidth = bodyWidth + 40; // Width extends past house slightly
        double domeStartX = startX - 20;
        double domeEndX = startX + bodyWidth + 20;
        double domePeakY = startY - domeHeight;
        double domeControlY = startY - domeHeight * 1.8; // Control point for curve

        dome.getElements().addAll(
                new javafx.scene.shape.MoveTo(domeStartX, startY),
                new javafx.scene.shape.QuadCurveTo(
                        startX + (bodyWidth / 2), domeControlY, // Control point (top center, higher)
                        domeEndX, startY), // End point (right side)
                new javafx.scene.shape.ClosePath());
        dome.setFill(javafx.scene.paint.Color.web("#00695C")); // Islamic green
        dome.setStroke(javafx.scene.paint.Color.web("#004D40"));
        dome.setStrokeWidth(3);
        floorPlanCanvas.getChildren().add(dome);

        // Add crescent moon on top of dome
        double crescentSize = 25;
        double crescentX = startX + (bodyWidth / 2);
        double crescentY = domePeakY + 15;

        // Crescent moon using two arcs
        javafx.scene.shape.Arc crescentOuter = new javafx.scene.shape.Arc(
                crescentX, crescentY, crescentSize, crescentSize, 0, 180);
        crescentOuter.setType(javafx.scene.shape.ArcType.OPEN);
        crescentOuter.setFill(null);
        // crescentOuter.setStroke(javafx.scene.paint.Color.web("#FFD700")); // Gold
        crescentOuter.setStrokeWidth(4);
        floorPlanCanvas.getChildren().add(crescentOuter);

        // Draw House Body
        javafx.scene.shape.Rectangle body = new javafx.scene.shape.Rectangle(
                startX, startY, bodyWidth, bodyHeight);
        body.setFill(javafx.scene.paint.Color.web("#FFF8E1"));
        body.setStroke(javafx.scene.paint.Color.BLACK);
        body.setStrokeWidth(wallThickness);
        floorPlanCanvas.getChildren().add(body);

        // Draw Rooms and Dividers
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            int col = i % cols;
            int row = i / cols;

            double x = startX + padding + (col * (roomWidth + padding));
            double y = startY + padding + (row * (roomHeight + padding));

            // Create Room Card
            RoomCard roomCard = createRoomCard(room);
            roomCard.setPrefWidth(roomWidth);
            roomCard.setPrefHeight(roomHeight);
            roomCard.setLayoutX(x);
            roomCard.setLayoutY(y);

            floorPlanCanvas.getChildren().add(roomCard);

            // Draw vertical divider to the right if not last column
            if (col < cols - 1) {
                double divX = x + roomWidth + (padding / 2);
                javafx.scene.shape.Line vLine = new javafx.scene.shape.Line(
                        divX, startY, divX, startY + bodyHeight);
                vLine.setStroke(javafx.scene.paint.Color.BLACK);
                vLine.setStrokeWidth(wallThickness);
                // Only add if not already added (simple check: we draw full height lines)
                // Actually, simpler to just draw grid lines separately
            }
        }

        // Draw Grid Lines (simpler approach)
        // Vertical lines
        for (int c = 1; c < cols; c++) {
            double lineX = startX + (c * (roomWidth + padding)) + (padding / 2); // Approximation
            // Precise calculation: startX + padding + c*roomWidth + (c-1)*padding +
            // padding/2 ?
            // Let's stick to the grid logic:
            // Col 0 ends at: startX + padding + roomWidth
            // Col 1 starts at: startX + padding + roomWidth + padding
            // Line should be at: startX + padding + roomWidth + padding/2

            double divX = startX + (c * roomWidth) + (c * padding) + (padding / 2);

            javafx.scene.shape.Line vLine = new javafx.scene.shape.Line(
                    divX, startY, divX, startY + bodyHeight);
            vLine.setStroke(javafx.scene.paint.Color.BLACK);
            vLine.setStrokeWidth(wallThickness);
            floorPlanCanvas.getChildren().add(vLine);
        }

        // Horizontal lines
        for (int r = 1; r < rows; r++) {
            double divY = startY + (r * roomHeight) + (r * padding) + (padding / 2);
            javafx.scene.shape.Line hLine = new javafx.scene.shape.Line(
                    startX, divY, startX + bodyWidth, divY);
            hLine.setStroke(javafx.scene.paint.Color.BLACK);
            hLine.setStrokeWidth(wallThickness);
            floorPlanCanvas.getChildren().add(hLine);
        }
    }

    /**
     * Creates a room card for the specified room with click handler.
     * 
     * @param room The room to create a card for
     * @return The created RoomCard
     */
    private RoomCard createRoomCard(Room room) {
        RoomCard roomCard = new RoomCard(room);

        // Add click handler to open room detail view
        roomCard.setOnMouseClicked(event -> {
            openRoomDetail(room);
        });

        // Set the remove action for the remove button
        roomCard.setOnRemoveAction(() -> {
            removeRoom(room);
        });

        return roomCard;
    }

    /**
     * Opens the room detail view for the specified room.
     * 
     * @param room The room to display details for
     */
    private void openRoomDetail(Room room) {
        System.out.println("Opening room detail for: " + room.getName());

        if (dashboardController == null) {
            System.err.println("Cannot open room detail: DashboardController is null");
            ErrorHandler.showError("Navigation Error", "Cannot open room details");
            return;
        }

        try {
            // Load room panel FXML
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/ui/fxml/room-panel.fxml"));
            javafx.scene.Parent roomPanel = loader.load();

            // Get the controller and set up the room
            RoomPanelController roomPanelController = loader.getController();
            if (roomPanelController != null) {
                roomPanelController.setHomeController(homeController);
                roomPanelController.setDashboardController(dashboardController);
                roomPanelController.setRoom(room);
            }

            // Display the room panel in the content area
            dashboardController.getContentArea().getChildren().clear();
            dashboardController.getContentArea().getChildren().add(roomPanel);

            System.out.println("Room detail view loaded successfully for: " + room.getName());
        } catch (Exception e) {
            System.err.println("Error loading room detail view: " + e.getMessage());
            e.printStackTrace();
            ErrorHandler.showError("Error", "Failed to load room details: " + e.getMessage());
        }
    }

    /**
     * Handles the Add Room button click.
     * Opens a dialog to create a new room.
     */
    @FXML
    public void addRoom() {
        System.out.println("Add Room button clicked");

        if (homeController == null) {
            ErrorHandler.showError("Error", "Cannot add room: HomeController is not initialized");
            return;
        }

        // Create and show the Add Room dialog
        AddRoomDialog dialog = new AddRoomDialog();
        Optional<Room> result = dialog.showAndWait();

        // If user confirmed, add the room
        result.ifPresent(room -> {
            try {
                homeController.getHome().addRoom(room);
                System.out.println("Room added: " + room.getName());

                // Reload rooms to display the new room
                loadRooms();

                // Show success message
                ErrorHandler.showSuccess("Success", "Room '" + room.getName() + "' has been added successfully.");
            } catch (Exception e) {
                System.err.println("Error adding room: " + e.getMessage());
                ErrorHandler.showError("Error", "Failed to add room: " + e.getMessage());
            }
        });
    }

    /**
     * Removes the specified room after confirmation.
     * 
     * @param room The room to remove
     */
    public void removeRoom(Room room) {
        if (room == null || homeController == null) {
            return;
        }

        // Check if room has devices
        if (!room.getDevices().isEmpty()) {
            ErrorHandler.showWarning("Cannot Remove Room",
                    "Room '" + room.getName() + "' contains " + room.getDevices().size() +
                            " device(s). Please remove all devices before deleting the room.");
            return;
        }

        // Show confirmation dialog using ErrorHandler
        boolean confirmed = ErrorHandler.showConfirmation(
                "Confirm Removal",
                "Are you sure you want to remove room '" + room.getName() + "'?");

        if (confirmed) {
            try {
                homeController.getHome().removeRoom(room.getName());
                System.out.println("Room removed: " + room.getName());

                // Reload rooms to update the display
                loadRooms();

                // Show success message
                ErrorHandler.showSuccess("Success", "Room '" + room.getName() + "' has been removed successfully.");
            } catch (Exception e) {
                System.err.println("Error removing room: " + e.getMessage());
                ErrorHandler.showError("Error", "Failed to remove room: " + e.getMessage());
            }
        }
    }

    /**
     * Refreshes the room display with current data.
     */
    public void refresh() {
        System.out.println("Refreshing home view");
        loadRooms();
    }
}
