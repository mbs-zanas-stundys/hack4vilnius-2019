package lt.vilnius.waste.container.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import lt.vilnius.waste.container.PickupHistory;

public class PickupContainerView extends ContainerListView{

    private List<PickupHistory> history;

    public ContainerForDateView toContainerForDateView(LocalDate date){
        ContainerForDateView dateView = new ContainerForDateView();
        dateView.setPosition(getPosition());
        dateView.setContainerNo(getContainerNo());
        dateView.setId(getId());
        dateView.setMissedPickup(Stream.ofNullable(history)
                .flatMap(Collection::stream)
                .filter(h -> h.getDate().equals(date.withDayOfMonth(1)))
                .flatMap(h -> h.getPickups()
                        .stream())
                .noneMatch(pickup -> pickup.getDate().toLocalDate().equals(date)));
        return dateView;
    }

}
