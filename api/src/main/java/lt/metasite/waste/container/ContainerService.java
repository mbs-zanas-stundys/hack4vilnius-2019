package lt.metasite.waste.container;

import java.util.List;

import lt.metasite.waste.container.dto.ContainerFlatListDto;
import lt.metasite.waste.container.repository.WasteContainerRepository;

import org.springframework.stereotype.Service;

@Service
public class ContainerService {
    private final WasteContainerRepository repository;

    public ContainerService(
            WasteContainerRepository repository) {
        this.repository = repository;
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
}
