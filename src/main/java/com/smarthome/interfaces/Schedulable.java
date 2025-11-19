package com.smarthome.interfaces;

import java.time.LocalTime;

public interface Schedulable {
    void scheduleTask(LocalTime time, Runnable task);
    void cancelScheduledTasks();
}
