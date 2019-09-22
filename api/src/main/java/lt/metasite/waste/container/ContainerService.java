package lt.metasite.waste.container;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lt.metasite.waste.container.dto.ContainerDto;
import lt.metasite.waste.container.dto.ContainerFlatListDto;
import lt.metasite.waste.container.repository.WasteContainerRepository;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

@Service
public class ContainerService {
    private final WasteContainerRepository repository;
    private final ScheduleService scheduleService;

    public ContainerService(
            WasteContainerRepository repository,
            ScheduleService scheduleService) {
        this.repository = repository;
        this.scheduleService = scheduleService;
    }

    public String save(){
        Container container = new Container();


        return repository.save(container).getId();
    }

    public List<Container> findByPosition(Double longitude, Double latitude, Double distance){

        return repository.findByGeoLocation(longitude, latitude, distance);
    }

    public List<Container> findByAddress(String street, String house, String flat){

        return repository.findByAddress(street, house, flat);
    }

    public Container getContainer(String containerNo){
        return repository.findByContainerNo(containerNo);
    }

    public List<PickupHistory> getPickupHistory(String containerNo){
        return repository.getPickupHistory(containerNo);
    }

    public List<ContainerFlatListDto> getLowRatioContainers(){
        return repository.getLowRationContainers();
    }

    public List<ContainerDto> getDelayedContainersForDate(LocalDate date){
        return scheduleService.getScheduleForDate(date)
        .stream()
        .map(i-> Optional.ofNullable(repository.findByContainerNo(i.getContainerNo()))
                .map(a->toDto(a,i))
        .orElse(null))
                              .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }

    private ContainerDto toDto(Container container, Schedule schedule){
        ContainerDto dto = new ContainerDto();
        dto.setContainerNo(container.getContainerNo());
        dto.setPosition(container.getPosition());
        dto.setStreet(container.getStreet());
        dto.setCompany(container.getCompany());
        dto.setLocation(container.getLocation());
        dto.setHouseNo(container.getHouseNo());
        dto.setCapacity(container.getCapacity());
        if(container.getHistory() != null && !container.getHistory().isEmpty()){
            dto.setMissedPickUp(container.getHistory().stream()
                                         .map(PickupHistory::getDate)
                                         .map(LocalDateTime::toLocalDate)
                                         .noneMatch(i->i.equals(schedule.getExpectedDate())));
        }
        dto.setId(container.getId());
        return dto;
    }
}
