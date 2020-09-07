import javax.swing.text.Position;
import java.awt.*;

public class CustomerDataContainer
{
    private int id;
    private String name;
    private float numeOfOrders;
    private float orderCostAvg;
    private float deliveryContAvg;
    private Point position;

    public CustomerDataContainer(int id, String name, float numeOfOrders, float orderCostAvg, float deliveryContAvg, Point position)
    {
        this.id = id;
        this.name = name;
        this.numeOfOrders = numeOfOrders;
        this.orderCostAvg = orderCostAvg;
        this.deliveryContAvg = deliveryContAvg;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getNumeOfOrders() {
        return numeOfOrders;
    }

    public float getOrderCostAvg() {
        return orderCostAvg;
    }

    public float getDeliveryContAvg() {
        return deliveryContAvg;
    }

    public Point getPosition() {
        return position;
    }
}
