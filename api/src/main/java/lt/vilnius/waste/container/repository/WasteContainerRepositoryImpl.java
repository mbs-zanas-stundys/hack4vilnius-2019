package lt.vilnius.waste.container.repository;

import lt.vilnius.waste.container.Container;
import lt.vilnius.waste.container.Pickup;
import lt.vilnius.waste.container.upload.ContainerDto;
import lt.vilnius.waste.container.value.*;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Gte;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Lte;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.ArrayOperators.arrayOf;
import static org.springframework.data.mongodb.core.aggregation.BooleanOperators.And.and;
import static org.springframework.data.mongodb.core.aggregation.ComparisonOperators.valueOf;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ifNull;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class WasteContainerRepositoryImpl implements WasteContainerRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public WasteContainerRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ContainerListView> findByGeoLocation(Double longitude, Double latitude, Double distance) {

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(geoNear(NearQuery.near(longitude, latitude, Metrics.KILOMETERS)
                .maxDistance(distance / 1000).spherical(true), "distance"));
        operations.add(project().and("containerNo").as("containerNo")
                .and("position").as("position"));
        operations.add(limit(5000));


        return mongoTemplate.aggregate(newAggregation(Container.class, operations), ContainerListView.class)
                .getMappedResults();

    }

    @Override
    public String addOrReplaceContainer(ContainerDto dto) {
        mongoTemplate.upsert(query(where("containerNo").is(dto.getContainerNo())),
                Update.update("position", new GeoJsonPoint(dto.getLongitude(), dto.getLatitutde()))
                        .set("address", dto.getAddress())
                        .set("capacity", dto.getCapacity()),
                Container.class);

        return dto.getContainerNo();
    }

    @Override
    public String savePickup(String containerNo, Pickup pickup) {
        mongoTemplate.updateFirst(query(where("containerNo").is(containerNo)),
                new Update()
                        .set("company", pickup.getCompany())
                        .push("pickups", pickup),
                Container.class);
        return containerNo;
    }

    @Override
    public String saveSchedules(String containerNo, Set<LocalDate> schedules) {
        mongoTemplate.updateFirst(query(where("containerNo").is(containerNo)),
                new Update()
                        .addToSet("schedules").each(schedules.toArray()),
                Container.class);
        return containerNo;
    }

    @Override
    public ContainerView getContainerView(String containerNo) {
        List<AggregationOperation> operations = new ArrayList<>();
        ConditionalOperators.Cond historySlice =
                when(valueOf(arrayOf(ifNull("pickups").then(emptyList())).length()).greaterThanValue(5))
                        .thenValueOf(arrayOf("pickups").slice().itemCount(-5))
                        .otherwiseValueOf(arrayOf("pickups").slice().itemCount(5));
        ConditionalOperators.Cond scheduleSlice =
                when(valueOf(arrayOf(ifNull("schedules").then(emptyList())).length()).greaterThanValue(5))
                        .thenValueOf(arrayOf("schedules").slice().itemCount(-5))
                        .otherwiseValueOf(arrayOf("schedules").slice().itemCount(5));

        operations.add(match(new Criteria("containerNo").is(containerNo)));
        operations.add(project().and("containerNo").as("containerNo")
                .and("position").as("position")
                .and("address").as("address")
                .and("company").as("company")
                .and("capacity").as("capacity")
                .and(scheduleSlice).as("schedules")
                .and(historySlice).as("history")
        );

        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                        operations
                );
        return mongoTemplate.aggregate(aggregation, ContainerView.class).getUniqueMappedResult();
    }

    @Override
    public List<Pickup> getPickupHistory(String containerNo, LocalDate date) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(new Criteria("containerNo").is(containerNo)));
        operations.add(unwind("pickups"));
        operations.add((replaceRoot().withValueOf("pickups")));
        operations.add(match(where("date").gt(date.withDayOfMonth(1)).lte(date.withDayOfMonth(1).plusMonths(1))));
        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                        operations
                );
        return mongoTemplate.aggregate(aggregation, Pickup.class).getMappedResults();
    }

    @Override
    public List<ContainerPickupHistoryView> getLowRationContainers(LocalDate date) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(where("pickups.date").gt(date.atTime(LocalTime.MIN)).lte(date.atTime(LocalTime.MAX))));
        operations.add(unwind("pickups"));
        operations.add(project().and("pickups.weight").divide("capacity").as("ratio")
                .and("containerNo").as("containerNo")
                .and("position").as("position")
                .and("address").as("address")
                .and("company").as("company")
                .and("capacity").as("capacity")
                .and("pickups.date").as("date")
                .and("pickups.garbageTruckRegNo").as("garbageTruckRegNo")
                .and("pickups.weight").as("weight")
        );
        operations.add(match(where("ratio").lte(10D)));
        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                        operations
                );
        return mongoTemplate.aggregate(aggregation, ContainerPickupHistoryView.class).getMappedResults();
    }

    @Override
    public List<ContainerForDateView> scheduledPickupContainers(LocalDate date) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(where("schedules").all(date)));
        operations.add(project()
                .and("containerNo").as("containerNo")
                .and("position").as("position")
                .and("pickups").filter("pickup", and(Gte.valueOf("pickup.date").greaterThanEqualToValue(date.atTime(LocalTime.MIN)),
                        Lte.valueOf("pickup.date").lessThanEqualToValue(date.atTime(LocalTime.MAX)))).as("pickups")
        );

        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                        operations
                );

        return mongoTemplate.aggregate(aggregation, PickupContainerView.class).getMappedResults()
                .stream()
                .map(PickupContainerView::toContainerForDateView)
                .collect(Collectors.toList());
    }

}
