package lt.metasite.waste.container.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lt.metasite.waste.container.Schedule;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static com.mongodb.client.model.Filters.where;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.valueOf;

public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public ScheduleRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void setNearestScheduleToCompleted(LocalDateTime date, String containerNo) {
        mongoTemplate.updateFirst(new Query(new Criteria("actualDate").exists(false).and(
                "expectedDate").lte(date.toLocalDate())),
                                  Update.update("actualDate",date),
                                  Schedule.class);
    }

    @Override
    public List<Schedule> getDelayedSchedules(LocalDate dateFrom, LocalDate dateTo) {

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(new Criteria("actualDate").exists(true)));
        TypedAggregation<Schedule> aggregation =
                newAggregation(Schedule.class,
                               operations
                );
        return mongoTemplate.aggregate(aggregation, Schedule.class).getMappedResults();
    }
}
