package com.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class QiblaService {
    private static final String API_URL = "http://api.aladhan.com/v1/qibla/";
    
    public static QiblaDirection getQiblaDirection(double latitude, double longitude) {
        try {
            String urlString = API_URL + latitude + "/" + longitude;
            // CHANGED: Replaced special character with a hyphen
            System.out.println("- Fetching Qibla direction from: " + urlString);
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                // CHANGED: Replaced special character with a hyphen
                System.err.println("- Qibla API returned HTTP " + responseCode);
                return getFallbackDirection(latitude, longitude);
            }
            
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject data = jsonResponse.getJSONObject("data");
            
            double direction = data.getDouble("direction");
            String compassDirection = getCompassDirection(direction);
            
            // CHANGED: Replaced special character with a hyphen
            System.out.println("- Qibla direction found: " + compassDirection + " (" + direction + "°)");
            return new QiblaDirection(direction, compassDirection);
            
        } catch (IOException e) {
            // CHANGED: Replaced special character with a hyphen
            System.err.println("- Failed to fetch Qibla direction from API: " + e.getMessage());
            return getFallbackDirection(latitude, longitude);
        }
    }
    
    private static QiblaDirection getFallbackDirection(double latitude, double longitude) {
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Using fallback Qibla calculation for coordinates: " + 
                          latitude + ", " + longitude);
        // Simple fallback: approximate direction for demo
        double fallbackDegrees = calculateApproximateQibla(latitude, longitude);
        String compassDirection = getCompassDirection(fallbackDegrees);
        return new QiblaDirection(fallbackDegrees, compassDirection);
    }
    
    private static double calculateApproximateQibla(double latitude, double longitude) {
        // Very approximate calculation for demo purposes
        // In real implementation, use proper spherical trigonometry
        if (latitude > 40) return 120.0; // Northern regions
        if (latitude < 10) return 65.0;  // Southern regions
        return 135.0; // Default for mid-latitudes
    }
    
    private static String getCompassDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5) return "North";
        if (degrees >= 22.5 && degrees < 67.5) return "Northeast";
        if (degrees >= 67.5 && degrees < 112.5) return "East";
        if (degrees >= 112.5 && degrees < 157.5) return "Southeast";
        if (degrees >= 157.5 && degrees < 202.5) return "South";
        if (degrees >= 202.5 && degrees < 247.5) return "Southwest";
        if (degrees >= 247.5 && degrees < 292.5) return "West";
        if (degrees >= 292.5 && degrees < 337.5) return "Northwest";
        return "Unknown";
    }
    
    public static class QiblaDirection {
        public double degrees;
        public String direction;
        
        public QiblaDirection(double degrees, String direction) {
            this.degrees = degrees;
            this.direction = direction;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%.1f°)", direction, degrees);
        }
        
        // Additional helpful methods
        public String toDisplayString() {
            // CHANGED: Replaced special character with a hyphen
            return "- Qibla Direction: " + toString();
        }
        
        public boolean isValid() {
            return degrees >= 0 && degrees <= 360 && !"Unknown".equals(direction);
        }
    }
    
    // Test method
    public static void main(String[] args) {
        // Test with Tunis coordinates
        QiblaDirection direction = getQiblaDirection(36.8065, 10.1815);
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Test Qibla for Tunis: " + direction.toDisplayString());
        // CHANGED: Replaced special character with a hyphen
        System.out.println("- Valid direction: " + direction.isValid());
    }
}