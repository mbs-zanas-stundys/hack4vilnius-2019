package lt.vilnius.waste.container.upload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class PickupDto {
    @JsonProperty("konteinerio_kodas")
    private String containerNo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty("aptarnavimo_data")
    private Date date;
    @JsonProperty("aptarnavimo_svoris")
    private Double weight;
    @JsonProperty("vezejo_pavadinimas")
    private String company;
    @JsonProperty("konteinerio_aptarnavimas")
    private int wasServiced;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getWasServiced() {
        return wasServiced;
    }

    public void setWasServiced(int wasServiced) {
        this.wasServiced = wasServiced;
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(containerNo) &&
                Objects.nonNull(date) &&
                Objects.nonNull(weight) &&
                StringUtils.isNotEmpty(company);
    }
}
