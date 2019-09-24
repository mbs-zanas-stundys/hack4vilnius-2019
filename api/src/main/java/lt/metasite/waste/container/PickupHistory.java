package lt.metasite.waste.container;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PickupHistory {
    private LocalDate date;
    private List<Pickup> pickups;


    public static PickupHistory newOf(LocalDate date){
        PickupHistory history = new PickupHistory();
        history.setDate(date.withDayOfMonth(1));
        return history;
    }

    public PickupHistory add(Pickup schedule){
        if(pickups ==null){
            pickups = new ArrayList<>();
        }
        pickups.add(schedule);
        return this;
    }

    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Pickup> getPickups() {
        return pickups;
    }

    public void setPickups(List<Pickup> pickups) {
        this.pickups = pickups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PickupHistory)) return false;
        PickupHistory that = (PickupHistory) o;
        return getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }
}
