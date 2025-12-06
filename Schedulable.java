
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

interface Schedulable {
    void schedule(String schedulePattern) throws InvalidOperationException;
    String getSchedule();
    boolean isScheduledActive();
}

