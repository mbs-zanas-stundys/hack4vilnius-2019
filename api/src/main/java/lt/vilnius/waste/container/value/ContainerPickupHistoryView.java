package lt.vilnius.waste.container.value;

import java.time.LocalDateTime;

public class ContainerPickupHistoryView extends ContainerListView {
    private Double capacity;
    private Double ratio;
    private Double weight;
    private String garbageTruckRegNo;
    private LocalDateTime date;

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
