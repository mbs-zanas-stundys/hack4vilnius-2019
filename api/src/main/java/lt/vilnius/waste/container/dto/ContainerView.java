package lt.vilnius.waste.container.dto;

import lt.vilnius.waste.container.Pickup;
import lt.vilnius.waste.container.Schedule;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

public class ContainerView {

    private String id;
    private String containerNo;
    private GeoJsonPoint position;
    private String street;
    private String company;
    private String location;
    private String houseNo;
    private Double capacity;
    private List<Pickup> history;
    private List<Schedule> schedules;

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

    public GeoJsonPoint getPosition() {
        return position;
    }

    public void setPosition(GeoJsonPoint position) {
        this.position = position;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public List<Pickup> getHistory() {
        return history;
    }

    public void setHistory(List<Pickup> history) {
        this.history = history;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
