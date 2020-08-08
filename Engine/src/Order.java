import java.awt.*;
import java.util.*;

public class Order
{
    private int id;
    private int shopId;
    private Date orderDate;
    private float deliveryCost;
    private Map<Product, Integer> productInOrder;

    public Order(int id, int shopId, Date date, float deliveryCost, Map<Product, Integer> productInOrder)
    {
        this.id = id;
        this.shopId = shopId;
        this.orderDate = date;
        this.deliveryCost = deliveryCost;
        this.productInOrder = new HashMap<Product, Integer>();
        for (Product product: productInOrder.keySet())
        {
            this.productInOrder.put(product, productInOrder.get(product));
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

    public Map<Product, Integer> getProductInOrder() {
        return productInOrder;
    }

    public void setProductInOrder(Map<Product, Integer> productInOrder) {
        this.productInOrder = productInOrder;
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

    int getProductQuantity(Product product)
    {
        return productInOrder.get(product);
    }
}
