package com.smarthome.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class QuranAPIService {
    private static final String API_URL = "https://api.alquran.cloud/v1/";
    
    public static QuranVerse getRandomVerse() {
        try {
            // Get a random verse between 1 and 6236 (total Quran verses)
            int randomVerseId = (int) (Math.random() * 6236) + 1;
            String urlString = API_URL + "ayah/" + randomVerseId;
            
            // CHANGED: Replaced special character with a hyphen
            System.out.println("- Fetching Quran verse from API: " + urlString);
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            
            // Check if API returned success
            if (!jsonResponse.getString("status").equals("OK")) {
                throw new IOException("API returned error: " + jsonResponse.getString("status"));
            }
            
            JSONObject data = jsonResponse.getJSONObject("data");
            
            // Get Arabic text
            String arabicText = data.getString("text");
            
            // Get surah info
            JSONObject surah = data.getJSONObject("surah");
            String surahName = surah.getString("englishName");
            int verseNumber = data.getInt("numberInSurah");
            
            // Get translation (from a different API call)
            String translation = getTranslation(randomVerseId);
            
            return new QuranVerse(arabicText, translation, surahName + " (" + verseNumber + ")");
            
        } catch (IOException e) {
            // CHANGED: Replaced special character with a hyphen
            System.err.println("- Failed to fetch Quran verse from API: " + e.getMessage());
            throw new RuntimeException("Quran API unavailable - please check internet connection");
        }
    }
    
    private static String getTranslation(int verseId) {
        try {
            String urlString = API_URL + "ayah/" + verseId + "/en.asad";
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject data = jsonResponse.getJSONObject("data");
            return data.getString("text");
            
        } catch (Exception e) {
            // If translation fails, return a generic message
            return "Translation unavailable";
        }
    }
    
    public static class QuranVerse {
        public String arabic;
        public String translation;
        public String surahInfo;
        
        public QuranVerse(String arabic, String translation, String surahInfo) {
            this.arabic = arabic;
            this.translation = translation;
            this.surahInfo = surahInfo;
        }
    }
}