# UI Architecture

This document details the User Interface (UI) architecture of the Smart Home Automation application, built using JavaFX.

## Overview
The application follows the Model-View-Controller (MVC) pattern, separating the presentation layer (FXML) from the business logic (Controllers).

## Controllers Package (`com.ui.controllers`)
Controllers handle user interactions and update the view.

- **DashboardController**: The main controller for the application shell. It manages navigation between different panels (Home, Security, Climate, etc.).
- **HomeViewController**: Manages the main home overview, likely displaying a summary of the house status.
- **ClimateController**: Controls the thermostat and air conditioning settings.
- **SecurityPanelController**: Manages the security system interface (cameras, locks, alarms).
- **MonitoringController**: Displays energy and resource usage statistics.
- **RoomPanelController**: Handles the detailed view and control of individual rooms.
- **DeviceControlFactory**: A factory class (or helper) to generate UI controls for different device types dynamically.

## Components Package (`com.ui.components`)
Custom reusable UI components.

- **RoomCard**: A visual card representing a room, likely showing a summary of devices in that room.
- **MonitoringWidget**: A widget for displaying real-time data (e.g., energy graph).
- **NotificationPanel**: Displays system notifications and alerts.
- **ToastNotification**: A transient notification popup.

## Views (FXML)
The UI layout is defined in FXML files located in `src/main/resources/com/ui/fxml`.

- `dashboard.fxml`: The main layout container.
- `home-view.fxml`: The home overview screen.
- `climate-control.fxml`: The climate control interface.
- `security-panel.fxml`: The security dashboard.
- `monitoring-panel.fxml`: The energy monitoring screen.
- `room-panel.fxml`: The individual room control screen.
- `notification-panel.fxml`: The notification center.

## Styling
The application uses CSS for styling, located in `src/main/resources/com/ui/css`. The main stylesheet is likely `main.css`.
