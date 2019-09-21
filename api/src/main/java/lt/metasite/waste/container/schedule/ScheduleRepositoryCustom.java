package lt.metasite.waste.container.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ScheduleRepositoryCustom {

    void setNearestScheduleToCompleted(LocalDateTime date, String containerNo);
}
