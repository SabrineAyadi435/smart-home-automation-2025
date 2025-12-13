package com.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.util.Duration;
import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton class that manages simulated time for the entire smart home system.
 * Allows controlling the flow of time (pause, play, speed up, slow down).
 */
public class TimeSimulator {
    private static TimeSimulator instance;

    private final ObjectProperty<LocalDateTime> simulatedTime;
    private final DoubleProperty speedMultiplier;
    private final BooleanProperty paused;
    private Timeline timeline;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TimeSimulator() {
        this.simulatedTime = new SimpleObjectProperty<>(LocalDateTime.now());
        this.speedMultiplier = new SimpleDoubleProperty(1.0);
        this.paused = new SimpleBooleanProperty(false);

        initializeTimeline();
        System.out.println("[TimeSimulator] Initialized at: " + simulatedTime.get());
    }

    public static TimeSimulator getInstance() {
        if (instance == null) {
            instance = new TimeSimulator();
        }
        return instance;
    }

    private final List<java.util.function.Consumer<Double>> tickListeners = new java.util.ArrayList<>();

    /**
     * Initialize the timeline that updates simulated time
     */
    private void initializeTimeline() {
        // Update every 100ms
        timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            if (!paused.get()) {
                // Advance simulated time by (100ms * speed multiplier)
                double realSecondsPassed = 0.1;
                double simulatedSecondsPassed = realSecondsPassed * speedMultiplier.get();

                long nanosToAdd = (long) (simulatedSecondsPassed * 1_000_000_000);
                simulatedTime.set(simulatedTime.get().plusNanos(nanosToAdd));

                // Notify listeners with simulated seconds elapsed
                notifyTickListeners(simulatedSecondsPassed);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void addTickListener(java.util.function.Consumer<Double> listener) {
        tickListeners.add(listener);
    }

    private void notifyTickListeners(double simulatedSecondsElapsed) {
        for (java.util.function.Consumer<Double> listener : tickListeners) {
            try {
                listener.accept(simulatedSecondsElapsed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the current simulated time
     */
    public LocalDateTime now() {
        return simulatedTime.get();
    }

    /**
     * Get the simulated time property (for binding)
     */
    public ObjectProperty<LocalDateTime> simulatedTimeProperty() {
        return simulatedTime;
    }

    /**
     * Set the simulated time to a specific value
     */
    public void setTime(LocalDateTime time) {
        simulatedTime.set(time);
        System.out.println("[TimeSimulator] Time set to: " + time.format(DATETIME_FORMATTER));
    }

    /**
     * Set the speed multiplier (0.1x to 100x)
     */
    public void setSpeed(double multiplier) {
        if (multiplier < 0.1 || multiplier > 100) {
            throw new IllegalArgumentException("Speed must be between 0.1 and 100");
        }
        speedMultiplier.set(multiplier);
        System.out.println("[TimeSimulator] Speed set to: " + multiplier + "x");
    }

    /**
     * Get the current speed multiplier
     */
    public double getSpeed() {
        return speedMultiplier.get();
    }

    /**
     * Get the speed property (for binding)
     */
    public DoubleProperty speedProperty() {
        return speedMultiplier;
    }

    /**
     * Pause time simulation
     */
    public void pause() {
        paused.set(true);
        System.out.println("[TimeSimulator] PAUSED at: " + simulatedTime.get().format(DATETIME_FORMATTER));
    }

    /**
     * Resume time simulation
     */
    public void play() {
        paused.set(false);
        System.out.println("[TimeSimulator] PLAYING at " + speedMultiplier.get() + "x speed");
    }

    /**
     * Toggle pause/play
     */
    public void togglePause() {
        if (paused.get()) {
            play();
        } else {
            pause();
        }
    }

    /**
     * Check if time is paused
     */
    public boolean isPaused() {
        return paused.get();
    }

    /**
     * Get the paused property (for binding)
     */
    public BooleanProperty pausedProperty() {
        return paused;
    }

    /**
     * Fast forward by specified hours
     */
    public void fastForward(int hours) {
        simulatedTime.set(simulatedTime.get().plusHours(hours));
        System.out.println("[TimeSimulator] Fast-forwarded " + hours + " hours to: " +
                simulatedTime.get().format(DATETIME_FORMATTER));
    }

    /**
     * Rewind by specified hours
     */
    public void rewind(int hours) {
        simulatedTime.set(simulatedTime.get().minusHours(hours));
        System.out.println("[TimeSimulator] Rewound " + hours + " hours to: " +
                simulatedTime.get().format(DATETIME_FORMATTER));
    }

    /**
     * Get formatted time string (HH:mm:ss)
     */
    public String getFormattedTime() {
        return simulatedTime.get().format(TIME_FORMATTER);
    }

    /**
     * Get formatted date string (yyyy-MM-dd)
     */
    public String getFormattedDate() {
        return simulatedTime.get().format(DATE_FORMATTER);
    }

    /**
     * Get formatted datetime string
     */
    public String getFormattedDateTime() {
        return simulatedTime.get().format(DATETIME_FORMATTER);
    }

    /**
     * Stop the timeline (cleanup)
     */
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
        System.out.println("[TimeSimulator] Stopped");
    }
}
