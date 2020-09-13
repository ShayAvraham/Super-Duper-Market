package dataContainers;

import javax.swing.text.Position;
import java.awt.*;

public class CustomerDataContainer
{
    private int id;
    private String name;
    private int numOfOrders;
    private float orderCostAvg;
    private float deliveryCostAvg;
    private Point position;

    public CustomerDataContainer(int id, String name, int numOfOrders, float orderCostAvg, float deliveryCostAvg, Point position)
    {
        this.id = id;
        this.name = name;
        this.numOfOrders = numOfOrders;
        this.orderCostAvg = orderCostAvg;
        this.deliveryCostAvg = deliveryCostAvg;
        this.position = position;
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

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString()
    {
        return name +" | " +
                "id:" + id;
    }
}
