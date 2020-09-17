package dataContainers;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class OrderDataContainer
{
    private int id;
    private CustomerDataContainer customer;
    private LocalDate date;
    private Map<StoreDataContainer, Collection<ProductDataContainer>> products;
    private Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts;
    private boolean isDynamic;
    private float costOfAllProducts;
    private float deliveryCost;
    private float totalCost;

    /** Create new Order **/

    public OrderDataContainer(LocalDate date,CustomerDataContainer customer ,Map<StoreDataContainer,
            Collection<ProductDataContainer>> products, Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts,
                              boolean isDynamic, float costOfAllProducts, float deliveryCost, float totalCost)
    {
        this.date = date;
        this.customer = customer;
        this.products = products;
        this.discounts = discounts;
        this.isDynamic = isDynamic;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;

    }

    /** Create Order From Data **/

    public OrderDataContainer(int id, LocalDate date, CustomerDataContainer customer,
                              Map<StoreDataContainer, Collection<ProductDataContainer>> products,
                              Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts,boolean isDynamic,
                              float costOfAllProducts, float deliveryCost, float totalCost)
    {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.products = products;
        this.discounts = discounts;
        this.isDynamic = isDynamic;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;

    }

    public int getId() {
        return id;
    }

    public CustomerDataContainer getCustomer() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<StoreDataContainer, Collection<ProductDataContainer>> getProducts() {
        return products;
    }

    public Map<StoreDataContainer, Collection<DiscountDataContainer>> getDiscounts() {
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
}
