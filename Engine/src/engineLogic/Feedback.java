package engineLogic;

import java.util.Date;

public class Feedback
{
    private String regionName;
    private int storeID;
    //    private int orderID;
    private String customerName;
    private int rank;
    private String description;
    private Date date;

    public Feedback(String regionName, int storeID, String customerName, int rank, String description, Date date) {
        this.regionName = regionName;
        this.storeID = storeID;
        this.customerName = customerName;
        this.rank = rank;
        this.description = description;
        this.date = date;
    }

    public String getRegionName() {
        return regionName;
    }

    public int getStoreID() {
        return storeID;
    }

    public String getCustomerName() {
        return customerName;
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
}
