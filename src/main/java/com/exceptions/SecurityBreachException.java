
package com.exceptions;
import java.time.LocalDateTime;

public class SecurityBreachException extends  RuntimeException {
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