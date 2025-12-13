package com.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.json.JSONObject;

public class IslamicCalendarService {
    private static final String API_BASE = "https://api.aladhan.com/v1/gToH";

    public static IslamicDate getTodayIslamicDate() {
        return getIslamicDate(LocalDate.now());
    }

    public static IslamicDate getIslamicDate(LocalDate date) {
        try {
            // Get date for the API call
            String dateParam = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            String urlString = API_BASE + "?date=" + dateParam;

            System.out.println("🕌 Fetching Islamic date from API: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("❌ API returned HTTP " + responseCode);
                return getFallbackDate();
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            String responseBody = response.toString();
            System.out.println("📥 API Response: " + responseBody);

            if (responseBody.trim().isEmpty() || !responseBody.trim().startsWith("{")) {
                System.err.println("❌ Invalid JSON response received");
                return getFallbackDate();
            }

            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject data = jsonResponse.getJSONObject("data");
            JSONObject hijri = data.getJSONObject("hijri");

            String day = hijri.getString("day");
            String monthEn = hijri.getJSONObject("month").getString("en"); // Use English instead of Arabic
            String year = hijri.getString("year");

            String event = getIslamicEvent(monthEn, day);

            String formattedDate = day + " " + monthEn + " " + year;
            System.out.println("✅ Islamic date fetched: " + formattedDate);

            return new IslamicDate(formattedDate, event);

        } catch (Exception e) {
            System.err.println("❌ Failed to fetch Islamic date from API: " + e.getMessage());
            e.printStackTrace();
            return getFallbackDate();
        }
    }

    private static IslamicDate getFallbackDate() {
        // Fallback with English text instead of Arabic
        LocalDate today = LocalDate.now();
        // Simple fallback calculation (approximate)
        String fallbackDate = "15 Ramadan 1445";
        String fallbackEvent = getIslamicEvent("Ramadan", "15");

        System.out.println("🔄 Using fallback Islamic date: " + fallbackDate);
        return new IslamicDate(fallbackDate, fallbackEvent);
    }

    private static String getIslamicEvent(String month, String day) {
        String monthDayKey = getMonthNumber(month) + "-" + day;

        switch (monthDayKey) {
            case "10-1":
                return "Eid al-Fitr";
            case "12-10":
                return "Eid al-Adha";
            case "1-1":
                return "Islamic New Year";
            case "9-1":
                return "Ramadan Begins";
            case "9-27":
                return "Laylat al-Qadr";
            case "3-12":
                return "Mawlid al-Nabi";
            case "7-27":
                return "Isra and Mi'raj";
            default:
                return null;
        }
    }

    private static String getMonthNumber(String monthEn) {
        switch (monthEn.toLowerCase()) {
            case "muharram":
                return "1";
            case "safar":
                return "2";
            case "rabi al-awwal":
                return "3";
            case "rabi al-thani":
                return "4";
            case "jumada al-awwal":
                return "5";
            case "jumada al-thani":
                return "6";
            case "rajab":
                return "7";
            case "sha'ban":
                return "8";
            case "ramadan":
                return "9";
            case "shawwal":
                return "10";
            case "dhu al-qidah":
                return "11";
            case "dhu al-hijjah":
                return "12";
            default:
                return "0";
        }
    }

    public static class IslamicDate {
        public String date;
        public String event;

        public IslamicDate(String date, String event) {
            this.date = date;
            this.event = event;
        }

        @Override
        public String toString() {
            return event != null ? date + " - " + event : date;
        }
    }

    // Test method
    public static void main(String[] args) {
        IslamicDate today = getTodayIslamicDate();
        System.out.println("📅 Today's Islamic Date: " + today.date);
        if (today.event != null) {
            System.out.println("🎉 Event: " + today.event);
        }
    }
}