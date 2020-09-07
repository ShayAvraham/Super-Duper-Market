package dataContainers;

import java.util.Map;
import java.util.Objects;

public class ProductDataContainer
{
    public enum ProductPurchaseForm
    {
        WEIGHT, QUANTITY
    }

    private int id;
    private String name;
    private ProductPurchaseForm purchaseForm;
    private int numberOfStoresSellProduct;
    private float averagePrice;
    private float numOfProductWasOrdered;
    private Map<Integer,Integer> pricePerStore;
    private Map<Integer,Float> soldAmountPerStore;

    public ProductDataContainer(int id, String name, ProductPurchaseForm purchaseForm, int numberOfStoresSellProduct, float averagePrice, float numOfProductWasOrdered)
    {
        this.id = id;
        this.name = name;
        this.purchaseForm = purchaseForm;
        this.numberOfStoresSellProduct = numberOfStoresSellProduct;
        this.averagePrice = averagePrice;
        this.numOfProductWasOrdered = numOfProductWasOrdered;
    }

    public ProductDataContainer(int id, String name, ProductPurchaseForm purchaseForm, Map<Integer, Integer> pricePerStore, Map<Integer, Float> soldAmountPerStore) {
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

    public ProductPurchaseForm getPurchaseForm() {
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ProductDataContainer)) return false;
        ProductDataContainer that = (ProductDataContainer) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }
}