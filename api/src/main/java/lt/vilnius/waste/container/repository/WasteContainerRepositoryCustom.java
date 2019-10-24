package lt.vilnius.waste.container.repository;

import java.time.LocalDate;
import java.util.List;

import lt.vilnius.waste.container.Pickup;
import lt.metasite.waste.container.dto.*;
import lt.vilnius.waste.container.dto.ContainerForDateView;
import lt.vilnius.waste.container.dto.ContainerListView;
import lt.vilnius.waste.container.dto.ContainerPickupHistoryView;
import lt.vilnius.waste.container.dto.ContainerView;

public interface WasteContainerRepositoryCustom {

    List<ContainerListView> findByGeoLocation(Double longitude, Double latitude, Double distance);

    Pickup pushHistory(String containerNo, Pickup history);

    ContainerView getContainerView(String containerNo);

    List<Pickup> getPickupHistory(String pickupHistory, LocalDate date);

    List<ContainerPickupHistoryView> getLowRationContainers(LocalDate date);

    List<ContainerForDateView> scheduledPickupContainers(LocalDate date);
}
