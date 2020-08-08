import jaxb.generated.SDMItem;
import jaxb.generated.SDMPrices;
import jaxb.generated.SDMStore;
import sun.text.CodePointIterator;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static java.lang.Math.round;

public class Store
{
    private int id;
    private String name;
    private Map<Product,Integer> productsInStore;
    private Collection<Order> storeOrders;
    private float ppk;
    private Point position;

    public Store(int id, String name, Collection<Product> productsInStore, float ppk, Point position)
    {
        this.id = id;
        this.name = name;
        this.productsInStore = new HashMap<>();
//        for (Product product: productsInStore)
//        {
//            this.productsInStore.put(product);
//        }
        this.storeOrders = new ArrayList<>();
        this.ppk = ppk;
        this.position = position;
    }

    public Store(SDMStore store ,Map<Product,Integer> productsInStore)
    {
        this.id = store.getId();
        this.name = store.getName();
        this.position = new Point(store.getLocation().getX(),store.getLocation().getY());
        this.ppk = store.getDeliveryPpk();
        this.productsInStore = productsInStore;
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

    public float getPpk() {
        return ppk;
    }

    public void setPpk(float ppk) {
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

//    public Collection<Product> getProductsInStore() {
//        return productsInStore;
//    }
//
//    public void setProductsInStore(Collection<Product> productsInStore) {
//        this.productsInStore = productsInStore;
//    }

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

//    int getHowManyTimesProductSoldById(int productId)
//    {
//        int howManyTimesProductSold = 0;
//        Product selectedProduct = null;
//
//        for (Product product: productsInStore)
//        {
//            if (product.getId() == productId)
//            {
//                selectedProduct = product;
//                break;
//            }
//        }
//        for (Order order: storeOrders)
//        {
//            howManyTimesProductSold += order.getProductQuantity(selectedProduct);
//        }
//
//        return howManyTimesProductSold;
//    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productsInStore=" + productsInStore +
                ", ppk=" + ppk +
                ", position=" + position +
                '}';
    }
}
