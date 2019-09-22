package lt.metasite.waste.container.dto;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class ContainerFlatListDto {
    private String id;
    private String containerNo;
    private GeoJsonPoint position;
    private String street;
    private String company;
    private String location;
    private String houseNo;
    private Double capacity;
    private Double ratio;
    private Double weight;
    private String garbageTruckRegNo;
    private LocalDateTime date;

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

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public String getGarbageTruckRegNo() {
        return garbageTruckRegNo;
    }

    public void setGarbageTruckRegNo(String garbageTruckRegNo) {
        this.garbageTruckRegNo = garbageTruckRegNo;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
