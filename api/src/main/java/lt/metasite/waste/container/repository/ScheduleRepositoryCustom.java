package lt.metasite.waste.container.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lt.metasite.waste.container.Schedule;

public interface ScheduleRepositoryCustom {

    void setNearestScheduleToCompleted(LocalDateTime date, String containerNo);

    List<Schedule> getDelayedSchedules(LocalDate dateFrom, LocalDate dateTo);
}

