package com.smarthome.service;

import com.smarthome.model.Thermostat;
import com.smarthome.model.AirQualitySensor;
import java.time.LocalTime;

public class IslamicWellnessService {
    private ClimateService climateService;
    private IslamicClimateService islamicClimateService;
    
    public IslamicWellnessService(ClimateService climateService, IslamicClimateService islamicClimateService) {
        this.climateService = climateService;
        this.islamicClimateService = islamicClimateService;
    }
    
    public void prepareForWudu() {
        System.out.println("💧 Preparing optimal Wudu environment...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn() && thermostat.getLocation().toLowerCase().contains("bathroom")) {
                thermostat.setTemperature(24.0);
                thermostat.setEcoMode(false);
                System.out.println("🚿 " + thermostat.getName() + " optimized for Wudu comfort");
            }
        });
    }
    
    public void prepareForTahajjud() {
        System.out.println("🌙 Preparing for Tahajjud prayers...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.setTemperature(20.0);
                thermostat.setMode("ECO");
                thermostat.setEcoMode(true);
                System.out.println("🕋 " + thermostat.getName() + " optimized for Tahajjud");
            }
        });
    }
    
    public void optimizeForFastingComfort() {
        System.out.println("🌅 Optimizing for fasting comfort...");
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(12, 0))) {
            climateService.getThermostats().forEach(thermostat -> {
                if (thermostat.isOn()) {
                    thermostat.setEcoMode(true);
                    thermostat.setMode("ECO");
                }
            });
            System.out.println("💤 Energy-saving mode for morning fasting hours");
        } else if (now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(16, 0))) {
            climateService.getThermostats().forEach(thermostat -> {
                if (thermostat.isOn() && thermostat.getLocation().toLowerCase().contains("living")) {
                    thermostat.setTemperature(21.0);
                    thermostat.setEcoMode(false);
                    System.out.println("😴 Enhanced comfort for afternoon fasting fatigue");
                }
            });
        }
    }
    
    public void prepareForIftarRecovery() {
        System.out.println("🩺 Preparing post-Iftar recovery environment...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn() && thermostat.getLocation().toLowerCase().contains("living")) {
                thermostat.setTemperature(22.0);
                thermostat.setMode("AUTO");
                System.out.println("💆 " + thermostat.getName() + " optimized for post-Iftar relaxation");
            }
        });
    }
    
    public void enableQuranRecitationMode() {
        System.out.println("📖 Quran Recitation Mode activated...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.setTemperature(22.0);
                thermostat.setMode("AUTO");
                System.out.println("🕌 " + thermostat.getName() + " optimized for Quran recitation");
            }
        });
    }
    
    public void setDhikrEnvironment() {
        System.out.println("🕋 Creating optimal Dhikr environment...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn()) {
                thermostat.setTemperature(21.0);
                thermostat.setMode("AUTO");
                System.out.println("☁️  " + thermostat.getName() + " set for peaceful Dhikr");
            }
        });
    }
    
    public void optimizeForElderlyComfort() {
        System.out.println("👵 Optimizing for elderly comfort...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn()) {
                double currentTemp = thermostat.getTargetTemperature();
                if (currentTemp < 23.0) {
                    thermostat.setTemperature(23.0);
                }
                thermostat.setMode("AUTO");
                System.out.println("❤️  " + thermostat.getName() + " optimized for elderly comfort");
            }
        });
    }
    
    public void setFamilyGatheringMode() {
        System.out.println("👨‍👩‍👧‍👦 Family Gathering Mode activated...");
        climateService.getThermostats().forEach(thermostat -> {
            if (thermostat.isOn() && thermostat.getLocation().toLowerCase().contains("living")) {
                thermostat.setTemperature(22.0);
                thermostat.setEcoMode(false);
                System.out.println("🏠 " + thermostat.getName() + " optimized for family gatherings");
            }
        });
    }
    
    public String getWellnessStatus() {
        return "Islamic Wellness Service: Active\n" +
               "Features: Wudu Preparation, Tahajjud Support, Fasting Comfort\n" +
               "Spiritual Modes: Quran Recitation, Dhikr Environment\n" +
               "Family Features: Elderly Comfort, Gathering Mode";
    }
    
    public void runDailyIslamicWellness() {
        System.out.println("🕌 Running daily Islamic wellness optimization...");
        prepareForWudu();
        optimizeForFastingComfort();
        setDhikrEnvironment();
        System.out.println("✅ Daily Islamic wellness optimization complete");
    }
}