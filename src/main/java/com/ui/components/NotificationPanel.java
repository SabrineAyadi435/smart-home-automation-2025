package com.ui.components;

import com.ui.models.Notification;
import com.ui.utils.NotificationManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controller for the notification panel component
 */
public class NotificationPanel {

    @FXML
    private ListView<Notification> notificationListView;

    @FXML
    private Label notificationCount;

    @FXML
    private Button clearAllButton;

    private NotificationManager notificationManager;

    @FXML
    public void initialize() {
        System.out.println("NotificationPanel initialized");

        notificationManager = NotificationManager.getInstance();

        // Bind the ListView to the notification list
        notificationListView.setItems(notificationManager.getNotifications());

        // Custom cell factory for notification items
        notificationListView.setCellFactory(listView -> new NotificationCell());

        // Update count when notifications change
        notificationManager.getNotifications()
                .addListener((javafx.collections.ListChangeListener.Change<? extends Notification> c) -> {
                    updateCount();
                });

        // Clear all button action
        clearAllButton.setOnAction(e -> {
            notificationManager.clearAll();
        });

        updateCount();
    }

    private void updateCount() {
        int count = notificationManager.getCount();
        notificationCount.setText("(" + count + ")");
    }

    /**
     * Custom ListCell for displaying notifications
     */
    private class NotificationCell extends ListCell<Notification> {
        @Override
        protected void updateItem(Notification notification, boolean empty) {
            super.updateItem(notification, empty);

            if (empty || notification == null) {
                setGraphic(null);
                setText(null);
                setStyle("");
            } else {
                VBox container = new VBox(5);
                container.setPadding(new Insets(10));
                container.setStyle(notification.getColorStyle()
                        + " -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-width: 1 0 1 3;");

                // Header with icon, title, and time
                HBox header = new HBox(10);
                header.setAlignment(Pos.CENTER_LEFT);

                Label iconLabel = new Label(notification.getIcon());
                iconLabel.setStyle("-fx-font-size: 16pt;");

                Label titleLabel = new Label(notification.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11pt;");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label timeLabel = new Label(notification.getFormattedTime());
                timeLabel.setStyle("-fx-font-size: 9pt; -fx-text-fill: #666;");

                Button dismissButton = new Button("×");
                dismissButton.setStyle(
                        "-fx-font-size: 14pt; -fx-background-color: transparent; -fx-text-fill: #666; -fx-padding: 0 5;");
                dismissButton.setOnAction(e -> {
                    notificationManager.removeNotification(notification);
                });

                header.getChildren().addAll(iconLabel, titleLabel, spacer, timeLabel, dismissButton);

                // Message
                Label messageLabel = new Label(notification.getMessage());
                messageLabel.setWrapText(true);
                messageLabel.setStyle("-fx-font-size: 10pt; -fx-text-fill: #333;");
                messageLabel.setMaxWidth(280);

                container.getChildren().addAll(header, messageLabel);

                setGraphic(container);
                setText(null);
                setStyle("-fx-background-color: transparent; -fx-padding: 5 0;");
            }
        }
    }
}
