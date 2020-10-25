package dataContainers;

import java.awt.*;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class OrderDataContainer
{
    private int id;
    private String regionName;
    private Point orderDestination;
    private String customerName;
    private Date date;
    private Map<Integer, Collection<ProductDataContainer>> products;
    private Map<Integer, Collection<DiscountDataContainer>> discounts;
    private boolean isDynamic;
    private float costOfAllProducts;
    private float deliveryCost;
    private float totalCost;
    private int amountOfProductsTypes;

    /** Create new Order **/

    public OrderDataContainer(Date date, Point orderDestination,String customerName, String regionName , Map<Integer,
            Collection<ProductDataContainer>> products, Map<Integer, Collection<DiscountDataContainer>> discounts,
                              boolean isDynamic, float costOfAllProducts, float deliveryCost, float totalCost)
    {
        this.date = date;
        this.orderDestination = orderDestination;
        this.customerName = customerName;
        this.regionName = regionName;
        this.products = products;
        this.discounts = discounts;
        this.isDynamic = isDynamic;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;

    }

    /** Create Order From Data **/

    public OrderDataContainer(int id, Date date, Point orderDestination,String customerName, String regionName,
                              Map<Integer, Collection<ProductDataContainer>> products,
                              Map<Integer, Collection<DiscountDataContainer>> discounts, boolean isDynamic,
                              float costOfAllProducts, float deliveryCost, float totalCost, int amountOfProductsTypes)
    {
        this.id = id;
        this.date = date;
        this.customerName = customerName;
        this.orderDestination = orderDestination;
        this.regionName = regionName;
        this.products = products;
        this.discounts = discounts;
        this.isDynamic = isDynamic;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;
        this.amountOfProductsTypes = amountOfProductsTypes;

    }


    public int getId() {
        return id;
    }

    public String getRegionName() {
        return regionName;
    }

    public Point getOrderDestination() {
        return orderDestination;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Date getDate() {
        return date;
    }

    public Map<Integer, Collection<ProductDataContainer>> getProducts() {
        return products;
    }

    public Map<Integer, Collection<DiscountDataContainer>> getDiscounts() {
        return discounts;
    }

    public boolean isDynamic() {
        return isDynamic;
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

    public int getAmountOfProductsTypes() {
        return amountOfProductsTypes;
    }

}
