package engineLogic;
import dataContainers.*;
import java.util.*;

public class Order
{
    private int id;
    private int storeId;
    private Date orderDate;
    private float deliveryCost;
    private Collection<OrderProduct> orderedProducts;
    private static int idNumber = 1;
    private int numberOfStoresOrderedFrom = 1;
    private boolean isDynamic = false;
    private Customer customer;


    public Order(int id, int storeId, Date date, float deliveryCost, Collection<OrderProduct> productsInOrder)
    {
        this.id = id;
        this.idNumber = id > idNumber ? ++id : idNumber;
        this.storeId = storeId;
        this.orderDate = date;
        this.deliveryCost = deliveryCost;
        this.orderedProducts = new HashSet<>();
        for (OrderProduct product: productsInOrder)
        {
            this.orderedProducts.add(product);
        }
    }

    public Order(int storeId, Date date, float deliveryCost, Collection<OrderProduct> productsInOrder)
    {
        this.id = idNumber++;
        this.storeId = storeId;
        this.orderDate = date;
        this.deliveryCost = deliveryCost;
        this.orderedProducts = new HashSet<>();
        for (OrderProduct product: productsInOrder)
        {
            this.orderedProducts.add(product);
        }
    }

    public Order(Date date, float deliveryCost, int numberOfStoresOrderedFrom,Collection<OrderProduct> productsInOrder)
    {
        this.id = idNumber++;
        this.orderDate = date;
        this.deliveryCost = deliveryCost;
        this.numberOfStoresOrderedFrom = numberOfStoresOrderedFrom;
        this.orderedProducts = new HashSet<>();
        for (OrderProduct product: productsInOrder)
        {
            this.orderedProducts.add(product);
        }
        this.isDynamic = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int shopId) {
        this.storeId = shopId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(float deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Collection<OrderProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(Collection<OrderProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getNumberOfStoresOrderedFrom() {
        return numberOfStoresOrderedFrom;
    }

    public float getCostOfAllProducts()
    {
        float costOfAllProducts = 0;
        for (OrderProduct orderProduct: orderedProducts)
        {
            costOfAllProducts += orderProduct.getAmount() * orderProduct.getPrice();
        }
        return  costOfAllProducts;
    }

    public float getTotalCostOfOrder()
    {
        return getCostOfAllProducts() + deliveryCost;
    }

    public boolean isDynamic()
    {
        return isDynamic;
    }

    public float getProductAmountInOrder(Product product)
    {
        float productAmountInOrder = 0;

        for (OrderProduct orderProduct: orderedProducts)
        {
            if(orderProduct.getId() == product.getId())
            {
                productAmountInOrder = orderProduct.getAmount();
                break;
            }
        }
        return productAmountInOrder;
    }

    public void addOrderProduct(OrderProduct productToAdd)
    {
        orderedProducts.add(productToAdd);
    }

    public int getAllOrderedProductsQuantity()
    {
        int allOrderedProductsQuantity = 0;

        for (OrderProduct orderProduct: orderedProducts)
        {
            float productQuantity = orderProduct.getAmount();
            if(orderProduct.getPurchaseForm() == ProductDataContainer.ProductPurchaseForm.WEIGHT)
            {
                productQuantity = 1;
            }
            allOrderedProductsQuantity += productQuantity;

        }
        return allOrderedProductsQuantity;
    }

    @Override
    public String toString() {
        return "engineLogic.Order details: " + "\n" +
                "Date: " + orderDate + "\n" +
                "Delivery cost: " + deliveryCost + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
