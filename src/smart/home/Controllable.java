
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

interface Controllable {
    void turnOn() throws InvalidOperationException;
    void turnOff() throws InvalidOperationException;
    boolean isOn();
}

