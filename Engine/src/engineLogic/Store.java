package engineLogic;

import jaxb.generated.SDMDiscounts;
import jaxb.generated.SDMStore;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Math.round;

public class Store
{
    private static DecimalFormat DECIMAL_FORMAT;

    private int id;
    private String name;
    private Collection<StoreProduct> storeProducts;
    private Collection<Order> storeOrders;
    private Collection<Discount> storeDiscounts;
    private float ppk;
    private Point position;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    public Store(SDMStore store,Point position, Collection<StoreProduct> storeProducts,Collection<Discount>storeDiscounts)
    {
        this.id = store.getId();
        this.name = store.getName();
        this.position = position;
        this.ppk = store.getDeliveryPpk();
        this.storeProducts = storeProducts;
        this.storeDiscounts = storeDiscounts;
        this.storeOrders = new HashSet<>();
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

    public Collection<StoreProduct> getStoreProducts() {
        return storeProducts;
    }

    public void setStoreProducts(Collection<StoreProduct> storeProducts) {
        this.storeProducts = storeProducts;
    }

    public Collection<Discount> getStoreDiscounts() {
        return storeDiscounts;
    }

    public StoreProduct getProductById(int productId)
    {
        StoreProduct storeProduct = null;
        for (StoreProduct currentProduct: storeProducts)
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
        for (StoreProduct product: storeProducts)
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
        storeProducts.remove(productToRemove);
    }

    public void addProduct(StoreProduct productToAdd)
    {
        storeProducts.add(productToAdd);
    }

    public void updateProductPrice(int productId, int newPrice)
    {
        getProductById(productId).setPrice(newPrice);
    }

    @Override
    public String toString() {
        return "engineLogic.Store details: " + "\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Price per kilometer: " + ppk + "\n";
    }
}
