package dataContainers;
import java.awt.*;
import java.util.Collection;
import java.util.Objects;

public class StoreDataContainer
{
    private int id;
    private String name;
    private float ppk;
    private float totalIncomeFromDeliveries;
    private Collection<ProductDataContainer> products;
    private Collection<OrderDataContainer> orders;
    private Collection<DiscountDataContainer> discounts;
    private Point position;

    public StoreDataContainer(int id, String name, Point position, float ppk, float totalIncomeFromDeliveries, Collection<ProductDataContainer> products, Collection<OrderDataContainer> orders, Collection<DiscountDataContainer> discounts)
    {
        this.id = id;
        this.name = name;
        this.position = position;
        this.ppk = ppk;
        this.totalIncomeFromDeliveries = totalIncomeFromDeliveries;
        this.products = products;
        this.orders = orders;
        this.discounts = discounts;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Point getPosition() {
        return position;
    }
    public float getPPK()
    {
        return ppk;
    }

    public float getTotalIncomeFromDeliveries()
    {
        return totalIncomeFromDeliveries;
    }

    public Collection<ProductDataContainer> getProducts()
    {
        return products;
    }

    public Collection<OrderDataContainer> getOrders()
    {
        return orders;
    }

    public Collection<DiscountDataContainer> getDiscounts() {
        return discounts;
    }

    public ProductDataContainer getProductDataContainerById(int productId)
    {
        ProductDataContainer productDataContainer = null;
        for (ProductDataContainer productData: products)
        {
            if (productData.getId() == productId)
            {
                productDataContainer = productData;
                break;
            }
        }
        return productDataContainer;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof StoreDataContainer)) return false;
        StoreDataContainer that = (StoreDataContainer) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId());
    }
}
