package dataContainers;

import javax.swing.text.Position;
import java.awt.*;

public class UserDataContainer
{
    private int id;
    private String name;
    private String role;
    private int numOfOrders;
    private float orderCostAvg;
    private float deliveryCostAvg;


    /** new user form client to server **/
    public UserDataContainer(String name, String role)
    {
        this.id = 0;
        this.name = name;
        this.role = role;
        this.numOfOrders = 0;
        this.orderCostAvg = 0;
        this.deliveryCostAvg = 0;
    }

    /** from exist user **/
    public UserDataContainer(int id, String name,String role,int numOfOrders, float orderCostAvg, float deliveryCostAvg)
    {
        this.id = id;
        this.name = name;
        this.role = role;
        this.numOfOrders = numOfOrders;
        this.orderCostAvg = orderCostAvg;
        this.deliveryCostAvg = deliveryCostAvg;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumOfOrders() {
        return numOfOrders;
    }

    public float getOrderCostAvg() {
        return orderCostAvg;
    }

    public float getDeliveryCostAvg() {
        return deliveryCostAvg;
    }

    @Override
    public String toString()
    {
        return name +" | " +
                "id:" + id;
    }
}
