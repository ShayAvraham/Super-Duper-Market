package dataContainers;

import java.util.Map;

public class RegionDataContainer
{
    private String name;
    private String ownerName;
    private int numOfStores;
    private int numOfOrders;
    private float orderCostAvg;
    private Map<Integer,ProductDataContainer> productsData;
    private Map<Integer,StoreDataContainer> storesData;

    public RegionDataContainer(String name, String ownerName, int numOfStores, int numOfOrders, float orderCostAvg,
                               Map<Integer,ProductDataContainer> productsData,Map<Integer,StoreDataContainer> storesData)
    {
        this.name = name;
        this.ownerName = ownerName;
        this.numOfStores = numOfStores;
        this.numOfOrders = numOfOrders;
        this.orderCostAvg = orderCostAvg;
        this.productsData = productsData;
        this.storesData = storesData;

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
