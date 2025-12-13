package com;

import com.controller.HomeController;
import com.devices.SmartTV;
import com.home.Home;
import com.room.Room;
import java.sql.Time;
import java.time.LocalDateTime;

public class TestAthanMute {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Athan Mute Verification...");

            // 1. Setup Home and Controller
            Home home = new Home("Test Home");
            HomeController controller = new HomeController(home);

            // 2. Setup Room and TV
            Room livingRoom = new Room("Living Room");
            SmartTV tv = new SmartTV("tv-1", "Living Room TV");
            livingRoom.addDevice(tv);
            home.addRoom(livingRoom);

            // 3. Turn on TV and set volume
            tv.turnOn();
            tv.setVolume(50);
            System.out.println("Initial TV State: " + tv.getStatus());

            // 4. Simulate Athan Time (Dhuhr at 12:30)
            // Set time to 12:30
            LocalDateTime athanTime = LocalDateTime.now().withHour(12).withMinute(30).withSecond(0);

            System.out.println("Simulating time: " + athanTime);

            // 5. Trigger Scheduled Events
            controller.checkScheduledEvents(athanTime);

            // 6. Verify Mute
            // We need to access the TV's volume directly or via status
            // Since we can't easily access private fields, we'll rely on getStatus() or
            // console output
            // But getStatus() shows volume.
            System.out.println("TV State after Athan: " + tv.getStatus());

            if (tv.getVolume() == 0) {
                System.out.println("SUCCESS: TV is muted (Volume 0)");
            } else {
                System.err.println("FAILURE: TV is NOT muted (Volume " + tv.getVolume() + ")");
            }

            // 7. Wait for Unmute (5 seconds + buffer)
            System.out.println("Waiting 6 seconds for unmute...");
            Thread.sleep(6000);

            // 8. Verify Unmute
            System.out.println("TV State after wait: " + tv.getStatus());

            if (tv.getVolume() == 50) {
                System.out.println("SUCCESS: TV is unmuted (Volume 50)");
            } else {
                System.err.println("FAILURE: TV is NOT unmuted correctly (Volume " + tv.getVolume() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
