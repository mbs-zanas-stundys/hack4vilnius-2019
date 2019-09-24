package lt.metasite.waste.container.repository;

import java.time.LocalDate;
import java.util.List;

import lt.metasite.waste.container.Container;
import lt.metasite.waste.container.Pickup;
import lt.metasite.waste.container.dto.ContainerListView;
import lt.metasite.waste.container.dto.ContainerPickupHistoryView;
import lt.metasite.waste.container.dto.ContainerView;
import lt.metasite.waste.container.dto.MissedPickupContainerView;

public interface WasteContainerRepositoryCustom {

    List<ContainerListView> findByGeoLocation(Double longitude, Double latitude, Double distance);

    Pickup pushHistory(String containerNo, Pickup history);

    ContainerView getContainerView(String containerNo);

    List<Pickup> getPickupHistory(String pickupHistory, LocalDate date);

    List<ContainerPickupHistoryView> getLowRationContainers(LocalDate date);

    List<MissedPickupContainerView> getMissedPickupContainers(LocalDate date);
}
