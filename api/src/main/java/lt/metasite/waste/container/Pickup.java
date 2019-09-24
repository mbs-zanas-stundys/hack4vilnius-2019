package lt.metasite.waste.container;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class Pickup {
    private LocalDateTime date;
    private String garbageTruckRegNo;
    private Double weight;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getGarbageTruckRegNo() {
        return garbageTruckRegNo;
    }

    public void setGarbageTruckRegNo(String garbageTruckRegNo) {
        this.garbageTruckRegNo = garbageTruckRegNo;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
