
package smart.home;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

interface EnergyConsumer {
    double calculateEnergyConsumption();
    double getEnergyConsumptionRate();
    void setEnergySavingMode(boolean enable);
}

