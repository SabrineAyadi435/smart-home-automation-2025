package com.ui.components;

import com.ui.models.Notification;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;

/**
 * Toast notification that appears as a temporary overlay
 */
public class ToastNotification extends VBox {

    private static final Duration FADE_IN_DURATION = Duration.millis(300);
    private static final Duration DISPLAY_DURATION = Duration.seconds(8);
    private static final Duration FADE_OUT_DURATION = Duration.millis(300);

    private final Notification notification;
    private final Runnable onClose;

    public ToastNotification(Notification notification, Runnable onClose) {
        this.notification = notification;
        this.onClose = onClose;

        setupUI();
        playAnimation();
    }

    private void setupUI() {
        setSpacing(5);
        setPadding(new Insets(15));
        setMaxWidth(400);
        setMinWidth(300);
        setStyle(notification.getColorStyle() +
                " -fx-border-radius: 8; -fx-background-radius: 8; " +
                " -fx-border-width: 1 0 1 4; " +
                " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(notification.getIcon());
        iconLabel.setStyle("-fx-font-size: 18pt;");

        Label titleLabel = new Label(notification.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(250);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeBtn = new Button("×");
        closeBtn.setStyle("-fx-font-size: 18pt; -fx-background-color: transparent; " +
                "-fx-text-fill: #666; -fx-padding: 0 5; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> close());

        header.getChildren().addAll(iconLabel, titleLabel, spacer, closeBtn);

        // Message
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-size: 11pt; -fx-text-fill: #333;");
        messageLabel.setMaxWidth(370);
        messageLabel.setMaxHeight(150); // Limit height

        // Time
        Label timeLabel = new Label(notification.getFormattedTime());
        timeLabel.setStyle("-fx-font-size: 9pt; -fx-text-fill: #999;");

        getChildren().addAll(header, messageLabel, timeLabel);

        // Make it clickable to close
        setOnMouseClicked(e -> close());
        setStyle(getStyle() + " -fx-cursor: hand;");
    }

    private void playAnimation() {
        // Fade in
        setOpacity(0);
        setTranslateX(50);

        FadeTransition fadeIn = new FadeTransition(FADE_IN_DURATION, this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideIn = new TranslateTransition(FADE_IN_DURATION, this);
        slideIn.setFromX(50);
        slideIn.setToX(0);

        ParallelTransition showTransition = new ParallelTransition(fadeIn, slideIn);

        // Auto-dismiss after display duration
        PauseTransition pause = new PauseTransition(DISPLAY_DURATION);
        pause.setOnFinished(e -> close());

        SequentialTransition sequence = new SequentialTransition(showTransition, pause);
        sequence.play();
    }

    private void close() {
        FadeTransition fadeOut = new FadeTransition(FADE_OUT_DURATION, this);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        TranslateTransition slideOut = new TranslateTransition(FADE_OUT_DURATION, this);
        slideOut.setFromX(0);
        slideOut.setToX(50);

        ParallelTransition hideTransition = new ParallelTransition(fadeOut, slideOut);
        hideTransition.setOnFinished(e -> {
            if (onClose != null) {
                onClose.run();
            }
        });

        hideTransition.play();
    }
}
