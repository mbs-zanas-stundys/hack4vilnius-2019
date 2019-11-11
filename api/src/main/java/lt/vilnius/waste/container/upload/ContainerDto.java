package lt.vilnius.waste.container.upload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ContainerDto {
    @JsonProperty("konteinerio_kodas")
    private String containerNo;
    @JsonProperty("konteinerio_longtitude")
    private Double longitude;
    @JsonProperty("konteinerio_latitude")
    private Double latitutde;
    @JsonProperty("konteinerio_adresas")
    private String address;
    @JsonProperty("talpa")
    private Double capacity;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitutde() {
        return latitutde;
    }

    public void setLatitutde(Double latitutde) {
        this.latitutde = latitutde;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    boolean isValid() {
        return StringUtils.isNotEmpty(containerNo) &&
                Objects.nonNull(longitude) &&
                Objects.nonNull(latitutde) &&
                StringUtils.isNotEmpty(address) &&
                Objects.nonNull(capacity);

    }

}
