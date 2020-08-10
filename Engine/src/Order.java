import java.util.*;

public class Order
{
    private int id;
    private int shopId;
    private Date orderDate;
    private float deliveryCost;
    private Collection<OrderProduct> productsInOrder;


    public Order(int id, int shopId, Date date, float deliveryCost, Collection<OrderProduct> productsInOrder)
    {
        this.id = id;
        this.shopId = shopId;
        this.orderDate = date;
        this.deliveryCost = deliveryCost;
        this.productsInOrder = new HashSet<>();
        for (OrderProduct product: productsInOrder)
        {
            this.productsInOrder.add(product);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public Collection<OrderProduct> getProductsInOrder() {
        return productsInOrder;
    }

    public void setProductsInOrder(Collection<OrderProduct> productsInOrder) {
        this.productsInOrder = productsInOrder;
    }

//    float getTotalCostOfOrder()
//    {
//        float totalCost = 0;
//        for (Product product: productInOrder.keySet())
//        {
//            totalCost += productInOrder.get(product) * product.getPrice();
//        }
//        totalCost += deliveryCost;
//
//        return totalCost;
//    }

    int getProductQuantityInOrder(Product product)
    {
        int productQuantityInOrder = 0;

        for (OrderProduct orderProduct: productsInOrder)
        {
            if(orderProduct.getStoreProduct().getProduct().getId() == product.getId())
            {
                productQuantityInOrder = orderProduct.getAmount();
            }
        }
        return productQuantityInOrder;
    }

    @Override
    public String toString() {
        return "Order details: " + "\n" +
                "Date: " + orderDate + "\n" +
                "Delivery cost: " + deliveryCost + "\n";
    }
}
