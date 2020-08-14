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
    private final String POSITION_VALUS_OUT_OF_BOUNDS_MSG = "Error: The store position is out of the bounds of [%1$s,%2$s]";


    private int id;
    private String name;
    private Collection<StoreProduct> productsInStore;
    private Collection<Order> storeOrders;
    private float ppk;
    private Point position;


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
            throw new IndexOutOfBoundsException(String.format(POSITION_VALUS_OUT_OF_BOUNDS_MSG,MIN_BOUND,MAX_BOUND));
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

    float getDeliveryCostByLocation(Point customerPosition)
    {
        float deliveryCostByLocation = (float) position.distance(customerPosition);
        DecimalFormat df = new DecimalFormat("#.##");
        deliveryCostByLocation *= ppk;
        return Float.valueOf(df.format(deliveryCostByLocation));
    }

    float getAllDeliveriesCost()
    {
        float allDeliveriesCost = 0;

        for (Order order: storeOrders)
        {
            allDeliveriesCost += order.getDeliveryCost();
        }

        return allDeliveriesCost;
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

    @Override
    public String toString() {
        return "Store details: " + "\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Price per kilometer: " + ppk + "\n";
    }
}
