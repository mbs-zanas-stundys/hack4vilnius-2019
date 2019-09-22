package lt.metasite.waste.container.repository;

import java.time.LocalDateTime;

import lt.metasite.waste.container.Schedule;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public ScheduleRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void setNearestScheduleToCompleted(LocalDateTime date, String containerNo) {
        mongoTemplate.updateFirst(new Query(new Criteria("actualDate").is(null).and("expectedDate").lte(date.toLocalDate())),
                                  Update.update("actualDate",date),
                                  Schedule.class);
    }
}
