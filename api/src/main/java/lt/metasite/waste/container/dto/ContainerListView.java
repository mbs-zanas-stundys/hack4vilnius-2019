package lt.metasite.waste.container.dto;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class ContainerListView {
    private String id;
    private String containerNo;
    private GeoJsonPoint position;

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
}
