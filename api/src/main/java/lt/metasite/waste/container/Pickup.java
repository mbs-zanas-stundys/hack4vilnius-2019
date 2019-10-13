package lt.metasite.waste.container;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class Pickup {
    private Long externalId;
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

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pickup)) return false;
        Pickup pickup = (Pickup) o;
        return Objects.equals(getExternalId(), pickup.getExternalId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExternalId());
    }
}
