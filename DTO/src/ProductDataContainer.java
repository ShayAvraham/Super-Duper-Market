import java.util.Collection;
import java.util.Map;

public class ProductDataContainer
{
    private int id;
    private String name;
    private Product.ProductPurchaseForm purchaseForm;
    private int numberOfStoresSellProduct;
    private float averagePrice;
    private float numOfProductWasOrdered;
    private Map<Integer,Integer> pricePerStore;
    private Map<Integer,Float> soldAmountPerStore;

    public ProductDataContainer(int id, String name, Product.ProductPurchaseForm purchaseForm, int numberOfStoresSellProduct, float averagePrice, float numOfProductWasOrdered)
    {
        this.id = id;
        this.name = name;
        this.purchaseForm = purchaseForm;
        this.numberOfStoresSellProduct = numberOfStoresSellProduct;
        this.averagePrice = averagePrice;
        this.numOfProductWasOrdered = numOfProductWasOrdered;
    }

    public ProductDataContainer(int id, String name, Product.ProductPurchaseForm purchaseForm, Map<Integer, Integer> pricePerStore, Map<Integer, Float> soldAmountPerStore) {
        this.id = id;
        this.name = name;
        this.purchaseForm = purchaseForm;
        this.pricePerStore = pricePerStore;
        this.soldAmountPerStore = soldAmountPerStore;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Product.ProductPurchaseForm getPurchaseForm() {
        return purchaseForm;
    }

    public int getNumberOfStoresSellProduct() {
        return numberOfStoresSellProduct;
    }

    public float getAveragePrice() {
        return averagePrice;
    }

    public float getNumOfProductWasOrdered() {
        return numOfProductWasOrdered;
    }

    public Map<Integer, Integer> getPricePerStore() {
        return pricePerStore;
    }

    public Map<Integer, Float> getSoldAmountPerStore() {
        return soldAmountPerStore;
    }
}
