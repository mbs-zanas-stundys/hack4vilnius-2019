package lt.metasite.waste.container.repository;

import java.util.ArrayList;
import java.util.List;

import lt.metasite.waste.container.Container;
import lt.metasite.waste.container.PickupHistory;

import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.geoNear;
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

public class WasteContainerRepositoryImpl implements WasteContainerRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public WasteContainerRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

//    @Override
//    public List<Container> findByGeoLocation(Double longitude, Double latitude, Double distance) {
//
//        Query query =
//                new Query(new Criteria("position").nearSphere(new GeoJsonPoint(
//                        longitude,
//                        latitude)).maxDistance(distance));
//
//
//        return mongoTemplate.find(query, Container.class);
//
//    }

    @Override
    public List<Container> findByGeoLocation(Double longitude, Double latitude, Double distance) {


        List<AggregationOperation> operations = new ArrayList<>();
        ConditionalOperators.Cond historySlice =
                when(valueOf(arrayOf("history").length()).greaterThanValue(0))
                        .thenValueOf(arrayOf("history").slice().itemCount(-1))
                        .otherwiseValueOf(arrayOf("history").slice().itemCount(1));

        operations.add(geoNear(NearQuery.near(longitude, latitude, Metrics.KILOMETERS)
                                        .maxDistance(distance/1000).spherical(true), "distance"));
        operations.add(project().and("containerNo").as("containerNo")
                                .and("position").as("position")
                                .and("street").as("street")
                                .and("company").as("company")
                                .and("location").as("location")
                                .and("houseNo").as("houseNo")
                                .and("capacity").as("capacity")
                                .and(ifNull("history").then(new ArrayList<>())).as("history"));
        operations.add(project().and("containerNo").as("containerNo")
                                .and("position").as("position")
                                .and("street").as("street")
                                .and("company").as("company")
                                .and("location").as("location")
                                .and("houseNo").as("houseNo")
                                .and("capacity").as("capacity")
                                .and(historySlice).as("history"));
        operations.add(limit(1500));
        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations

                );

        return mongoTemplate.aggregate(aggregation, Container.class).getMappedResults();

    }


    @Override
    public List<Container> findByAddress(String street, String houseNo, String flatNo) {

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(new Criteria("street").is(street)
                                                   .and("houseNo").is(houseNo)));

        if(flatNo!=null){
            operations.add(match(new Criteria("flatNo").is(flatNo)));
        }

        ConditionalOperators.Cond historySlice =
                when(valueOf(arrayOf("history").length()).greaterThanValue(0))
                        .thenValueOf(arrayOf("history").slice().itemCount(-1))
                        .otherwiseValueOf(arrayOf("history").slice().itemCount(1));
        operations.add(project().and("containerNo").as("containerNo")
                                .and("position").as("position")
                                .and("street").as("street")
                                .and("company").as("company")
                                .and("location").as("location")
                                .and("houseNo").as("houseNo")
                                .and("capacity").as("capacity")
                               .and(ifNull("history").then(new ArrayList<>())).as("history"));
        operations.add(project().and("containerNo").as("containerNo")
                                .and("position").as("position")
                                .and("street").as("street")
                                .and("company").as("company")
                                .and("location").as("location")
                                .and("houseNo").as("houseNo")
                                .and("capacity").as("capacity")
                                .and(historySlice).as("history"));

        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations
                );

        return mongoTemplate.aggregate(aggregation, Container.class).getMappedResults();
    }

    @Override
    public PickupHistory pushHistory(String containerNo, PickupHistory history) {
        mongoTemplate.updateFirst(new Query(new Criteria("containerNo").is(containerNo)),
                             new Update().push("history").value(history),Container.class);
        return history;
    }

    @Override
    public Container findByContainerNo(String containerNo) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(new Criteria("containerNo").is(containerNo)));
        ConditionalOperators.Cond historySlice =
                when(valueOf(arrayOf("history").length()).greaterThanValue(0))
                        .thenValueOf(arrayOf("history").slice().itemCount(-3))
                        .otherwiseValueOf(arrayOf("history").slice().itemCount(1));

        operations.add(project().and("containerNo").as("containerNo")
        .and("position").as("position")
        .and("street").as("street")
        .and("company").as("company")
        .and("location").as("location")
        .and("houseNo").as("houseNo")
        .and("capacity").as("capacity")
        .and(historySlice).as("history"));

        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations
                );
        return mongoTemplate.aggregate(aggregation,Container.class).getUniqueMappedResult();
    }

    @Override
    public List<PickupHistory> getPickupHistory(String containerNo) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(new Criteria("containerNo").is(containerNo)));
        operations.add(unwind("history"));
        operations.add(replaceRoot().withValueOf("history"));

        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations
                );
        return mongoTemplate.aggregate(aggregation,PickupHistory.class).getMappedResults();
    }



}