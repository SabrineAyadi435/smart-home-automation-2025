
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class SecurityBreachException extends  RuntimeException {
    private final String breachLocation;
    private final LocalDateTime timestamp;
    
    public SecurityBreachException(String message, String location) {
        super(message);
        this.breachLocation = location;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getBreachLocation() { return breachLocation; }
    public LocalDateTime getTimestamp() { return timestamp; }
}