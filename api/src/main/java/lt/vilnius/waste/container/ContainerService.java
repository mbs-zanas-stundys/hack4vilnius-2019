package lt.vilnius.waste.container;

import lt.vilnius.waste.container.repository.WasteContainerRepository;
import lt.vilnius.waste.container.value.ContainerForDateView;
import lt.vilnius.waste.container.value.ContainerListView;
import lt.vilnius.waste.container.value.ContainerPickupHistoryView;
import lt.vilnius.waste.container.value.ContainerView;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContainerService {
    private final WasteContainerRepository repository;

    public ContainerService(
            WasteContainerRepository repository) {
        this.repository = repository;
    }

    public List<ContainerListView> findByPosition(Double longitude, Double latitude, Double distance) {

        return repository.findByGeoLocation(longitude, latitude, distance);
    }

    public ContainerView getContainerView(String containerNo) {
        return repository.getContainerView(containerNo);
    }

    public List<Pickup> getPickupHistory(String containerNo, LocalDate date) {
        return repository.getPickupHistory(containerNo, date);
    }

    public List<ContainerPickupHistoryView> getLowRatioContainers(LocalDate date) {

        return repository.getLowRationContainers(date);
    }

    public List<ContainerForDateView> pickupsForDate(LocalDate date) {
        return repository.scheduledPickupContainers(date);
    }

}
