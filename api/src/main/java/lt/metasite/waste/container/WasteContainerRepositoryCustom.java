package lt.metasite.waste.container;

import java.util.List;

public interface WasteContainerRepositoryCustom {

    List<Container> findByGeoLocation(Double longitude, Double latitude, Double distance);

    List<Container> findByAddress(String street, String houseNo, String flatNo);

    void pushHistory(String containerNo, PickupHistory history);

    Container findByContainerNo(String containerNo);

    List<PickupHistory> getPickupHistory(String pickupHistory);
}
