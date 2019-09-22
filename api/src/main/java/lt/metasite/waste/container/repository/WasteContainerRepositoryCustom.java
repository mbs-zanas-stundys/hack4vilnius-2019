package lt.metasite.waste.container.repository;

import java.util.List;

import lt.metasite.waste.container.Container;
import lt.metasite.waste.container.PickupHistory;

public interface WasteContainerRepositoryCustom {

    List<Container> findByGeoLocation(Double longitude, Double latitude, Double distance);

    List<Container> findByAddress(String street, String houseNo, String flatNo);

    PickupHistory pushHistory(String containerNo, PickupHistory history);

    Container findByContainerNo(String containerNo);

    List<PickupHistory> getPickupHistory(String pickupHistory);
}
