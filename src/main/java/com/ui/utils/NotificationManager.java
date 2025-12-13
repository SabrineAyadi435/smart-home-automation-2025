package com.ui.utils;

import com.ui.models.Notification;
import com.ui.models.NotificationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Singleton manager for dashboard notifications
 */
public class NotificationManager {
    private static NotificationManager instance;
    private ObservableList<Notification> notifications;
    private static final int MAX_NOTIFICATIONS = 20;

    private NotificationManager() {
        notifications = FXCollections.observableArrayList();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    /**
     * Adds a new notification to the list
     */
    public void addNotification(String title, String message, NotificationType type) {
        Notification notification = new Notification(title, message, type);

        // Add to the beginning of the list (newest first)
        notifications.add(0, notification);

        // Remove old notifications if exceeding max
        while (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.remove(notifications.size() - 1);
        }

        System.out.println("[NOTIFICATION] " + type + ": " + title + " - " + message);
    }

    /**
     * Removes a specific notification
     */
    public void removeNotification(Notification notification) {
        notifications.remove(notification);
    }

    /**
     * Clears all notifications
     */
    public void clearAll() {
        notifications.clear();
        System.out.println("[NOTIFICATION] All notifications cleared");
    }

    /**
     * Gets the observable list of notifications
     */
    public ObservableList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Gets the count of notifications
     */
    public int getCount() {
        return notifications.size();
    }
}
