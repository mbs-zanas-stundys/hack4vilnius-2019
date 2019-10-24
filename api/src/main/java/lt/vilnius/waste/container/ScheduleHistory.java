package lt.vilnius.waste.container;

import java.time.LocalDate;
import java.util.*;

public class ScheduleHistory {
    private LocalDate date;
    private Set<Schedule> schedules;

    public ScheduleHistory(){

    }

    public static ScheduleHistory newOf(LocalDate date){
        ScheduleHistory history = new ScheduleHistory();
        history.setDate(date);
        return history;
    }

    public ScheduleHistory add(Schedule schedule){
        if(schedules ==null){
            schedules = new HashSet<>();
        }
        schedules.add(schedule);
        return this;
    }

    public ScheduleHistory addAll(List<Schedule> schedule){
        if(schedules ==null){
            schedules = new HashSet<>();
        }
        schedules.addAll(schedule);
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleHistory)) return false;
        ScheduleHistory history = (ScheduleHistory) o;
        return getDate().equals(history.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }
}
