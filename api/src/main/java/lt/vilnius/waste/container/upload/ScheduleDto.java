package lt.vilnius.waste.container.upload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Objects;

public class ScheduleDto {
    @JsonProperty("konteinerio_kodas")
    private String containerNo;
    @JsonProperty("data")
    private LocalDate date;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(containerNo) && Objects.nonNull(date);
    }

}
