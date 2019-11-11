package lt.vilnius.waste.container.value;

import lt.vilnius.waste.container.Pickup;

import java.util.List;

public class PickupContainerView extends ContainerListView {

    private List<Pickup> pickups;

    public ContainerForDateView toContainerForDateView() {
        ContainerForDateView dateView = new ContainerForDateView();
        dateView.setPosition(getPosition());
        dateView.setContainerNo(getContainerNo());
        dateView.setId(getId());
        dateView.setMissedPickup(pickups == null || pickups.isEmpty());
        return dateView;
    }

}
