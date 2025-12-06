package com.smarthome.model;

import com.smarthome.interfaces.Controllable;
import com.smarthome.interfaces.EnergyConsumer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SmartMirror extends SmartDevice implements Controllable, EnergyConsumer {
    private boolean isMirrorMode;
    private String currentArabicVerse;
    private String currentTranslation;
    private String currentSurahInfo;
    // --- SIMPLIFICATION: These lists now hold data synced from the service ---
    private List<String> reminders;
    private String weather;
    private double temperature;
    private final String qiblaDirection;
    private final String city;

    private CalendarService calendarService;

    private static final double YOUR_LATITUDE = 36.8065;
    private static final double YOUR_LONGITUDE = 10.1815;
    private static final String YOUR_CITY = "Tunis";

    public SmartMirror(String deviceId, String name) {
        super(deviceId, name);
        this.isOn = true; // Ensure mirror stays on by default
        this.isMirrorMode = false; // Start in display mode to show information
        this.reminders = new ArrayList<>(); // This list will be populated by the service
        this.weather = "Sunny";
        this.temperature = 22.0;
        this.city = YOUR_CITY;

        // --- SIMPLIFICATION: Initialize with a default email, which will be changed later ---
        this.calendarService = new CalendarService("user@example.com");

        QiblaService.QiblaDirection qibla = QiblaService.getQiblaDirection(YOUR_LATITUDE, YOUR_LONGITUDE);
        this.qiblaDirection = String.format("Qibla Direction: %s (%.1f°)", qibla.direction, qibla.degrees);

        System.out.println("[INFO] Smart Mirror configured for: " + YOUR_CITY);
        System.out.println("[INFO] Coordinates: " + YOUR_LATITUDE + ", " + YOUR_LONGITUDE);
        System.out.println("[INFO] " + qiblaDirection);

        updateDailyVerse();
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        Thread refreshThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3600000); // Refresh every hour
                    updateDailyVerse();
                    System.out.println("[INFO] Quran verse auto-refreshed (hourly update)");
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        refreshThread.setDaemon(true);
        refreshThread.start();
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        System.out.println("Smart Mirror " + getName() + " turned ON");
    }

    @Override
    public void turnOff() {
        this.isOn = false;
        System.out.println("Smart Mirror " + getName() + " turned OFF");
    }

    @Override
    public String getStatus() {
        return isOn ?
            String.format("ON (%s - %s, %.1f°C)",
                isMirrorMode ? "Mirror Mode" : "Display Mode", weather, temperature) :
            "OFF";
    }

    public void toggleMode() {
        this.isMirrorMode = !this.isMirrorMode;
        System.out.println("Smart Mirror switched to " +
            (isMirrorMode ? "Mirror Mode" : "Information Display Mode"));
    }

    public void displayBriefing() {
        if (!isOn || isMirrorMode) {
            System.out.println("[ERROR] Cannot display briefing: Mirror is " + 
                (!isOn ? "OFF" : "in Mirror Mode"));
            return;
        }

        IslamicCalendarService.IslamicDate islamicDate = IslamicCalendarService.getTodayIslamicDate();

        // --- SIMPLIFICATION: Sync all reminders from the service ---
        syncReminders();

        printSeparator("=");
        System.out.println(" SMART MIRROR BRIEFING");
        printSeparator("=");

        System.out.println("[TIME] " + getCurrentTime() + " | [WEATHER] " + weather + " (" + temperature + "C)");
        System.out.println("[QIBLA] " + qiblaDirection);
        System.out.println("[ISLAMIC DATE] " + islamicDate.date);
        if (islamicDate.event != null) {
            System.out.println(" [EVENT] " + islamicDate.event);
        }

        // FIX 1: Ensure Quran verse is displayed in English only
        System.out.println("\n[QURAN VERSE]");
        if (currentTranslation != null && !currentTranslation.isEmpty()) {
            System.out.println(" \"" + currentTranslation + "\"");
            System.out.println(" (" + currentSurahInfo + ")");
        } else {
            System.out.println(" No verse available. Try refreshing with REFRESH_VERSE command.");
        }

        // FIX 2: Ensure reminders are displayed
        if (!reminders.isEmpty()) {
            System.out.println("\n[REMINDERS] Today's Schedule:");
            // BUG FIX: This loop was missing. We need to add it to print the reminders.
            for (String reminder : reminders) {
                System.out.println(" • " + reminder);
            }
        } else {
            System.out.println("\n[REMINDERS] No reminders for today.");
        }

        printSeparator("=");
    }

    public void displayIslamicCalendar() {
        if (!isOn || isMirrorMode) {
            System.out.println("[ERROR] Cannot display Islamic calendar: Mirror is " + 
                (!isOn ? "OFF" : "in Mirror Mode"));
            return;
        }

        IslamicCalendarService.IslamicDate islamicDate = IslamicCalendarService.getTodayIslamicDate();

        printSeparator("*");
        System.out.println(" ISLAMIC CALENDAR");
        printSeparator("*");
        System.out.println("[DATE] " + islamicDate.date);

        if (islamicDate.event != null) {
            System.out.println("[EVENT] " + islamicDate.event);
        } else {
            System.out.println("[INFO] No Islamic events today");
        }

        System.out.println("[QIBLA] " + qiblaDirection);
        System.out.println("[CITY] " + city);
        printSeparator("*");
    }

    public void displayQuranVerseOnly() {
        if (!isOn || isMirrorMode) {
            System.out.println("[ERROR] Cannot display Quran verse: Mirror is " + 
                (!isOn ? "OFF" : "in Mirror Mode"));
            return;
        }
        
        if (currentTranslation != null && !currentTranslation.isEmpty()) {
            System.out.println("[QURAN] " + currentTranslation + " " + currentSurahInfo);
        } else {
            System.out.println("[QURAN] No verse available. Try refreshing with REFRESH_VERSE command.");
        }
    }

    private String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private void updateDailyVerse() {
        try {
            QuranAPIService.QuranVerse verse = QuranAPIService.getRandomVerse();
            this.currentArabicVerse = verse.arabic;
            this.currentTranslation = verse.translation;
            this.currentSurahInfo = verse.surahInfo;
            System.out.println("[INFO] Quran verse updated successfully");
        } catch (Exception e) {
            System.err.println("[ERROR] Error fetching Quran verse: " + e.getMessage());
            // Set default values in case of error
            this.currentArabicVerse = "Arabic text";
            this.currentTranslation = "In the name of Allah, the Most Gracious, the Most Merciful";
            this.currentSurahInfo = "Al-Fatihah 1:1";
        }
    }

    // --- SIMPLIFICATION: This method now syncs reminders from the service ---
    public void syncReminders() {
        if (calendarService != null) {
            List<String> remindersFromService = calendarService.getTodaysReminders();
            this.reminders.clear();
            this.reminders.addAll(remindersFromService);
            System.out.println("[INFO] Reminders synced: " + remindersFromService.size() + " reminders loaded");
        }
    }

    public void setCalendarEmail(String email) {
        this.calendarService = new CalendarService(email);
        System.out.println("[INFO] Calendar service connected to: " + email);
    }

    public String getCalendarStatus() {
        return calendarService != null ? calendarService.getServiceStatus() : "Calendar Service Not Connected";
    }

    // --- SIMPLIFICATION: Removed local add methods. The mirror now only displays what the service provides. ---
    public void setWeather(String weather, double temperature) {
        this.weather = weather;
        this.temperature = temperature;
        System.out.println("[INFO] Weather updated: " + weather + " | " + temperature + "C");
    }

    public void refreshVerse() {
        updateDailyVerse();
        System.out.println("[INFO] Quran verse refreshed!");
    }

    public boolean isMirrorMode() { return isMirrorMode; }
    public String getQiblaDirection() { return qiblaDirection; }
    public String getCity() { return city; }

    @Override
    public void executeCommand(String command) {
        switch (command.toUpperCase()) {
            case "ON": turnOn(); break;
            case "OFF": turnOff(); break;
            case "TOGGLE": toggleMode(); break;
            case "BRIEFING": displayBriefing(); break;
            case "CALENDAR": displayIslamicCalendar(); break;
            case "QURAN": displayQuranVerseOnly(); break;
            case "REFRESH_VERSE": refreshVerse(); break;
            // --- SIMPLIFICATION: Use a single, clear command for syncing ---
            case "SYNC_CALENDAR":
                syncReminders();
                break;
            default: System.out.println("Unknown command for Smart Mirror: " + command);
        }
    }

    @Override
    public boolean isControllable() {
        return true;
    }

    @Override
    public double getEnergyConsumption() {
        return isOn ? 8.0 : 0.5;
    }

    @Override
    public void setEnergyMode(String mode) {
        System.out.println("Smart Mirror energy mode set to: " + mode);
    }

    // Helper method for consistent separators
    private static void printSeparator(String character) {
        for (int i = 0; i < 50; i++) {
            System.out.print(character);
        }
        System.out.println();
    }
}