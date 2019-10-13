package lt.metasite.waste.container;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class Schedule {
    private Long externalId;
    private LocalDate expectedDate;

    public LocalDate getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(LocalDate expectedDate) {
        this.expectedDate = expectedDate;
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
        if (!(o instanceof Schedule)) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(getExternalId(), schedule.getExternalId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExternalId());
    }
}
