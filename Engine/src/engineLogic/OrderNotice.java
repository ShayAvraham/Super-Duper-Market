package engineLogic;

import java.util.Date;

public class OrderNotice implements Notice
{
    private int orderId;
    private String customerName;
    private int numOfProductsType;
    private float productsCost;
    private float deliveryCost;

    public OrderNotice(int orderId, String customerName, int numOfProductsType, float productsCost, float deliveryCost) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.numOfProductsType = numOfProductsType;
        this.productsCost = productsCost;
        this.deliveryCost = deliveryCost;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getNumOfProductsType() {
        return numOfProductsType;
    }

    public float getProductsCost() {
        return productsCost;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }
}
