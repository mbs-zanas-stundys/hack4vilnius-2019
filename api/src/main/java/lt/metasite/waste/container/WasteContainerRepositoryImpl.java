package lt.metasite.waste.container;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

public class WasteContainerRepositoryImpl implements WasteContainerRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public WasteContainerRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Container> findByGeoLocation(Double longitude, Double latitude, Double distance) {

        Query query =
                new Query(new Criteria("position").nearSphere(new GeoJsonPoint(
                        longitude,
                        latitude)).maxDistance(distance));

        return mongoTemplate.find(query, Container.class);

    }


    @Override
    public List<Container> findByAddress(String street, String houseNo, String flatNo) {

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(new Criteria("street").is(street)
                                                   .and("houseNo").is(houseNo)));

        if(flatNo!=null){
            operations.add(match(new Criteria("flatNo").is(flatNo)));
        }
        operations.add(project().andExclude("history"));

        TypedAggregation<Container> aggregation =
                newAggregation(Container.class,
                               operations
                );

        return mongoTemplate.aggregate(aggregation, Container.class).getMappedResults();
    }
}
