package dataContainers;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public class OrderDataContainer
{
    private int id;
    private UserDataContainer customer;
    private LocalDate date;
    private Map<StoreDataContainer, Collection<ProductDataContainer>> products;
    private Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts;
    private boolean isDynamic;
    private float costOfAllProducts;
    private float deliveryCost;
    private float totalCost;
    private int amountOfProductsTypes;

    /** Create new Order **/

    public OrderDataContainer(LocalDate date, UserDataContainer customer , Map<StoreDataContainer,
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

    public OrderDataContainer(int id, LocalDate date, UserDataContainer customer,
                              Map<StoreDataContainer, Collection<ProductDataContainer>> products,
                              Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts,boolean isDynamic,
                              float costOfAllProducts, float deliveryCost, float totalCost, int amountOfProductsTypes)
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
        this.amountOfProductsTypes = amountOfProductsTypes;

    }


    /** New **/

    public OrderDataContainer(int id, LocalDate date,
                              Map<StoreDataContainer, Collection<ProductDataContainer>> products,
                              Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts,boolean isDynamic,
                              float costOfAllProducts, float deliveryCost, float totalCost, int amountOfProductsTypes)
    {
        this.id = id;
        this.date = date;
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

    public UserDataContainer getCustomer() {
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

    public int getAmountOfProductsTypes() {
        return amountOfProductsTypes;
    }

}
