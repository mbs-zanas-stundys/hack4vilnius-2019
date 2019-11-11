package lt.vilnius.waste.container.value;

import lt.vilnius.waste.container.Pickup;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ContainerView {

    private String id;
    private String containerNo;
    private GeoJsonPoint position;
    private String address;
    private Double capacity;
    private List<Pickup> history;
    private Set<LocalDate> schedules;

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

    public Set<LocalDate> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<LocalDate> schedules) {
        this.schedules = schedules;
    }
}
