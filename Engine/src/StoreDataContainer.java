import java.util.Collection;

public class StoreDataContainer
{
    private int id;
    private String name;
    private float ppk;
    private float totalIncomeFromDeliveries;
    private Collection<ProductDataContainer> products;
    private Collection<OrderDataContainer> orders;

    public StoreDataContainer(int id, String name, float ppk, float totalIncomeFromDeliveries, Collection<ProductDataContainer> products, Collection<OrderDataContainer> orders)
    {
        this.id = id;
        this.name = name;
        this.ppk = ppk;
        this.totalIncomeFromDeliveries = totalIncomeFromDeliveries;
        this.products = products;
        this.orders = orders;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
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
}
