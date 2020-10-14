package dataContainers;

public class RegionDataContainer
{
    private String name;
    private String ownerName;
    private int numOfStores;
    private int numOfOrders;
    private float orderCostAvg;

    public RegionDataContainer(String name, String ownerName, int numOfStores, int numOfOrders, float orderCostAvg)
    {
        this.name = name;
        this.ownerName = ownerName;
        this.numOfStores = numOfStores;
        this.numOfOrders = numOfOrders;
        this.orderCostAvg = orderCostAvg;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getNumOfStores() {
        return numOfStores;
    }

    public int getNumOfOrders() {
        return numOfOrders;
    }

    public float getOrderCostAvg() {
        return orderCostAvg;
    }
}
