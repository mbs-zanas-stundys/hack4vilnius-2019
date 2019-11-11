package lt.vilnius.waste.container.value;

public class ContainerForDateView extends ContainerListView {
    private boolean missedPickup;

    public boolean isMissedPickup() {
        return missedPickup;
    }

    public void setMissedPickup(boolean missedPickup) {
        this.missedPickup = missedPickup;
    }
}
