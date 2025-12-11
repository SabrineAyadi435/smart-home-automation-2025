# Smart Home Automation 2025

Welcome to the Smart Home Automation 2025 project! This is a comprehensive JavaFX application designed to simulate and manage a modern smart home environment.

## Features

- **Device Management**: Control lights, thermostats, locks, cameras, and more.
- **Automation**: Define rules to automate device actions based on triggers (e.g., motion detection).
- **Energy Monitoring**: Track real-time energy consumption of all connected devices.
- **Security Panel**: Monitor security cameras and manage alarm systems.
- **Room Management**: Organize devices by room for easy access.
- **Interactive Dashboard**: A beautiful and responsive UI built with JavaFX.

## Documentation

Detailed documentation for the project architecture can be found in the `docs` directory:

- [Backend Architecture](docs/BACKEND.md): Details on the device hierarchy, automation engine, and core interfaces.
- [UI Architecture](docs/UI.md): Overview of the JavaFX controllers, FXML views, and custom components.

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher is recommended.
- **JavaFX**: Ensure your JDK includes JavaFX or you have the JavaFX SDK configured.
- **Maven/Gradle**: (Assuming a build tool is used, adjust if necessary).

### Running the Application

1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd smart-home-automation-2025
    ```

2.  **Build the project**:
    ```bash
    # If using Maven
    mvn clean install
    ```

3.  **Run the application**:
    Navigate to the `src/main/java` directory and run the main class.
    ```bash
    # Example command (adjust classpath as needed)
    java com.Main
    # OR
    java com.ui.DashboardApplication
    ```

## Project Structure

- `src/main/java/com/devices`: Smart device implementations.
- `src/main/java/com/automation`: Automation logic.
- `src/main/java/com/ui`: JavaFX controllers and components.
- `src/main/resources/com/ui/fxml`: FXML layout files.
- `src/main/resources/com/ui/css`: Stylesheets.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.
