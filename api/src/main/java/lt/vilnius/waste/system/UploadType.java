package lt.vilnius.waste.system;

import java.time.Period;

public enum UploadType {
    CONTAINER_UPLOAD(Period.ofMonths(1)),
    PICKUP_UPLOAD(Period.ofDays(1));

    private Period periodBetweenEvents;

    UploadType(Period period) {
        periodBetweenEvents = period;
    }

    public Period getPeriodBetweenEvents() {
        return periodBetweenEvents;
    }
}
