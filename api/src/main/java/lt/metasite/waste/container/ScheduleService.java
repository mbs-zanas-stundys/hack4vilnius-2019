package lt.metasite.waste.container;

import java.time.LocalDate;
import java.util.List;

import lt.metasite.waste.container.repository.ScheduleRepository;

import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    public List<Schedule> getScheduleForContainer(String containerNo, LocalDate dateFrom,
                                                  LocalDate dateTo){

        return repository.findByContainerNoAndExpectedDateIsBetween(containerNo,
                                                                    dateFrom,
                                                                    dateTo);
    }

    public List<Schedule> getDelayedSchedules(){
        return  repository.getDelayedSchedules(LocalDate.now().withDayOfMonth(1),
                                               LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1));
    }


}
