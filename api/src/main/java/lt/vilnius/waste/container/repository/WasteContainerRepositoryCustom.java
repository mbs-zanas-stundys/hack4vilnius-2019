package lt.vilnius.waste.container.repository;

import lt.vilnius.waste.container.Pickup;
import lt.vilnius.waste.container.upload.ContainerDto;
import lt.vilnius.waste.container.value.ContainerForDateView;
import lt.vilnius.waste.container.value.ContainerListView;
import lt.vilnius.waste.container.value.ContainerPickupHistoryView;
import lt.vilnius.waste.container.value.ContainerView;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WasteContainerRepositoryCustom {

    List<ContainerListView> findByGeoLocation(Double longitude, Double latitude, Double distance);

    String addOrReplaceContainer(ContainerDto dto);

    String savePickup(String containerNo, Pickup pickup);

    String saveSchedules(String containerNo, Set<LocalDate> schedules);

    ContainerView getContainerView(String containerNo);

    List<Pickup> getPickupHistory(String pickupHistory, LocalDate date);

    List<ContainerPickupHistoryView> getLowRationContainers(LocalDate date);

    List<ContainerForDateView> scheduledPickupContainers(LocalDate date);
}
