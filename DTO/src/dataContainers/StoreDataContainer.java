package dataContainers;
import java.awt.*;
import java.util.Collection;
import java.util.Objects;

public class StoreDataContainer
{
    private int id;
    private String name;
    private String ownerName;
    private float ppk;
    private float totalIncomeFromDeliveries;
    private float totalIncomeFromProducts;
    private Collection<ProductDataContainer> products;
    private Collection<OrderDataContainer> orders;
    private Collection<DiscountDataContainer> discounts;
    private Point position;

    public StoreDataContainer(int id, String name, String ownerName, Point position, float ppk,
                              float totalIncomeFromDeliveries, float totalIncomeFromProducts,
                              Collection<ProductDataContainer> products, Collection<OrderDataContainer> orders,
                              Collection<DiscountDataContainer> discounts)
    {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.position = position;
        this.ppk = ppk;
        this.totalIncomeFromDeliveries = totalIncomeFromDeliveries;
        this.totalIncomeFromProducts = totalIncomeFromProducts;
        this.products = products;
        this.orders = orders;
        this.discounts = discounts;
    }


    public StoreDataContainer(int id, String name, String ownerName,Point position ,float ppk,Collection<ProductDataContainer> products)
    {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.position = position;
        this.ppk = ppk;
        this.products = products;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Point getPosition() {
        return position;
    }

    public float getPpk()
    {
        return ppk;
    }

    public float getTotalIncomeFromDeliveries()
    {
        return totalIncomeFromDeliveries;
    }

    public float getTotalIncomeFromProducts() {
        return totalIncomeFromProducts;
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

//    @Override
//    public String toString() {
//        return name +" | " +
//                "id: " + id;
//    }
}
