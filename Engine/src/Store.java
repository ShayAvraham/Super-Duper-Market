import jaxb.generated.Location;
import jaxb.generated.SDMStore;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Math.round;

public class Store
{
    private final int MIN_BOUND = 1;
    private final int MAX_BOUND = 50;
    private final String POSITION_VALUES_OUT_OF_BOUNDS_MSG = "Error: The store with i.d %1$s position is out of the bounds of [%2$s,%3$s]";
    private static DecimalFormat DECIMAL_FORMAT;

    private int id;
    private String name;
    private Collection<StoreProduct> productsInStore;
    private Collection<Order> storeOrders;
    private float ppk;
    private Point position;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    public Store(SDMStore store ,Collection<StoreProduct> productsInStore)
    {
        this.id = store.getId();
        this.name = store.getName();
        this.position = createPosition(store.getLocation());
        this.ppk = store.getDeliveryPpk();
        this.productsInStore = productsInStore;
        this.storeOrders = new HashSet<>();
    }

    private Point createPosition(Location storeLocation)
    {
        Point position = new Point(storeLocation.getX(),storeLocation.getY());
        if((position.getX() > MAX_BOUND)||(position.getX() < MIN_BOUND)||(position.getY() > MAX_BOUND)||(position.getY() < MIN_BOUND))
        {
            throw new IndexOutOfBoundsException(String.format(POSITION_VALUES_OUT_OF_BOUNDS_MSG,id,MIN_BOUND,MAX_BOUND));
        }
        return position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPPK() {
        return ppk;
    }

    public void getPPK(float ppk) {
        this.ppk = ppk;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Collection<Order> getStoreOrders() {
        return storeOrders;
    }

    public void setStoreOrders(Collection<Order> storeOrders) {
        this.storeOrders = storeOrders;
    }

    public Collection<StoreProduct> getProductsInStore() {
        return productsInStore;
    }

    public void setProductsInStore(Collection<StoreProduct> productsInStore) {
        this.productsInStore = productsInStore;
    }

    public StoreProduct getProductById(int productId)
    {
        StoreProduct storeProduct = null;
        for (StoreProduct currentProduct: productsInStore)
        {
            if(currentProduct.getId() == productId)
            {
                storeProduct = currentProduct;
                break;
            }
        }
        return storeProduct;
    }

    float getDistanceToCustomer(Point customerPosition)
    {
        float distanceToCustomer = Math.abs((float) position.distance(customerPosition));
        return Float.valueOf(DECIMAL_FORMAT.format(distanceToCustomer));
    }

    float getDeliveryCostByLocation(Point customerPosition)
    {
        float deliveryCost = getDistanceToCustomer(customerPosition) * ppk;
        return Float.valueOf(DECIMAL_FORMAT.format(deliveryCost));
    }

    float getHowManyTimesProductSold(Product product)
    {
        float howManyTimesProductSold = 0;

        for (Order order: storeOrders)
        {
            howManyTimesProductSold += order.getProductAmountInOrder(product);
        }

        return howManyTimesProductSold;
    }

    public boolean isProductInStore(Product selectedProduct)
    {
        boolean isProductInStore = false;
        for (StoreProduct product: productsInStore)
        {
            if (product.getId() == selectedProduct.getId())
            {
                isProductInStore = true;
                break;
            }
        }
        return isProductInStore;
    }

    public void addNewOrder(Order newOrder)
    {
        storeOrders.add(newOrder);
    }

    public void removeProduct(int productId)
    {
        Product productToRemove = getProductById(productId);
        productsInStore.remove(productToRemove);
    }

    public void addProduct(StoreProduct productToAdd)
    {
        productsInStore.add(productToAdd);
    }

    public void updateProductPrice(int productId, int newPrice)
    {
        getProductById(productId).setPrice(newPrice);
    }

    @Override
    public String toString() {
        return "Store details: " + "\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Price per kilometer: " + ppk + "\n";
    }

}
