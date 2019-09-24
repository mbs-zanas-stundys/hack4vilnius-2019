package lt.metasite.waste.container.dto;

public class ContainerForDateView  extends ContainerListView{
    private boolean missedPickup;

    public boolean isMissedPickup() {
        return missedPickup;
    }

    public void setMissedPickup(boolean missedPickup) {
        this.missedPickup = missedPickup;
    }
}
