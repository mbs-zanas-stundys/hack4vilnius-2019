package lt.vilnius.waste.container.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lt.vilnius.waste.container.Container;
import lt.vilnius.waste.container.Pickup;
import lt.metasite.waste.container.dto.*;

import lt.vilnius.waste.container.dto.*;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static java.util.Collections.emptyList;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.geoNear;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.arrayOf;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.valueOf;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.ifNull;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.when;
import static org.springframework.data.mongodb.core.aggregation.DateOperators.DateToString.dateOf;

public class WasteContainerRepositoryImpl implements WasteContainerRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public WasteContainerRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ContainerListView> findByGeoLocation(Double longitude, Double latitude, Double distance) {


        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(geoNear(NearQuery.near(longitude, latitude, Metrics.KILOMETERS)
                                        .maxDistance(distance/1000).spherical(true), "distance"));
        operations.add(project().and("containerNo").as("containerNo")
                                .and("position").as("position"));
        operations.add(limit(5000));
                ;

        return mongoTemplate.aggregate(newAggregation(Container.class, operations), ContainerListView.class)
                .getMappedResults();

    }

//    @Override
//    public List<Container> findByGeoLocation(Double longitude, Double latitude, Double distance) {
//
//
//        List<AggregationOperation> operations = new ArrayList<>();
//        ConditionalOperators.Cond historySlice =
//                when(valueOf(arrayOf("history").length()).greaterThanValue(0))
//                        .thenValueOf(arrayOf("history").slice().itemCount(-1))
//                        .otherwiseValueOf(arrayOf("history").slice().itemCount(1));
//
//        operations.add(geoNear(NearQuery.near(longitude, latitude, Metrics.KILOMETERS)
//                .maxDistance(distance/1000).spherical(true), "distance"));
//        //Fixme slice on nullable array
//
//        operations.add(project().and("containerNo").as("containerNo")
//                .and("position").as("position")
//                .and("street").as("street")
//                .and("company").as("company")
//                .and("location").as("location")
//                .and("houseNo").as("houseNo")
//                .and("capacity").as("capacity")
//                .and(ifNull("history").then(new ArrayList<>())).as("history"));
//        operations.add(project().and("containerNo").as("containerNo")
//                .and("position").as("position")
//                .and("street").as("street")
//                .and("company").as("company")
//                .and("location").as("location")
//                .and("houseNo").as("houseNo")
//                .and("capacity").as("capacity")
//                .and(historySlice).as("history"));
//        operations.add(limit(1500));
//        TypedAggregation<Container> aggregation =
//                newAggregation(Container.class,
//                        operations
//
//                );
//
//        return mongoTemplate.aggregate(aggregation, Container.class).getMappedResults();
//
//    }


    @Override
    public Pickup pushHistory(String containerNo, Pickup history) {
        mongoTemplate.updateFirst(new Query(new Criteria("containerNo").is(containerNo)),
                             new Update().push("history").value(history),Container.class);
        return history;
    }

    @Override
    public ContainerView getContainerView(String containerNo) {
        List<AggregationOperation> operations = new ArrayList<>();
        ConditionalOperators.Cond historySlice =
        when(valueOf(arrayOf(ifNull("history").then(emptyList())).length()).greaterThanValue(0))
                .thenValueOf(arrayOf("history").slice().itemCount(-1))
                .otherwiseValueOf(arrayOf("history").slice().itemCount(1));
        ConditionalOperators.Cond scheduleSlice =
                when(valueOf(arrayOf(ifNull("schedule").then(emptyList())).length()).greaterThanValue(0))
                        .thenValueOf(arrayOf("schedule").slice().itemCount(-1))
                        .otherwiseValueOf(arrayOf("schedule").slice().itemCount(1));

        operations.add(match(new Criteria("containerNo").is(containerNo)));
        operations.add(project().and("containerNo").as("containerNo")
                .and("position").as("position")
                .and("street").as("street")
                .and("company").as("company")
                .and("location").as("location")
                .and("houseNo").as("houseNo")
                .and("capacity").as("capacity")
                .and(historySlice).as("history")
                .and(scheduleSlice).as("schedule"));
        operations.add(unwind("history"));
        operations.add(unwind("schedule"));
        operations.add(project().and("containerNo").as("containerNo")
                .and("position").as("position")
                .and("street").as("street")
                .and("company").as("company")
                .and("location").as("location")
                .and("houseNo").as("houseNo")
                .and("capacity").as("capacity")
                .and("history.pickups").as("history")
                .and("schedule.schedules").as("schedules")
        );
        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations
                );
        return mongoTemplate.aggregate(aggregation,ContainerView.class).getUniqueMappedResult();
    }

    @Override
    public List<Pickup> getPickupHistory(String containerNo, LocalDate date) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(new Criteria("containerNo").is(containerNo)));
        operations.add(unwind("history"));
        operations.add(replaceRoot().withValueOf("history"));
        operations.add(match(Criteria.where("date").is(date.withDayOfMonth(1))));
        operations.add(unwind("pickups"));
        operations.add((replaceRoot().withValueOf("pickups")));
        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations
                );
        return mongoTemplate.aggregate(aggregation, Pickup.class).getMappedResults();
    }

    @Override
    public List<ContainerPickupHistoryView> getLowRationContainers(LocalDate date) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(Criteria.where("history.date").is(date.withDayOfMonth(1))));
        operations.add(unwind("history"));
        operations.add(unwind("history.pickups"));
        operations.add(project().and("history.pickups.weight").divide("capacity").as("ratio")
                                .and("containerNo").as("containerNo")
                                .and("position").as("position")
                                .and("street").as("street")
                                .and("company").as("company")
                                .and("location").as("location")
                                .and("houseNo").as("houseNo")
                                .and("capacity").as("capacity")
                                .and("history.pickups.date").as("date")
                                .and("history.pickups.garbageTruckRegNo").as("garbageTruckRegNo")
                                .and("history.pickups.weight").as("weight")
        );
        operations.add(match(Criteria.where("ratio").lte(10D)));
        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations
                );
        return mongoTemplate.aggregate(aggregation, ContainerPickupHistoryView.class).getMappedResults();
    }

    @Override
    public List<ContainerForDateView> scheduledPickupContainers(LocalDate date) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(Criteria.where("schedule.date").is(date.withDayOfMonth(1))));
        operations.add(unwind("schedule"));
        operations.add(unwind("schedule.schedules"));
        operations.add(match(Criteria.where("schedule.schedules.expectedDate").is(date)));
        operations.add(project()
                .and("containerNo").as("containerNo")
                .and("position").as("position")
                .and("history").as("history")
        );

        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                        operations
                );

        return mongoTemplate.aggregate(aggregation, PickupContainerView.class).getMappedResults()
                .stream()
                .map(t->t.toContainerForDateView(date))
                .collect(Collectors.toList());
    }

}
