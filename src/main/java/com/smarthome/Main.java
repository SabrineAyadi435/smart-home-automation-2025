package com.smarthome;

import com.smarthome.model.*;
import com.smarthome.controller.HomeController;
import com.smarthome.automation.ClimateRule;
import java.util.Scanner;
import java.util.List;
import java.time.LocalTime;

public class Main {
    private static HomeController homeController;
    
    public static void main(String[] args) {
        // Create a home instance
        Home home = new Home("My Islamic Smart Home");
        
        // Initialize controller with all services
        homeController = new HomeController(home);
        
        // Initialize demo climate setup
        homeController.initializeDemoClimate();
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== ISLAMIC SMART HOME CLIMATE SYSTEM ===");
            System.out.println("1. Climate Control");
            System.out.println("2. Islamic Features");
            System.out.println("3. Automation Rules");
            System.out.println("4. System Status");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    climateControlMenu(scanner);
                    break;
                case 2:
                    islamicFeaturesMenu(scanner);
                    break;
                case 3:
                    automationMenu(scanner);
                    break;
                case 4:
                    systemStatusMenu();
                    break;
                case 5:
                    System.out.println("Goodbye! Assalamu Alaikum 🌙");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void climateControlMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== CLIMATE CONTROL ===");
            System.out.println("1. View Climate Status");
            System.out.println("2. Set Room Temperature");
            System.out.println("3. Set Whole Home Temperature");
            System.out.println("4. Optimize for Comfort");
            System.out.println("5. Optimize for Energy Savings");
            System.out.println("6. Check Air Quality");
            System.out.println("7. Set Home/Away Modes");
            System.out.println("8. Apply Schedules");
            System.out.println("9. Energy Report");
            System.out.println("10. Back to Main Menu");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.println(homeController.getClimateSummary());
                    break;
                case 2:
                    setRoomTemperature(scanner);
                    break;
                case 3:
                    setWholeHomeTemperature(scanner);
                    break;
                case 4:
                    homeController.optimizeForComfort();
                    break;
                case 5:
                    homeController.optimizeForEnergySavings();
                    break;
                case 6:
                    homeController.checkAirQuality();
                    break;
                case 7:
                    setHomeAwayMode(scanner);
                    break;
                case 8:
                    applySchedule(scanner);
                    break;
                case 9:
                    homeController.generateEnergyReport();
                    break;
                case 10:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void islamicFeaturesMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== ISLAMIC FEATURES ===");
            System.out.println("1. Prayer Time Automation");
            System.out.println("2. Ramadan Mode");
            System.out.println("3. Wellness Features");
            System.out.println("4. Islamic Status");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    prayerTimeMenu(scanner);
                    break;
                case 2:
                    ramadanModeMenu(scanner);
                    break;
                case 3:
                    wellnessMenu(scanner);
                    break;
                case 4:
                    displayIslamicStatus();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void automationMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== AUTOMATION RULES ===");
            System.out.println("1. View All Rules");
            System.out.println("2. Execute Rules Now");
            System.out.println("3. Create Custom Rule");
            System.out.println("4. Create Occupancy Rule");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewAllRules();
                    break;
                case 2:
                    homeController.executeClimateRules();
                    break;
                case 3:
                    createCustomRule(scanner);
                    break;
                case 4:
                    createOccupancyRule(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void systemStatusMenu() {
        System.out.println("\n=== SYSTEM STATUS ===");
        System.out.println(homeController.getSystemStatus());
        
        // Show rooms with climate devices
        List<Room> climateRooms = homeController.getRoomsWithClimateDevices();
        System.out.println("\n--- Rooms with Climate Devices ---");
        if (climateRooms.isEmpty()) {
            System.out.println("No rooms with climate devices found.");
        } else {
            climateRooms.forEach(room -> {
                System.out.println(room.getName() + " - " + room.getClimateStatus());
            });
        }
    }
    
    // HELPER METHODS
    private static void setRoomTemperature(Scanner scanner) {
        System.out.print("Enter room name: ");
        String roomName = scanner.nextLine();
        System.out.print("Enter temperature (°C): ");
        double temperature = scanner.nextDouble();
        scanner.nextLine();
        homeController.setRoomTemperature(roomName, temperature);
    }
    
    private static void setWholeHomeTemperature(Scanner scanner) {
        System.out.print("Enter temperature for whole home (°C): ");
        double temperature = scanner.nextDouble();
        scanner.nextLine();
        homeController.setWholeHomeTemperature(temperature);
    }
    
    private static void setHomeAwayMode(Scanner scanner) {
        System.out.println("1. Home Mode (Comfort)");
        System.out.println("2. Away Mode (Energy Saving)");
        System.out.print("Choose mode: ");
        int modeChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (modeChoice == 1) {
            homeController.setHomeMode();
        } else if (modeChoice == 2) {
            homeController.setAwayMode();
        } else {
            System.out.println("Invalid choice!");
        }
    }
    
    private static void applySchedule(Scanner scanner) {
        System.out.println("1. Apply Day Schedule");
        System.out.println("2. Apply Night Schedule");
        System.out.print("Choose schedule: ");
        int scheduleChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (scheduleChoice == 1) {
            homeController.getClimateService().applyDaySchedule();
        } else if (scheduleChoice == 2) {
            homeController.getClimateService().applyNightSchedule();
        } else {
            System.out.println("Invalid choice!");
        }
    }
    
    private static void prayerTimeMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== PRAYER TIME AUTOMATION ===");
            System.out.println("1. Enable/Disable Automation");
            System.out.println("2. Set Prayer Times");
            System.out.println("3. Prepare for Specific Prayer");
            System.out.println("4. Auto-Prepare for Next Prayer");
            System.out.println("5. Set Prayer Room");
            System.out.println("6. Back to Islamic Menu");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enable prayer time automation? (y/n): ");
                    String enable = scanner.nextLine();
                    homeController.enablePrayerTimeAutomation(enable.equalsIgnoreCase("y"));
                    break;
                case 2:
                    setPrayerTimes(scanner);
                    break;
                case 3:
                    prepareForSpecificPrayer(scanner);
                    break;
                case 4:
                    homeController.autoPrepareForNextPrayer();
                    break;
                case 5:
                    setPrayerRoom(scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void ramadanModeMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== RAMADAN MODE ===");
            System.out.println("1. Enable Ramadan Mode");
            System.out.println("2. Disable Ramadan Mode");
            System.out.println("3. Prepare for Iftar");
            System.out.println("4. Prepare for Taraweeh");
            System.out.println("5. Prepare for Suhoor");
            System.out.println("6. Ramadan Energy Report");
            System.out.println("7. Back to Islamic Menu");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    homeController.enableRamadanMode();
                    break;
                case 2:
                    homeController.disableRamadanMode();
                    break;
                case 3:
                    homeController.prepareForIftar();
                    break;
                case 4:
                    homeController.prepareForTaraweeh();
                    break;
                case 5:
                    homeController.prepareForSuhoor();
                    break;
                case 6:
                    homeController.generateRamadanEnergyReport();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void wellnessMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== ISLAMIC WELLNESS ===");
            System.out.println("1. Prepare for Wudu");
            System.out.println("2. Prepare for Tahajjud");
            System.out.println("3. Optimize for Fasting Comfort");
            System.out.println("4. Quran Recitation Mode");
            System.out.println("5. Family Gathering Mode");
            System.out.println("6. Elderly Comfort Mode");
            System.out.println("7. Back to Islamic Menu");
            System.out.print("Choose option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    homeController.prepareForWudu();
                    break;
                case 2:
                    homeController.prepareForTahajjud();
                    break;
                case 3:
                    homeController.optimizeForFastingComfort();
                    break;
                case 4:
                    homeController.enableQuranRecitationMode();
                    break;
                case 5:
                    homeController.setFamilyGatheringMode();
                    break;
                case 6:
                    homeController.optimizeForElderlyComfort();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    private static void setPrayerTimes(Scanner scanner) {
        System.out.println("Set Prayer Times (HH:MM format):");
        String[] prayers = {"FAJR", "DHUHR", "ASR", "MAGHRIB", "ISHA"};
        
        for (String prayer : prayers) {
            System.out.print(prayer + " time: ");
            String timeStr = scanner.nextLine();
            try {
                LocalTime time = LocalTime.parse(timeStr);
                homeController.getIslamicClimateService().setPrayerTime(prayer, time);
            } catch (Exception e) {
                System.out.println("Invalid time format for " + prayer);
            }
        }
    }
    
    private static void prepareForSpecificPrayer(Scanner scanner) {
        System.out.println("Select prayer to prepare for:");
        System.out.println("1. Fajr");
        System.out.println("2. Dhuhr");
        System.out.println("3. Asr");
        System.out.println("4. Maghrib");
        System.out.println("5. Isha");
        System.out.print("Choose: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        String[] prayers = {"FAJR", "DHUHR", "ASR", "MAGHRIB", "ISHA"};
        if (choice >= 1 && choice <= 5) {
            homeController.prepareForPrayer(prayers[choice - 1]);
        } else {
            System.out.println("Invalid choice!");
        }
    }
    
    private static void setPrayerRoom(Scanner scanner) {
        System.out.print("Enter room name to designate as prayer room: ");
        String roomName = scanner.nextLine();
        homeController.setPrayerRoom(roomName);
    }
    
    private static void displayIslamicStatus() {
        System.out.println(homeController.getIslamicClimateService().getIslamicClimateStatus());
        System.out.println("\n" + homeController.getIslamicWellnessService().getWellnessStatus());
        
        if (homeController.getRamadanModeService().isRamadanModeActive()) {
            System.out.println("\n" + homeController.getRamadanModeService().getRamadanStatus());
        }
    }
    
    private static void viewAllRules() {
        System.out.println("\n=== ALL AUTOMATION RULES ===");
        List<ClimateRule> rules = homeController.getClimateRules();
        if (rules.isEmpty()) {
            System.out.println("No automation rules defined.");
        } else {
            for (int i = 0; i < rules.size(); i++) {
                ClimateRule rule = rules.get(i);
                System.out.println((i + 1) + ". " + rule.getName());
                System.out.println("   Description: " + rule.getRuleDescription());
                System.out.println("   Target: " + rule.getTargetDevice());
                System.out.println();
            }
        }
    }
    
    private static void createCustomRule(Scanner scanner) {
        System.out.println("\n=== CREATE CUSTOM AUTOMATION RULE ===");
        
        System.out.print("Rule name: ");
        String name = scanner.nextLine();
        
        System.out.println("Condition types:");
        System.out.println("1. Temperature Above");
        System.out.println("2. Temperature Below");
        System.out.println("3. Air Quality Poor");
        System.out.println("4. High Humidity");
        System.out.println("5. High CO2");
        System.out.print("Choose condition (1-5): ");
        int conditionChoice = scanner.nextInt();
        
        System.out.print("Threshold value: ");
        double threshold = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.println("Action types:");
        System.out.println("1. Set Temperature");
        System.out.println("2. Set Mode");
        System.out.println("3. Enable Eco Mode");
        System.out.println("4. Send Alert");
        System.out.println("5. Apply Schedule");
        System.out.print("Choose action (1-5): ");
        int actionChoice = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Target value: ");
        String targetValue = scanner.nextLine();
        
        System.out.print("Target device/room (or 'all' for all devices): ");
        String targetDevice = scanner.nextLine();
        
        ClimateRule.ClimateCondition condition = getConditionFromChoice(conditionChoice);
        ClimateRule.ClimateAction action = getActionFromChoice(actionChoice);
        
        if (condition != null && action != null) {
            ClimateRule rule = new ClimateRule(name, "Custom rule", condition, action, threshold, targetValue, targetDevice);
            homeController.addClimateRule(rule);
            System.out.println("✅ Custom rule created successfully!");
        } else {
            System.out.println("❌ Failed to create rule. Invalid choices.");
        }
    }
    
    private static void createOccupancyRule(Scanner scanner) {
        System.out.print("Enter room name for occupancy rule: ");
        String roomName = scanner.nextLine();
        System.out.print("Enter empty minutes threshold: ");
        int threshold = scanner.nextInt();
        scanner.nextLine();
        
        homeController.getClimateService().createOccupancyRule(roomName, threshold);
    }
    
    private static ClimateRule.ClimateCondition getConditionFromChoice(int choice) {
        switch (choice) {
            case 1: return ClimateRule.ClimateCondition.TEMPERATURE_ABOVE;
            case 2: return ClimateRule.ClimateCondition.TEMPERATURE_BELOW;
            case 3: return ClimateRule.ClimateCondition.AIR_QUALITY_POOR;
            case 4: return ClimateRule.ClimateCondition.HIGH_HUMIDITY;
            case 5: return ClimateRule.ClimateCondition.HIGH_CO2;
            default: return null;
        }
    }
    
    private static ClimateRule.ClimateAction getActionFromChoice(int choice) {
        switch (choice) {
            case 1: return ClimateRule.ClimateAction.SET_TEMPERATURE;
            case 2: return ClimateRule.ClimateAction.SET_MODE;
            case 3: return ClimateRule.ClimateAction.ENABLE_ECO_MODE;
            case 4: return ClimateRule.ClimateAction.SEND_ALERT;
            case 5: return ClimateRule.ClimateAction.APPLY_SCHEDULE;
            default: return null;
        }
    }
}