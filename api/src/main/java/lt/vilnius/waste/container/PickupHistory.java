package lt.vilnius.waste.container;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PickupHistory {
    private LocalDate date;
    private Set<Pickup> pickups;


    public static PickupHistory newOf(LocalDate date){
        PickupHistory history = new PickupHistory();
        history.setDate(date.withDayOfMonth(1));
        return history;
    }

    public PickupHistory add(Pickup schedule){
        if(pickups ==null){
            pickups = new HashSet<>();
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

    public Set<Pickup> getPickups() {
        return pickups;
    }

    public void setPickups(Set<Pickup> pickups) {
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
