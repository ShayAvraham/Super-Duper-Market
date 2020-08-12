import java.util.Date;

public class OrderDataContainer
{
    private int id;
    private Date date;
    private int numOfProducts;
    private float costOfAllProducts;
    private float deliveryCost;
    private float totalCost;
    private int storeId;
    private String storeName;
    private int numOfProductTypes;


    public OrderDataContainer(int id, Date date, int numOfProducts, float costOfAllProducts, float deliveryCost, float totalCost) {
        this.id = id;
        this.date = date;
        this.numOfProducts = numOfProducts;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;
    }

    public OrderDataContainer(int id, Date date, int numOfProducts, float costOfAllProducts, float deliveryCost, float totalCost, int storeId, String storeName, int numOfProductTypes) {
        this.id = id;
        this.date = date;
        this.numOfProducts = numOfProducts;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;
        this.storeId = storeId;
        this.storeName = storeName;
        this.numOfProductTypes = numOfProductTypes;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getNumOfProducts() {
        return numOfProducts;
    }

    public float getCostOfAllProducts() {
        return costOfAllProducts;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getNumOfProductTypes() {
        return numOfProductTypes;
    }
}
