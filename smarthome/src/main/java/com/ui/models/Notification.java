package com.ui.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a notification in the dashboard notification panel
 */
public class Notification {
    private String title;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;
    private String icon;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Notification(String title, String message, NotificationType type) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.icon = getIconForType(type);
    }

    private String getIconForType(NotificationType type) {
        switch (type) {
            case INFO:
                return "ℹ️";
            case SUCCESS:
                return "✅";
            case WARNING:
                return "⚠️";
            case ALERT:
                return "🚨";
            case MIRROR:
                return "📖";
            default:
                return "📢";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTime() {
        return timestamp.format(TIME_FORMATTER);
    }

    public String getIcon() {
        return icon;
    }

    public String getColorStyle() {
        switch (type) {
            case INFO:
                return "-fx-background-color: #E3F2FD; -fx-border-color: #2196F3;";
            case SUCCESS:
                return "-fx-background-color: #E8F5E9; -fx-border-color: #4CAF50;";
            case WARNING:
                return "-fx-background-color: #FFF3E0; -fx-border-color: #FF9800;";
            case ALERT:
                return "-fx-background-color: #FFEBEE; -fx-border-color: #F44336;";
            case MIRROR:
                return "-fx-background-color: #F3E5F5; -fx-border-color: #9C27B0;";
            default:
                return "-fx-background-color: #F5F5F5; -fx-border-color: #9E9E9E;";
        }
    }
}
