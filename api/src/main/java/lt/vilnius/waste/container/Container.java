package lt.vilnius.waste.container;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static java.util.Collections.singletonList;

@Document(collection = "container")
public class Container {

    @Id
    private String id;
    @Indexed(unique = true)
    private String containerNo;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint position;
    private String street;
    private String company;
    private String location;
    private String houseNo;
    private Double capacity;
    private List<PickupHistory> history;
    private List<ScheduleHistory> schedule;

    public Container withSchedules(LocalDate date, List<Schedule> schedule){
        Stream.ofNullable(this.schedule)
                .flatMap(Collection::stream)
                .filter(s->s.getDate().equals(date))
                .findFirst()
                .ifPresentOrElse(h -> h.addAll(schedule),
                        () -> setSchedule(singletonList(ScheduleHistory.newOf(date).addAll(schedule))));
        return this;
    }

    public Container withHistory(LocalDate date, Pickup pickup){
        Stream.ofNullable(history)
                .flatMap(Collection::stream)
                .filter(s->s.getDate().equals(date))
                .findFirst()
                .ifPresentOrElse(h -> h.add(pickup),
                        () -> setHistory(singletonList(PickupHistory.newOf(date).add(pickup))));
        return this;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public GeoJsonPoint getPosition() {
        return position;
    }

    public void setPosition(GeoJsonPoint position) {
        this.position = position;
    }

    public List<PickupHistory> getHistory() {
        return history;
    }

    public void setHistory(List<PickupHistory> history) {
        this.history = history;
    }

    public List<ScheduleHistory> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleHistory> schedule) {
        this.schedule = schedule;
    }
}
