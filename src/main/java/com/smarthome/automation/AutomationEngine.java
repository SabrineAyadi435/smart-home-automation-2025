package com.smarthome.automation;

import com.smarthome.controller.HomeController;
import com.smarthome.model.AdhanService;
import java.util.ArrayList;
import java.util.List;

public class AutomationEngine {
    private List<Rule> rules;
    private HomeController homeController;
    private AdhanService adhanService;
    private boolean wasAdhanTime = false;
    
    public AutomationEngine() {
        this.rules = new ArrayList<>();
    }
    
    public AutomationEngine(HomeController homeController) {
        this.rules = new ArrayList<>();
        this.homeController = homeController;
        this.adhanService = new AdhanService("DemoCity", "DemoCountry");
    }
    
    public AutomationEngine(HomeController homeController, String city, String country) {
        this.rules = new ArrayList<>();
        this.homeController = homeController;
        this.adhanService = new AdhanService(city, country);
    }
    
    public void checkAdhanAndMuteTVs() {
        if (adhanService == null || homeController == null) {
            return;
        }
        
        boolean isAdhanTime = adhanService.isAdhanTime();
        
        if (isAdhanTime && !wasAdhanTime) {
            System.out.println("\n[INFO] ADHAN STARTED! Muting TVs and playing Adhan.");
            homeController.muteAllTVs();
            homeController.playAdhanOnAllSpeakers();
            wasAdhanTime = true;
            
        } else if (!isAdhanTime && wasAdhanTime) {
            System.out.println("\n[INFO] ADHAN FINISHED! Unmuting TVs and stopping Adhan.");
            homeController.unmuteAllTVs();
            homeController.stopAdhanOnAllSpeakers();
            wasAdhanTime = false;
        }
    }
    
    public void startAdhanMonitoring() {
        if (adhanService == null || homeController == null) {
            System.err.println("[ERROR] Cannot start Adhan monitoring - HomeController not initialized");
            return;
        }
        
        Thread monitoringThread = new Thread(() -> {
            int checkCount = 0;
            while (true) {
                try {
                    checkAdhanAndMuteTVs();
                    Thread.sleep(1000);
                    checkCount++;

                    // Print a status message only every 30 seconds
                    if (checkCount % 30 == 0) {
                        System.out.println("[INFO] System monitoring... (Current Time: " + adhanService.getCurrentSimulatedTime() + ")");
                    }
                } catch (InterruptedException e) {
                    System.err.println("[ERROR] Adhan monitoring interrupted");
                    break;
                }
            }
        });
        monitoringThread.setDaemon(true);
        monitoringThread.start();
        System.out.println("[SUCCESS] Adhan monitoring started with simulated time");
    }
    
    public AdhanService getAdhanService() {
        return adhanService;
    }
    
    // Demo control methods for GUI integration
    public void demoFastForwardToNextPrayer() {
        if (adhanService != null) {
            adhanService.fastForwardToNextPrayer();
        }
    }
    
    public void demoSetTime(int hours, int minutes) {
        if (adhanService != null) {
            adhanService.setSimulatedTime(java.time.LocalTime.of(hours, minutes));
        }
    }
    
    public void demoEnableTimeAcceleration(int factor) {
        if (adhanService != null) {
            adhanService.enableTimeAcceleration(factor);
        }
    }
    
    public String demoGetCurrentTime() {
        return adhanService != null ? adhanService.getCurrentSimulatedTime() : "N/A";
    }
    
    public String demoGetNextPrayer() {
        return adhanService != null ? adhanService.getNextPrayer() : "Adhan service not available";
    }
    
    // Your existing rule methods
    public void addRule(Rule rule) {
        rules.add(rule);
        System.out.println("Automation rule added: " + rule.getName());
    }
    
    public void removeRule(String ruleName) {
        rules.removeIf(r -> r.getName().equals(ruleName));
    }
    
    public void evaluateRules() {
        System.out.println("Evaluating automation rules...");
        for (Rule rule : rules) {
            rule.evaluate();
        }
    }
    
    public List<Rule> getRules() {
        return new ArrayList<>(rules);
    }
    
    // Getters and setters
    public HomeController getHomeController() {
        return homeController;
    }
    
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }
}