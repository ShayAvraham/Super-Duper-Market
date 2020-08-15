import java.util.Date;
import java.util.Map;

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
    private Map<Integer,Float> amountPerProduct;
    private int numberOfStoresOrderedFrom = 1;


    public OrderDataContainer(Date date, int numOfProducts, float costOfAllProducts, float deliveryCost, float totalCost) {
        this.date = date;
        this.numOfProducts = numOfProducts;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;
    }

    public OrderDataContainer(int id, Date date, int storeId, String storeName, int numOfProductTypes, int numOfProducts, float costOfAllProducts, float deliveryCost, float totalCost)
    {
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


    public OrderDataContainer(Date date, float deliveryCost, int storeId, Map<Integer, Float> amountPerProduct) {
        this.date = date;
        this.deliveryCost = deliveryCost;
        this.storeId = storeId;
        this.amountPerProduct = amountPerProduct;
    }

/*
    //bonus dynamic new order
    public OrderDataContainer(Date date,float deliveryCost,Map<Integer, Float> amountPerProduct,int numberOfStoresOrderedFrom) {
        this.date = date;
        this.deliveryCost = deliveryCost;
        this.amountPerProduct = amountPerProduct;
        this.numberOfStoresOrderedFrom = numberOfStoresOrderedFrom;
    }
//bonus
*/

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

    public Map<Integer, Float> getAmountPerProduct() {
        return amountPerProduct;
    }
}
