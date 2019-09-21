package lt.metasite.waste.container.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<Schedule,String> {

    List<Schedule> findByContainerNoAndExpectedDateIsBetween(String containerNo, LocalDate start, LocalDate end);
}
