package lt.metasite.waste.container.repository;

import java.time.LocalDate;
import java.util.List;

import lt.metasite.waste.container.Schedule;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<Schedule,String>, ScheduleRepositoryCustom {

    List<Schedule> findByContainerNoAndExpectedDateIsBetween(String containerNo, LocalDate start, LocalDate end);

}
