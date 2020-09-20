package dataContainers;

import javafx.beans.property.*;

import java.util.Map;
import java.util.Objects;

public class ProductDataContainer
{

    private int id;
    private String name;
    private String purchaseForm;
    private int numberOfStoresSellProduct;
    private float averagePrice;
    private float numOfProductWasOrdered;
    private Map<Integer,Integer> pricePerStore;
    private Map<Integer,Float> soldAmountPerStore;

    private final BooleanProperty checked = new SimpleBooleanProperty(false);
    private final SimpleDoubleProperty amount = new SimpleDoubleProperty(1);
    private final SimpleIntegerProperty price = new SimpleIntegerProperty(1);

    public ProductDataContainer(int id, String name, String purchaseForm, int numberOfStoresSellProduct, float averagePrice, float numOfProductWasOrdered)
    {
        this.id = id;
        this.name = name;
        this.purchaseForm = purchaseForm;
        this.numberOfStoresSellProduct = numberOfStoresSellProduct;
        this.averagePrice = averagePrice;
        this.numOfProductWasOrdered = numOfProductWasOrdered;
    }

    public ProductDataContainer(int id, String name, String purchaseForm, Map<Integer, Integer> pricePerStore, Map<Integer, Float> soldAmountPerStore) {
        this.id = id;
        this.name = name;
        this.purchaseForm = purchaseForm;
        this.pricePerStore = pricePerStore;
        this.soldAmountPerStore = soldAmountPerStore;
    }

    //product in order
    public ProductDataContainer(ProductDataContainer product , float amount)
    {
        this.id = product.getId();
        this.name = product.getName();
        this.purchaseForm = product.getPurchaseForm();
        this.numberOfStoresSellProduct = product.getNumberOfStoresSellProduct();
        this.averagePrice = product.getAveragePrice();
        this.numOfProductWasOrdered = product.getNumOfProductWasOrdered();
        this.pricePerStore = product.getPricePerStore();
        this.soldAmountPerStore = product.getSoldAmountPerStore();
        this.amountProperty().setValue(amount);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPurchaseForm() {
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

    public boolean isChecked()
    {
        return checked.get();
    }

    public BooleanProperty checkedProperty()
    {
        return checked;
    }

    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public int getPrice() {
        return price.get();
    }

    public SimpleIntegerProperty priceProperty() {
        return price;
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

    @Override
    public String toString()
    {
        return name +" | " +
                "id: " + id;
    }
}
