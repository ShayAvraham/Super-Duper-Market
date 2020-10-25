package dataContainers;

import java.util.Date;

public class NoticeDataContainer
{
    /**  feedback **/
    private String regionName;
    private int storeID;
    private int rank;
    private String description;
    private Date date;

    /**  orderNotice **/
    private int orderId;
    private int numOfProductsType;
    private float productsCost;
    private float deliveryCost;

    /**  common **/
    private String customerName;
    private String strMessage;
    private String type;

    /**  From Feedback **/
    public NoticeDataContainer(String regionName, int storeID, String customerName, int rank, String description, Date date, String strMassage)
    {
        this.regionName = regionName;
        this.storeID = storeID;
        this.customerName = customerName;
        this.rank = rank;
        this.description = description;
        this.date = date;
        this.strMessage = strMassage;
        this.type = "feedback";
    }

    /**  From Order Notice **/
    public NoticeDataContainer(int orderId, int numOfProductsType, float productsCost, float deliveryCost, String customerName, String strMassage)
    {
        this.orderId = orderId;
        this.numOfProductsType = numOfProductsType;
        this.productsCost = productsCost;
        this.deliveryCost = deliveryCost;
        this.customerName = customerName;
        this.strMessage = strMassage;
        this.type = "orderNotice";
    }

    public String getRegionName() {
        return regionName;
    }

    public int getStoreID() {
        return storeID;
    }

    public int getRank() {
        return rank;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public int getOrderId() {
        return orderId;
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

    public String getCustomerName() {
        return customerName;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public String getType() {
        return type;
    }
}
