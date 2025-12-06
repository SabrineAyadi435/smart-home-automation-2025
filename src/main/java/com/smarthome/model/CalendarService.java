package com.smarthome.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * Service to handle calendar reminders for a DEMO.
 * This version uses a deterministic "Demo Week" schedule.
 */
public class CalendarService {
    private String userEmail;
    private Map<DayOfWeek, List<String>> demoWeekSchedule;

    public CalendarService(String userEmail) {
        this.userEmail = userEmail;
        this.demoWeekSchedule = createDemoWeekSchedule();
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Demo Calendar Service initialized for: " + userEmail);
    }
    
    /**
     * Creates a personalized and recurring weekly schedule based on your specific requests.
     * @return A map where the key is the day of the week and the value is the list of reminders.
     */
    // --- THIS ENTIRE METHOD HAS BEEN REPLACED WITH YOUR CUSTOM SCHEDULE ---
    private Map<DayOfWeek, List<String>> createDemoWeekSchedule() {
        Map<DayOfWeek, List<String>> schedule = new HashMap<>();
        
        // --- MONDAY: Weekly Planning ---
        schedule.put(DayOfWeek.MONDAY, Arrays.asList(
            "9:00 AM: Meet with team to schedule the work of the week",
            "2:00 PM: Meet with client",
            "7:00 PM: Gym time",
            "8:30 PM: Reminder: 15min tadabor Quran"
        ));
        
        // --- TUESDAY: Team, Work & Piano ---
        schedule.put(DayOfWeek.TUESDAY, Arrays.asList(
            "12:00 PM: Lunch time with team",
            "3:00 PM: Focused work session",
            "6:00 PM: Piano course",
            "7:00 PM: Relax with family",
            "8:30 PM: Reminder: 15min tadabor Quran"
        ));
        
        // --- WEDNESDAY: Personal Care ---
        schedule.put(DayOfWeek.WEDNESDAY, Arrays.asList(
            "7:00 AM: Gym time",
            "9:00 AM: Work / University",
            "1:00 PM: Lunch Break",
            "8:00 PM: Watch favorite TV show",
            "8:30 PM: Reminder: 15min tadabor Quran"
        ));
        
        // --- THURSDAY: Work, Worship & Piano ---
        schedule.put(DayOfWeek.THURSDAY, Arrays.asList(
            "10:00 AM: Work meeting",
            "2:00 PM: Project work",
            "6:00 PM: Piano course",
            "9:00 PM: Reminder: Read Surah Al-Kahf"
        ));
        
        // --- FRIDAY: Jumu'ah and Family ---
        schedule.put(DayOfWeek.FRIDAY, Arrays.asList(
            "11:00 AM: Prepare for Jumu'ah",
            "1:00 PM: Jumu'ah Prayer at the Mosque",
            "7:00 PM: Family Gathering",
            "9:30 PM: Reminder: Read Surah Al-Kahf if not already read"
        ));
        
        // --- SATURDAY: Chores, Douha & Hobbies ---
        schedule.put(DayOfWeek.SATURDAY, Arrays.asList(
            "10:00 AM: Personal Project / Hobby Time",
            "11:30 AM: Douha prayer time",
            "1:00 PM: Lunch Out",
            "3:00 PM: Clean the house",
            "8:00 PM: Relax",
            "8:30 PM: Reminder: 15min tadabor Quran"
        ));
        
        // --- SUNDAY: Rest, Douha & Preparation ---
        schedule.put(DayOfWeek.SUNDAY, Arrays.asList(
            "10:00 AM: Free time",
            "11:30 AM: Douha prayer time",
            "12:00 PM: Meal Prep for the Week",
            "5:00 PM: Plan the upcoming week's schedule",
            "8:30 PM: Reminder: 15min tadabor Quran"
        ));
        
        return schedule;
    }

    /**
     * Get today's reminders based on the DEMO WEEK schedule.
     * @return A list of strings representing today's reminders.
     */
    public List<String> getTodaysReminders() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        List<String> todaysReminders = demoWeekSchedule.getOrDefault(today, Collections.emptyList());
        
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Fetching DEMO reminders for: " + userEmail + " (" + today + ")");
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Found " + todaysReminders.size() + " reminders for today");
        
        return todaysReminders;
    }
    
    /**
     * Get the next reminder from today's schedule.
     */
    public String getNextUpcomingReminder() {
        List<String> todaysReminders = getTodaysReminders();
        if (todaysReminders.isEmpty()) {
            return "No more reminders for today.";
        }
        // For the demo, just return the first reminder.
        // A more advanced version could check the actual time.
        return "Next: " + todaysReminders.get(0);
    }
    
    /**
     * Get the calendar service status.
     */
    public String getServiceStatus() {
        // CHANGED: Replaced special character with a hyphen
        return "- Calendar Service Active (Demo Week Mode) - Connected to: " + userEmail;
    }
    
    /**
     * Simulate adding a new reminder.
     */
    public boolean addReminder(String reminder) {
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Reminder added: " + reminder);
        return true;
    }
}