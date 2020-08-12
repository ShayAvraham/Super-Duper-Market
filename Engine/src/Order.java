import java.util.*;

public class Order
{
    private int id;
    private int storeId;
    private Date orderDate;
    private float deliveryCost;
    private Collection<OrderProduct> orderedProducts;


    public Order(int id, int storeId, Date date, float deliveryCost, Collection<OrderProduct> productsInOrder)
    {
        this.id = id;
        this.storeId = storeId;
        this.orderDate = date;
        this.deliveryCost = deliveryCost;
        this.orderedProducts = new HashSet<>();
        for (OrderProduct product: productsInOrder)
        {
            this.orderedProducts.add(product);
        }
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
        float totalCost = getCostOfAllProducts() + deliveryCost;
        return totalCost;
    }

    public float getProductQuantityInOrder(Product product)
    {
        float productQuantityInOrder = 0;

        for (OrderProduct orderProduct: orderedProducts)
        {
            if(orderProduct.equals(product))
            {
                productQuantityInOrder = orderProduct.getAmount();
            }
        }
        return productQuantityInOrder;
    }

    public int getAllOrderedProductsQuantity()
    {
        int allOrderedProductsQuantity = 0;

        for (OrderProduct orderProduct: orderedProducts)
        {
            allOrderedProductsQuantity += orderProduct.getAmount();
        }
        return allOrderedProductsQuantity;
    }

    @Override
    public String toString() {
        return "Order details: " + "\n" +
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
