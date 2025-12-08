package com.ui.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading and managing device and UI icons.
 * Provides centralized icon management with caching for performance.
 */
public class IconManager {
    
    // Icon cache to avoid reloading the same icons
    private static final Map<String, Image> iconCache = new HashMap<>();
    
    // Icon paths
    private static final String ICON_BASE_PATH = "/com/ui/icons/";
    
    // Default icon size
    private static final int DEFAULT_ICON_SIZE = 24;
    
    /**
     * Loads an icon from the resources folder.
     * 
     * @param iconName The name of the icon file (without path)
     * @return The loaded Image, or null if not found
     */
    public static Image loadIcon(String iconName) {
        if (iconName == null || iconName.isEmpty()) {
            System.err.println("Icon name cannot be null or empty");
            return null;
        }
        
        // Check cache first
        if (iconCache.containsKey(iconName)) {
            return iconCache.get(iconName);
        }
        
        try {
            String iconPath = ICON_BASE_PATH + iconName;
            InputStream iconStream = IconManager.class.getResourceAsStream(iconPath);
            
            if (iconStream != null) {
                Image icon = new Image(iconStream);
                iconCache.put(iconName, icon);
                return icon;
            } else {
                System.err.println("Icon not found: " + iconPath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading icon " + iconName + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates an ImageView with the specified icon.
     * 
     * @param iconName The name of the icon file
     * @return An ImageView containing the icon, or null if icon not found
     */
    public static ImageView createIconView(String iconName) {
        return createIconView(iconName, DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE);
    }
    
    /**
     * Creates an ImageView with the specified icon and size.
     * 
     * @param iconName The name of the icon file
     * @param width The width of the icon
     * @param height The height of the icon
     * @return An ImageView containing the icon, or null if icon not found
     */
    public static ImageView createIconView(String iconName, int width, int height) {
        Image icon = loadIcon(iconName);
        if (icon != null) {
            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            return imageView;
        }
        return null;
    }
    
    /**
     * Gets the icon name for a specific device type.
     * 
     * @param deviceType The type of device (e.g., "Light", "AC", "SmartTV")
     * @return The icon file name for the device type
     */
    public static String getDeviceIcon(String deviceType) {
        if (deviceType == null) {
            return "device-default.png";
        }
        
        switch (deviceType.toLowerCase()) {
            case "light":
                return "light.png";
            case "ac":
                return "ac.png";
            case "smarttv":
                return "tv.png";
            case "speaker":
                return "speaker.png";
            case "doorlock":
                return "lock.png";
            case "securitycamera":
                return "camera.png";
            case "smartfridge":
                return "fridge.png";
            case "smartplug":
                return "plug.png";
            case "smartfaucet":
                return "faucet.png";
            case "smartmirror":
                return "mirror.png";
            case "motionsensor":
                return "motion-sensor.png";
            case "doorwindowsensor":
                return "door-sensor.png";
            case "smokedetector":
                return "smoke-detector.png";
            case "airqualitysensor":
                return "air-quality.png";
            case "alarmsiren":
                return "siren.png";
            default:
                return "device-default.png";
        }
    }
    
    /**
     * Gets the Unicode emoji for a specific device type as a fallback.
     * 
     * @param deviceType The type of device
     * @return The Unicode emoji string for the device type
     */
    public static String getDeviceEmoji(String deviceType) {
        if (deviceType == null) {
            return "📱";
        }
        
        switch (deviceType.toLowerCase()) {
            case "light":
                return "💡";
            case "ac":
                return "❄️";
            case "smarttv":
                return "📺";
            case "speaker":
                return "🔊";
            case "doorlock":
                return "🔒";
            case "securitycamera":
                return "📹";
            case "smartfridge":
                return "🧊";
            case "smartplug":
                return "🔌";
            case "smartfaucet":
                return "🚰";
            case "smartmirror":
                return "🪞";
            case "motionsensor":
                return "👁️";
            case "doorwindowsensor":
                return "🚪";
            case "smokedetector":
                return "🔥";
            case "airqualitysensor":
                return "🌬️";
            case "alarmsiren":
                return "🚨";
            default:
                return "📱";
        }
    }
    
    /**
     * Clears the icon cache to free memory.
     */
    public static void clearCache() {
        iconCache.clear();
    }
    
    /**
     * Gets the number of cached icons.
     * 
     * @return The number of icons in the cache
     */
    public static int getCacheSize() {
        return iconCache.size();
    }
}
