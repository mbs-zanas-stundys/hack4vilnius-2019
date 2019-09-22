package lt.metasite.waste.container.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ScheduleRepositoryCustom {

    void setNearestScheduleToCompleted(LocalDateTime date, String containerNo);
}
