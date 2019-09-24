package lt.metasite.waste.container.dto;

public class MissedPickupContainerView extends ContainerListView{

    private Boolean missedPickUp;

    public Boolean getMissedPickUp() {
        return missedPickUp;
    }

    public void setMissedPickUp(Boolean missedPickUp) {
        this.missedPickUp = missedPickUp;
    }
}
